package net.aegistudio.monopoly.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import net.aegistudio.monopoly.Database;
import net.aegistudio.monopoly.Tokenizer;

public class PackScheme {
	public final Database database;
	public final Integer[] objectMap;
	public final Mapping[] fieldMap;
	public final PreparedStatement statement;
	
	public PackScheme(Database database, String query, MappingPolicy policy) throws SQLException {
		this.database = database;
		
		final ArrayList<Integer> objectMap = new ArrayList<Integer>();
		final ArrayList<Mapping> mapping = new ArrayList<Mapping>();
		Tokenizer tokenizer = new Tokenizer(database.config, query) {
			@Override
			public String acceptToken(String token) {
				objectMap.add(policy.getObjectIndex(token));
				mapping.add(policy.getMapping(token));
				return "?";
			}
		};
		
		this.objectMap = objectMap.toArray(new Integer[0]);
		this.fieldMap = mapping.toArray(new Mapping[0]);
		String processedQuery = tokenizer.getResult();
		this.statement = this.createStatement(processedQuery);
	}
	
	protected PreparedStatement createStatement(String processedQuery) throws SQLException {
		return database.connection.prepareStatement(processedQuery);
	}
	
	protected void pack(Object... o) throws SQLException {
		for(int i = 0; i < objectMap.length; i ++) {
			if(fieldMap[i] != null && objectMap[i] != null)
				statement.setObject(i + 1, fieldMap[i].get(o[objectMap[i]]));
		}
	}
	
	public ResultSet executeQuery(Object... o) throws SQLException {
		synchronized(this.statement) {
			this.pack(o);
			return statement.executeQuery();
		}
	}
	
	public void execute(Object... o) throws SQLException {
		synchronized(this.statement) {
			this.pack(o);
			statement.execute();
		}
	}
	
	public ResultSet executeInsert(Object... o) throws SQLException {
		synchronized(this.statement) {
			this.pack(o);
			statement.execute();
			return statement.getGeneratedKeys();
		}
	}
	
	public int executeUpdate(Object... o) throws SQLException {
		synchronized(this.statement) {
			this.pack(o);
			return statement.executeUpdate();
		}
	}
}
