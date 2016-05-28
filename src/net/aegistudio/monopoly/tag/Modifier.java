package net.aegistudio.monopoly.tag;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.query.ListBuilder;

public enum Modifier {
	AUTO_INCREMENT(AutoIncrement.class) {
		public String afterName(Column field, Annotation annotation) {
			return "auto_increment";
		}
	},
	NOT_NULLABLE(NotNullable.class) {
		public String afterName(Column field, Annotation annotation) {
			return "not null";
		}
	},
	PRIMARY_KEY(PrimaryKey.class) {
		ListBuilder<Column> dispatcher = new ListBuilder<Column>((Column field) -> field.getName());
		
		public String afterList(Collection<Column> annotatedFields) {
			if(annotatedFields.size() == 0) return null;
			StringBuilder builder = new StringBuilder("primary key (");
			builder.append(dispatcher.build(annotatedFields));
			builder.append(")");
			return new String(builder);
		}
	},
	UNIQUE(Unique.class) {
		ListBuilder<Column> dispatcher = new ListBuilder<Column>((Column field) -> {
			StringBuilder builder = new StringBuilder("unique (");
			builder.append(field.getName());
			builder.append(")");
			return new String(builder);
		});
		
		public String afterList(Collection<Column> annotatedFields) {
			if(annotatedFields.size() == 0) return null;
			return dispatcher.build(annotatedFields);
		}
	},
	FOREIGN_KEY(ForeignKey.class) {
		ListBuilder<Column> dispatcher = new ListBuilder<Column>((Column field) -> {
			ForeignKey foreign = (ForeignKey) field.getAnnotation(ForeignKey.class);
			StringBuilder builder = new StringBuilder("foreign key (");
			builder.append(field.getName());
			builder.append(") references ");
			builder.append(foreign.value());
			builder.append("(");
			builder.append(foreign.field());
			builder.append(")");
			return new String(builder);
		});
		
		public String afterList(Collection<Column> annotatedFields) {
			if(annotatedFields.size() == 0) return null;
			return dispatcher.build(annotatedFields);
		}
	};
	
	private final Class<? extends Annotation> annotation;
	private Modifier(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}
	
	public String afterName(Column field, Annotation annotation) { return null; }
	public String afterList(Collection<Column> annotatedFields) { return null; }
	
	@SuppressWarnings("unchecked")
	public static String build(Relation metadata) {
		Iterable<Column> fields = metadata.fieldMap.values();
		
		ArrayList<Column>[] classifier = new ArrayList[values().length];
		for(int i = 0; i < classifier.length; i ++) classifier[i] = new ArrayList<Column>();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(new ListBuilder<Column>((field) -> {
			String typeString = field.getTypeString();
			if(typeString == null) return null;
			StringBuilder lineBuilder = new StringBuilder();
			
			lineBuilder.append(field.getName());
			lineBuilder.append(' ');
			lineBuilder.append(typeString);
			
			for(int i = 0; i < values().length; i ++) {
				Modifier modifier = values()[i];
				
				Annotation annotation = field.getAnnotation(modifier.annotation);
				if(annotation == null) continue;
				classifier[i].add(field);
				
				String after = modifier.afterName(field, annotation);
				if(after == null) continue;
				lineBuilder.append(" ");
				lineBuilder.append(after);
			}
			return new String(lineBuilder);
		})
		.build(fields));
		
		for(int i = 0; i < values().length; i ++) {
			Modifier modifier = values()[i];
			String afterList = modifier.afterList(classifier[i]);
			if(afterList == null) continue;
			
			builder.append(", ");
			builder.append(afterList);
		}
		
		return new String(builder);
	}
}
