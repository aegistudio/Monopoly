package net.aegistudio.monopoly;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.aegistudio.monopoly.crud.CrudStatementSet;

public class Database {
	public final Configuration config;
	public final Connection connection;
	public Database(Configuration config, Connection connection) {
		this.config = config;
		this.connection = connection;
	}
	
	public Database(Connection connection) {
		this(new Configuration(), connection);
	}

	public final Map<Class<?>, Relation> registeredTable = new HashMap<Class<?>, Relation>();
	
	private final Set<Relation> pendingTable = new HashSet<Relation>();
	
	public final Set<Class<?>> allTableName = new HashSet<Class<?>>();
	public final Set<String> registeredTableName = new TreeSet<String>();
	
	public <T> void register(Class<T> table) throws SQLException {
		if(allTableName.contains(table)) return;
		
		Relation meta = new Relation(this, table);
		pendingTable.add(meta);
		allTableName.add(table);
		
		this.create();
	}
	
	public Relation getRelation(Class<?> clazz) {
		Relation relation = registeredTable.get(clazz);
		return relation;
	}
	
	public Relation getRelationComplete(Class<?> clazz) {
		Relation relation = this.getRelation(clazz);
		if(relation == null)
			throw new RuntimeException("The provided entity has not yet been registered!");
		if(relation.defaultStatements == null)
			throw new RuntimeException("This table may have unresolved dependency!");
		return relation;
	}
	
	public void create() throws SQLException {
		boolean removeTable;
		do {
			removeTable = false;
			Iterator<Relation> relationIterator = pendingTable.iterator();
			while(relationIterator.hasNext()) {
				Relation relation = relationIterator.next();
				if(registeredTableName.containsAll(relation.dependencies)) {
					relationIterator.remove();
					removeTable = true;
					
					relation.defaultStatements = new CrudStatementSet(relation, this);
					
					registeredTableName.add(relation.getName());
					registeredTable.put(relation.objectClazz, relation);
				}
			}
		}
		while(removeTable);
	}
	
	public void insert(Object entity) throws SQLException {
		Relation relation = this.getRelationComplete(entity.getClass());
		relation.defaultStatements.insert.insert(entity);
	}
}
