package net.aegistudio.monopoly.query;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.tag.Index;

public class CreateIndexQuery extends QueryBuilder {
	private final Column field;
	public CreateIndexQuery(Relation table, Column field) {
		this.field = field;
		super.add(this.getCreateStatus());
		super.add(table.getName());
		super.add("(");
		super.add(field.getName());
		super.add(")");
	}
	
	public String getCreateStatus() {
		return "create " + getIndexType() + " index " + getIndexName() + " on ";
	}
	
	public String getIndexName() {
		Index index = field.getAnnotation(Index.class);
		String indexName = index.name();
		if(indexName.length() == 0) indexName = "idx_" + field.table.getName() + "_" + field.getName();
		return indexName;
	}
	
	public String getIndexType() {
		return field.getAnnotation(Index.class).value().name;
	}
}
