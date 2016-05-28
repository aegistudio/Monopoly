package net.aegistudio.monopoly.map;

public interface MappingPolicy {
	public Integer getObjectIndex(String objectName);
	
	public Mapping getMapping(String column);
}
