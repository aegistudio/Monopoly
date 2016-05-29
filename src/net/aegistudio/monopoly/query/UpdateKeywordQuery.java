package net.aegistudio.monopoly.query;

import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;

public class UpdateKeywordQuery extends QueryBuilder {
	public UpdateKeywordQuery(Relation table) {
		this(table, ALL_BUT_PRIMARY_KEY, PRIMARY_KEY_ONLY);
	}
	
	public UpdateKeywordQuery(Relation table, Predicate<Column> updated, Predicate<Column> select) {
		super.add("update ");
		super.add(table.getName());
		super.add(" set ");
		super.addFieldEquals(table, ", ", updated);
		super.add(" where ");
		super.addFieldEquals(table, select);
	}
}
