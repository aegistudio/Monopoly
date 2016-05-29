package net.aegistudio.monopoly.crud;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.map.ColumnMapPolicy;
import net.aegistudio.monopoly.map.MappingPolicy;
import net.aegistudio.monopoly.map.PackScheme;
import net.aegistudio.monopoly.map.UnpackScheme;

public class FindTransation {
	MappingPolicy columnMap;
	PackScheme findPack;
	UnpackScheme findUnpack;
	public FindTransation(Relation relation, String query) throws SQLException {
		this.columnMap = new ColumnMapPolicy(relation);
		findPack = new PackScheme(relation.database, query, columnMap);
	}
	
	public boolean find(Object object) throws SQLException {
		ResultSet result = findPack.executeQuery(object);
		if(findUnpack == null) 
			findUnpack = new UnpackScheme(result.getMetaData(), columnMap);
		if(!result.next()) return false;
		findUnpack.unpack(result, object);
		return true;
	}
}
