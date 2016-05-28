package net.aegistudio.monopoly.tag;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DefaultTransform implements Transform {
	public static final Map<Class, Map<Class, Function>> casting 
		= new HashMap<Class, Map<Class, Function>>();
	
	static {
		/** byte / Byte -> ? **/	{
			HashMap<Class, Function> byteCast = new HashMap<>();
			casting.put(Integer.class, byteCast);
			casting.put(int.class, byteCast);
			
			Function byteToByte = i -> (byte)i;
			byteCast.put(byte.class, byteToByte);
			byteCast.put(Byte.class, byteToByte);
			
			Function byteToShort = i -> (short)((byte)i);
			byteCast.put(short.class, byteToShort);
			byteCast.put(Short.class, byteToShort);
			
			Function byteToInt = i -> (int)((byte)i);
			byteCast.put(int.class, byteToInt);
			byteCast.put(Integer.class, byteToInt);
			
			Function byteToLong = i -> (long)((byte)i);
			byteCast.put(long.class, byteToLong);
			byteCast.put(Long.class, byteToLong);
			
			Function byteToString = i -> i.toString();
			byteCast.put(String.class, byteToString);
		}
		
		/** short / Short -> ? **/	{
			HashMap<Class, Function> shortCast = new HashMap<>();
			casting.put(Short.class, shortCast);
			casting.put(short.class, shortCast);
			
			Function shortToByte = i -> (byte)((short)i);
			shortCast.put(byte.class, shortToByte);
			shortCast.put(Byte.class, shortToByte);
			
			Function shortToShort = i -> (short)i;
			shortCast.put(short.class, shortToShort);
			shortCast.put(Short.class, shortToShort);
			
			Function shortToInt = i -> (int)((short)i);
			shortCast.put(int.class, shortToInt);
			shortCast.put(Integer.class, shortToInt);
			
			Function shortToLong = i -> (long)((short)i);
			shortCast.put(long.class, shortToLong);
			shortCast.put(Long.class, shortToLong);
			
			Function shortToString = i -> i.toString();
			shortCast.put(String.class, shortToString);
		}
		
		/** int / Integer -> ? **/	{
			HashMap<Class, Function> integerCast = new HashMap<>();
			casting.put(Integer.class, integerCast);
			casting.put(int.class, integerCast); 
			
			Function intToByte = i -> (byte)((int)i);
			integerCast.put(byte.class, intToByte);
			integerCast.put(Byte.class, intToByte);
			
			Function intToShort = i -> (short)((int)i);
			integerCast.put(short.class, intToShort);
			integerCast.put(Short.class, intToShort);
			
			Function intToInt = i -> (int)i;
			integerCast.put(int.class, intToInt);
			integerCast.put(Integer.class, intToInt);
			
			Function intToLong = i -> (long)((int)i);
			integerCast.put(long.class, intToLong);
			integerCast.put(Long.class, intToLong);
			
			Function intToString = i -> i.toString();
			integerCast.put(String.class, intToString);
		}
		
		/** long / Long -> ? **/	{
			HashMap<Class, Function> longCast = new HashMap<>();
			casting.put(Long.class, longCast);
			casting.put(long.class, longCast); 
			
			Function longToByte = i -> (byte)((long)i);
			longCast.put(byte.class, longToByte);
			longCast.put(Byte.class, longToByte);
			
			Function longToShort = i -> (short)((long)i);
			longCast.put(short.class, longToShort);
			longCast.put(Short.class, longToShort);
			
			Function longToInt = i -> (int)((long)i);
			longCast.put(int.class, longToInt);
			longCast.put(Integer.class, longToInt);
			
			Function longToLong = i -> (long)i;
			longCast.put(long.class, longToLong);
			longCast.put(Long.class, longToLong);
			
			Function longToString = i -> i.toString();
			longCast.put(String.class, longToString);
		}
		
		/** long / Long -> ? **/	{
			HashMap<Class, Function> stringCast = new HashMap<>();
			casting.put(String.class, stringCast);
			
			stringCast.put(byte.class, i -> Byte.parseByte((String)i));
			stringCast.put(short.class, i -> Short.parseShort((String)i));
			stringCast.put(int.class, i -> Integer.parseInt((String)i));
			stringCast.put(long.class, i -> Long.parseLong((String)i));
			stringCast.put(String.class, i -> (String)i);
		}
	}
	
	@Override
	public Object toObject(Field field, Object relationData) {
		if(relationData == null) return null;
		if(relationData.getClass() == field.getType()) return relationData;
		return casting.get(relationData.getClass())			// the type from cast
				.get(field.getType())						// the type to cast
				.apply(relationData);						// do cast
	}

	@Override
	public Object toRelation(Field field, Object objectData) {
		return objectData;
	}
}