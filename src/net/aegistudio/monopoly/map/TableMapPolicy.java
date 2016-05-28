package net.aegistudio.monopoly.map;

import java.util.TreeMap;

import net.aegistudio.monopoly.Relation;

public class TableMapPolicy implements MappingPolicy {
	
	public TableMapPolicy() {}
	
	private final TreeMap<String, Integer> mapper = new TreeMap<String, Integer>();
	private final TreeMap<String, MappingPolicy> policies = new TreeMap<String, MappingPolicy>();
	
	public void add(String tablename, Relation metadata) {
		this.add(tablename, new ColumnMapPolicy(metadata));
	}
	
	public void add(Relation table) {
		this.add(table.getName(), table);
	}
	
	int currentIndex = 0;	
	public void add(String tablename, MappingPolicy policy) {
		mapper.put(tablename, currentIndex ++);
		policies.put(tablename, policy);
	}

	@Override
	public Integer getObjectIndex(String column) {
		int dotindex = column.indexOf('.');
		String tablename = dotindex < 0? "" : column.substring(0, dotindex);
		return mapper.get(tablename);
	}
	
	@Override
	public Mapping getMapping(String column) {
		int dotindex = column.indexOf('.');
		String tablename = dotindex < 0? "" : column.substring(0, dotindex);
		MappingPolicy policy = policies.get(tablename);
		if(policy == null) return null;
		
		String columnname = column.substring(dotindex + 1);
		return policy.getMapping(columnname);
	}
}
