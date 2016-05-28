package net.aegistudio.monopoly.crud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import net.aegistudio.monopoly.Column;
import net.aegistudio.monopoly.Database;
import net.aegistudio.monopoly.Relation;
import net.aegistudio.monopoly.query.CreateIndexQuery;
import net.aegistudio.monopoly.query.CreateTableQuery;

/**
 * Some basic CRUD statements.
 * @author aegistudio
 */

public class CrudStatementSet {
	public final InsertTransation insert;
	public CrudStatementSet(Relation relation, Database database) throws SQLException {
		// We should have the relation/table first.
		java.sql.Statement statement = database.connection.createStatement();
		
		CreateTableQuery createTable = new CreateTableQuery(relation);
		statement.execute(createTable.getQuery());
		
		// Create the indices if manual set.
		if(relation.manualIndices.size() > 0) {
			ResultSet indices = database.connection.getMetaData()
					.getIndexInfo("", "", relation.getName(), false, false);
			Set<String> indicesSet = new TreeSet<String>();
			while(indices.next()) 
				indicesSet.add(indices.getString("INDEX_NAME"));
			
			for(Column indexedColumn : relation.manualIndices) {
				CreateIndexQuery createIndex = new CreateIndexQuery(relation, indexedColumn);
				if(indicesSet.contains(createIndex.getIndexName())) continue;
				statement.execute(createIndex.getQuery());
			}
		}
		statement.close();
		
		// Then we make the basic insert operation.
		this.insert = new InsertTransation(relation);
	}
}
