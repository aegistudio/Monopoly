package net.aegistudio.monopoly;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import net.aegistudio.monopoly.crud.CrudStatementSet;
import net.aegistudio.monopoly.crud.Iterator;

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
	

	public static interface SQLCallable<T> {	public T apply(Relation relation) throws SQLException;	}
	protected <T> T getRelationComplete(Object object, SQLCallable<T> todo) throws SQLException {
		Relation relation = this.getRelation(object.getClass());
		if(relation == null)
			throw new RuntimeException("The provided entity has not yet been registered!");
		if(relation.defaultStatements == null)
			throw new RuntimeException("This table may have unresolved dependency!");
		return todo.apply(relation);
	}
	
	
	public void create() throws SQLException {
		boolean removeTable;
		do {
			removeTable = false;
			java.util.Iterator<Relation> relationIterator = pendingTable.iterator();
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
	
	public boolean insert(Object entity) throws SQLException {
		return this.getRelationComplete(entity, relation ->
			relation.defaultStatements.insert.insert(entity));
	}
	
	public boolean find(Object entity) throws SQLException {
		return this.getRelationComplete(entity, relation ->
			relation.defaultStatements.find.find(entity));
	}
	
	public boolean update(Object entity) throws SQLException {
		return this.getRelationComplete(entity, relation ->
			relation.defaultStatements.update.update(entity));
	}
	
	public boolean delete(Object entity) throws SQLException {
		return this.getRelationComplete(entity, relation ->
			relation.defaultStatements.delete.update(entity));
	}
	
	public <T> void list(T entity, Consumer<T> consumer) throws SQLException {
		this.getRelationComplete(entity, relation -> {
			Iterator iterator = relation.defaultStatements.list.list();
			while(iterator.next()) {
				iterator.unpack(entity);
				consumer.accept(entity);
			}
			return null;
		});
	}
}
