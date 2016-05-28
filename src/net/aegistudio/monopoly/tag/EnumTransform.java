package net.aegistudio.monopoly.tag;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class EnumTransform implements Transform<Enum<?>, Integer> {
	@Override
	public Enum<?> toObject(Field field, Integer relationData) {
		if(relationData == null) return null;
		try {
			return (Enum<?>) Array.get(
					field.getType().getMethod("values")
					.invoke(null), relationData);
		}
		catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Integer toRelation(Field field, Enum objectData) {
		if(objectData == null) return null;
		return objectData.ordinal();
	}
}
