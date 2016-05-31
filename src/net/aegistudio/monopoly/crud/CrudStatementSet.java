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
import net.aegistudio.monopoly.query.DeleteKeywordQuery;
import net.aegistudio.monopoly.query.FindKeywordQuery;
import net.aegistudio.monopoly.query.FindSingleQuery;
import net.aegistudio.monopoly.query.InsertSingleQuery;
import net.aegistudio.monopoly.query.QueryBuilder;
import net.aegistudio.monopoly.query.UpdateKeywordQuery;

/**
 * Some basic CRUD statements.
 * @author aegistudio
 */

public class CrudStatementSet {
	public final InsertTransation insert;
	public final FindTransation find;
	public final ListTransation list;
	public final UpdateTransation update, delete;
	
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
		this.insert = new InsertTransation(relation, new InsertSingleQuery(relation).getQuery());
		
		// The find by id operation.
		this.find = new FindTransation(relation, new FindKeywordQuery(relation).getQuery());
		this.list = new ListTransation(relation, new FindSingleQuery(relation, QueryBuilder.ALL_COLUMN, "true").getQuery());
		
		// The update and delete by id operation.
		this.update = new UpdateTransation(relation, new UpdateKeywordQuery(relation).getQuery());
		this.delete = new UpdateTransation(relation, new DeleteKeywordQuery(relation).getQuery());
	}
}
