package net.aegistudio.monopoly.query;

import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;

public class InsertSingleQuery extends QueryBuilder {
	public InsertSingleQuery(Relation metadata) {
		this(metadata, NO_AUTO_INCREMENT);
	}

	public InsertSingleQuery(Relation table, Predicate<Column> acceptence) {
		super.add("insert into ");
		super.add(table.getName());
		super.add(" (");
		super.addFieldNames(table, acceptence);
		super.add(") values (");
		super.addFieldQuestions(table, acceptence);
		super.add(")");
	}
}
