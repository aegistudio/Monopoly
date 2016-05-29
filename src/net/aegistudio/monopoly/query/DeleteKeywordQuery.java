package net.aegistudio.monopoly.query;

import net.aegistudio.monopoly.Relation;

public class DeleteKeywordQuery extends DeleteSingleQuery {
	public DeleteKeywordQuery(Relation table) {
		super(table, PRIMARY_KEY_ONLY);
	}
}
