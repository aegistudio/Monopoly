package net.aegistudio.monopoly;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.aegistudio.monopoly.crud.CrudStatementSet;
import net.aegistudio.monopoly.tag.Entity;
import net.aegistudio.monopoly.tag.ForeignKey;
import net.aegistudio.monopoly.tag.Index;

public class Relation {
	public final Database database;
	public CrudStatementSet defaultStatements;
	
	public final Class<?> objectClazz;
	public final Entity entityAnnotation;
	
	public final Map<String, Column> fieldMap = new TreeMap<String, Column>();
	public final Set<String> dependencies = new TreeSet<String>();
	public final Set<Column> manualIndices = new HashSet<Column>();
	
	public Relation(Database database, Class<?> objectClazz) {
		this.database = database;
		this.objectClazz = objectClazz;
		this.entityAnnotation = objectClazz
				.getAnnotation(Entity.class);
		
		if(entityAnnotation == null) 
			throw new IllegalArgumentException("Not an entity");
		
		this.translateFields();
	}
	
	public String getName() {
		String name = this.entityAnnotation.value();
		if(name.length() == 0) return objectClazz.getName();
		return name;
	}
	
	protected void translateFields() {
		for(java.lang.reflect.Field field : objectClazz.getDeclaredFields()) {
			if(Modifier.isStatic(field.getModifiers())) continue;
			Column column = new Column(this, field);
			fieldMap.put(column.getName(), column);
			this.acceptField(column);
		}
	}
	
	protected void acceptField(Column field) {	
		ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
		if(foreignKey != null)
			dependencies.add(foreignKey.value());
		
		Index index = field.getAnnotation(Index.class);
		if(index != null)
			this.manualIndices.add(field);
	}
}
