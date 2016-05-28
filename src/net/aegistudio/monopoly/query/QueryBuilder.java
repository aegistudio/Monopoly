package net.aegistudio.monopoly.query;

public class QueryBuilder {
	StringBuilder result;
	public QueryBuilder() {
		this.result = new StringBuilder();
	}
	
	public void add(String query) {
		result.append(query);
	}
	
	public String getQuery() {
		return new String(result);
	}
}
