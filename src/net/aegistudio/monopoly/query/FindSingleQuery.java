package net.aegistudio.monopoly.query;

import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;

public class FindSingleQuery extends QueryBuilder {
	public FindSingleQuery(Relation metadata, String condition) {
		this(metadata, ALL_BUT_PRIMARY_KEY, condition);
	}

	public FindSingleQuery(Relation table, Predicate<Column> acceptence, String condition) {
		super.add("select ");
		super.addFieldNames(table, acceptence);
		super.add(" from ");
		super.add(table.getName());
		super.add(" where ");
		super.add(condition);
	}
}
