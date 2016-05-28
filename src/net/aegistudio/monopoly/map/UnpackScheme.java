package net.aegistudio.monopoly.map;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * When we finish a query and retrieve a result set.
 * We should re-assign particular fields back to the entity object.
 * 
 * @author aegistudio
 */

public class UnpackScheme {
	public final Integer[] objectMap;
	public final Mapping[] fieldMap;
	public UnpackScheme(ResultSetMetaData metadata, MappingPolicy policy) throws SQLException {
		this.objectMap = new Integer[metadata.getColumnCount()];
		this.fieldMap = new Mapping[metadata.getColumnCount()];
		
		for(int i = 0; i < metadata.getColumnCount(); i ++) {
			int sqlIndex = i + 1;
			String column = metadata.getTableName(sqlIndex) + "." + metadata.getColumnLabel(sqlIndex);
			objectMap[i] = policy.getObjectIndex(column);
			fieldMap[i] = policy.getMapping(column);
		}
	}
	
	public void unpack(ResultSet result, Object... o) throws SQLException {
		for(int i = 0; i < objectMap.length; i ++) {
			if(fieldMap[i] != null && objectMap[i] != null)
				fieldMap[i].set(o[objectMap[i]], result.getObject(i + 1));
		}
	}
}
