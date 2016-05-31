package net.aegistudio.monopoly.crud;

import java.sql.SQLException;

import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.map.ColumnMapPolicy;
import net.aegistudio.monopoly.map.MappingPolicy;
import net.aegistudio.monopoly.map.PackScheme;

public class UpdateTransation {
	MappingPolicy columnMap;
	PackScheme updatePack;
	public UpdateTransation(Relation relation, String query) throws SQLException {
		this.columnMap = new ColumnMapPolicy(relation);
		updatePack = new PackScheme(relation.database, query, columnMap);
	}
	
	public boolean update(Object... object) throws SQLException {
		return updatePack.executeUpdate(object) > 0;
	}
}
