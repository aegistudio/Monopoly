package net.aegistudio.monopoly.query;

import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.tag.Modifier;

public class CreateTableQuery extends QueryBuilder {
	public CreateTableQuery(Relation table) {
		super.add(this.getCreateStatus());
		super.add(table.getName());
		super.add(" ( ");
		super.add(Modifier.build(table));
		super.add(" ) ");
	}
	
	public String getCreateStatus() {
		return "create table if not exists ";
	}
}
