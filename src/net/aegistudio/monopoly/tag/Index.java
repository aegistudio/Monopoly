package net.aegistudio.monopoly.tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Index {
	public Index.Type value() default Type.BTREE;
	
	public String name() default "";
	
	public static enum Type {
		BTREE(""), UNIQUE("unique"),
		FULLTEXT("fulltext");
		
		public final String name;
		private Type(String name) {
			this.name = name;
		}
	}
}
