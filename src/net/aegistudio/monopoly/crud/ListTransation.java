package net.aegistudio.monopoly.crud;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.map.ColumnMapPolicy;
import net.aegistudio.monopoly.map.MappingPolicy;
import net.aegistudio.monopoly.map.PackScheme;
import net.aegistudio.monopoly.map.UnpackScheme;

public class ListTransation {
	MappingPolicy columnMap;
	PackScheme listPack;
	UnpackScheme listUnpack;
	public ListTransation(Relation relation, String query) throws SQLException {
		this.columnMap = new ColumnMapPolicy(relation);
		listPack = new PackScheme(relation.database, query, columnMap);
	}
	
	public Iterator list(Object... object) throws SQLException {
		ResultSet result = listPack.executeQuery(object);
		if(listUnpack == null) 
			listUnpack = new UnpackScheme(result.getMetaData(), columnMap);
		return new Iterator(result, this.listUnpack);
	}
}
