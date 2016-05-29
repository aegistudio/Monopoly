package net.aegistudio.monopoly.query;

import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;

public class DeleteSingleQuery extends QueryBuilder {
	public DeleteSingleQuery(Relation table, Predicate<Column> acceptence) {
		super.add("delete from ");
		super.add(table.getName());
		super.add(" where ");
		super.addFieldEquals(table, acceptence);
	}
}
