package net.aegistudio.monopoly.map;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Relation;

public class ColumnMapPolicy implements MappingPolicy {
	private final Relation metadata;
	public ColumnMapPolicy(Relation metadata) {
		this.metadata = metadata;
	}
	
	@Override
	public Integer getObjectIndex(String column) {
		return 0;
	}
	
	@Override
	public Column getMapping(String column) {
		return metadata.fieldMap.get(
				column.substring(column.lastIndexOf('.') + 1));
	}
}
