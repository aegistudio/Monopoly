package net.aegistudio.monopoly.crud;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.aegistudio.monopoly.map.UnpackScheme;

public class Iterator {
	public final ResultSet resultSet;
	public final UnpackScheme scheme;
	
	public Iterator(ResultSet resultSet, UnpackScheme scheme) {
		this.resultSet = resultSet;
		this.scheme = scheme;
	}
	
	public boolean previous() throws SQLException {
		return resultSet.previous();
	}
	
	public boolean next() throws SQLException {
		return resultSet.next();
	}
	
	public void unpack(Object... object) throws SQLException {
		scheme.unpack(resultSet, object);
	}
}
