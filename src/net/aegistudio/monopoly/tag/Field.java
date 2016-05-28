package net.aegistudio.monopoly.tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Representing the most primitive types provided
 * by a database.
 * 
 * @author aegistudio
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("rawtypes")
public @interface Field {
	public Type value() default Type.AUTO;
	public int length() default 0;
	public Class<? extends Transform> transform() 
		default DefaultTransform.class;
	
	public static final Map<Class<?>, Type> defaultValueMap
		= new HashMap<Class<?>, Type>();
	
	public static enum Type {
		AUTO(null) {
			public String getField(java.lang.reflect.Field field) {
				Class<?> type = field.getType();
				if(type.isArray()) type = type.getComponentType();
				return defaultValueMap.get(type).getField(field);
			}
		},
		TINYINT("tinyint", byte.class, Byte.class),
		SMALLINT("smallint", short.class, Short.class),
		INT("int", int.class, Integer.class),
		BIGINT("bigint", long.class, Long.class),
		CHAR("char", char.class, Character.class),
		VARCHAR("varchar", String.class),	// Must assign length this time.
		DATETIME("datetime", Date.class);
		
		private final String field;
		private Type(String field, Class<?>... defaultMapping) {
			this.field = field;
			for(Class<?> type : defaultMapping)
				defaultValueMap.put(type, this);
		}
		
		public String getField(java.lang.reflect.Field field) {
			return this.field;
		}
	}
}
