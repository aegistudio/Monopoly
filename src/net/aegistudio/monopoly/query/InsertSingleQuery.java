package net.aegistudio.monopoly.query;

import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.tag.AutoIncrement;

public class InsertSingleQuery extends QueryBuilder {
	public static final Predicate<Column> NO_AUTO_INCREMENT = 
			(column) -> column.getAnnotation(AutoIncrement.class) == null;
	
	public InsertSingleQuery(Relation metadata) {
		this(metadata, NO_AUTO_INCREMENT);
	}

	public InsertSingleQuery(Relation table, Predicate<Column> acceptence) {
		String begin = table.database.config.BLOCK_BEGIN_TOKEN;
		String end = table.database.config.BLOCK_END_TOKEN;
		
		super.add("insert into ");
		super.add(table.getName());
		super.add(" (");
		super.add(new ListBuilder<Column>((i) -> acceptence.test(i)? i.getName() : null).
				build(table.fieldMap.values()));
		super.add(") values (");
		super.add(new ListBuilder<Column>((i) -> acceptence.test(i)? begin + i.getName() + end : null).
				build(table.fieldMap.values()));
		super.add(")");
	}
}
