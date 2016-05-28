package net.aegistudio.monopoly;

import java.lang.annotation.Annotation;

import net.aegistudio.monopoly.map.Mapping;
import net.aegistudio.monopoly.tag.DefaultTransform;
import net.aegistudio.monopoly.tag.Field;
import net.aegistudio.monopoly.tag.Transform;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Column implements Mapping {
	private final java.lang.reflect.Field field;
	public final Relation table;
	private final Field.Type type;
	private final int length;
	private final Transform transform;
	
	public Column(Relation table, java.lang.reflect.Field field) {
		this.table = table;
		this.field = field;
		
		Field.Type type = null; int length = 0;
		Transform transform = new DefaultTransform();
		Field annotation = field.getAnnotation(Field.class);
		if(annotation == null) {
			if(table.entityAnnotation.all()) 
				type = Field.Type.AUTO;
		}
		else {
			type = annotation.value();
			length = annotation.length();
			try {
				transform = annotation.transform().newInstance();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		this.type = type;
		this.length = length;
		this.transform = transform;
	}
	
	public String getName() {
		return this.field.getName();
	}
	
	public <V extends Annotation> V getAnnotation(Class<V> annotationClass) {
		return this.field.getAnnotation(annotationClass);
	}
	
	public String getTypeString() {
		if(type == null) return null;
		
		StringBuilder builder = new StringBuilder(type.getField(field));
		if(length > 0) {
			builder.append("(");
			builder.append(length);
			builder.append(")");
		}
		return new String(builder);
	}

	@Override
	public void set(Object target, Object value) {
		try {
			this.field.set(target, transform.toObject(this.field, value));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object get(Object target) {
		try {
			return transform.toRelation(this.field, this.field.get(target));
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
