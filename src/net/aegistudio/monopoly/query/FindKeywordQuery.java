package net.aegistudio.monopoly.query;

import net.aegistudio.monopoly.Relation;

public class FindKeywordQuery extends FindSingleQuery {
	public FindKeywordQuery(Relation metadata) {
		super(metadata, "");
		super.addFieldEquals(metadata, PRIMARY_KEY_ONLY);
	}
}
