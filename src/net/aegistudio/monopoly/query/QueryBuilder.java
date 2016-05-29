package net.aegistudio.monopoly.query;

import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.tag.AutoIncrement;
import net.aegistudio.monopoly.tag.PrimaryKey;

public class QueryBuilder {
	public static final Predicate<Column> ALL_COLUMN = (column) -> true;
	public static final Predicate<Column> PRIMARY_KEY_ONLY = 
			(column) -> column.getAnnotation(PrimaryKey.class) != null;
	public static final Predicate<Column> ALL_BUT_PRIMARY_KEY = 
			(column) -> column.getAnnotation(PrimaryKey.class) == null;
	public static final Predicate<Column> NO_AUTO_INCREMENT = 
			(column) -> column.getAnnotation(AutoIncrement.class) == null;
	
	public void addFieldEquals(Relation table, Predicate<Column> fieldSelector) {
		this.addFieldEquals(table, " and ", fieldSelector);
	}
	
	public void addFieldEquals(Relation table, String delimeter, Predicate<Column> fieldSelector) {
		String result = new ListBuilder<Column>(delimeter, 
				c -> fieldSelector.test(c)? c.getName() + "={" + c.getName() + "}" : null)
		.build(table.fieldMap.values());
		this.add(result);
	}
	
	public void addFieldNames(Relation table, Predicate<Column> fieldSelector) {
		String result = new ListBuilder<Column>((i) -> fieldSelector.test(i)? i.getName() : null).
				build(table.fieldMap.values());
		this.add(result);
	}
	
	public void addFieldQuestions(Relation table, Predicate<Column> fieldSelector) {
		String begin = table.database.config.BLOCK_BEGIN_TOKEN;
		String end = table.database.config.BLOCK_END_TOKEN;
		
		String result = new ListBuilder<Column>((i) -> fieldSelector.test(i)? begin + i.getName() + end : null).
				build(table.fieldMap.values());
		this.add(result);
	}
	
	StringBuilder result;
	public QueryBuilder() {
		this.result = new StringBuilder();
	}
	
	public void add(String query) {
		result.append(query);
	}
	
	public String getQuery() {
		return new String(result);
	}
}
