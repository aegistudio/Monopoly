package net.aegistudio.monopoly.tag;

import java.lang.reflect.Field;

public interface Transform<O, R> {
	public O toObject(Field field, R relationData);
	
	public R toRelation(Field field, O objectData);
}
