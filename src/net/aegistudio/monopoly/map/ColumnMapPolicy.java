package net.aegistudio.monopoly.map;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.tag.PrimaryKey;

public class ColumnMapPolicy implements MappingPolicy {
	private final Relation metadata;
	private Column generatedKey;
	public ColumnMapPolicy(Relation metadata) {
		this.metadata = metadata;
		this.metadata.fieldMap.values().forEach(v -> {
			if(v.getAnnotation(PrimaryKey.class) != null)
				generatedKey = v;
		});
	}
	
	@Override
	public Integer getObjectIndex(String column) {
		return 0;
	}
	
	@Override
	public Column getMapping(String column) {
		String columnName = column.substring(column.lastIndexOf('.') + 1);
		if(columnName.equals("GENERATED_KEY")) return generatedKey;
		return metadata.fieldMap.get(columnName);
	}
}
