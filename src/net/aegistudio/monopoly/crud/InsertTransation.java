package net.aegistudio.monopoly.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.map.ColumnMapPolicy;
import net.aegistudio.monopoly.map.MappingPolicy;
import net.aegistudio.monopoly.map.PackScheme;
import net.aegistudio.monopoly.map.UnpackScheme;
import net.aegistudio.monopoly.query.InsertSingleQuery;

/**
 * The basic insert transation.
 * All column will be set except the auto-increment columns.
 * 
 * @author aegistudio
 */

public class InsertTransation {
	MappingPolicy columnMap;
	PackScheme insertPack;
	UnpackScheme insertUnpack;
	public InsertTransation(Relation relation) throws SQLException {
		this.columnMap = new ColumnMapPolicy(relation);
		InsertSingleQuery insertSingle = new InsertSingleQuery(relation);
		insertPack = new PackScheme(relation.database, insertSingle.getQuery(), columnMap) {
			public PreparedStatement createStatement(String name) throws SQLException {
				return super.database.connection.prepareStatement(name, 
						PreparedStatement.RETURN_GENERATED_KEYS);
			};
		};
	}
	
	public void insert(Object object) throws SQLException {
		ResultSet result = insertPack.executeInsert(object);
		if(insertUnpack == null) 
			insertUnpack = new UnpackScheme(result.getMetaData(), columnMap);
		if(result.next()) insertUnpack.unpack(result, object);
	}
}
