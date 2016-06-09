package net.aegistudio.monopoly.query;

import java.util.TreeSet;
import java.util.function.Predicate;

import net.aegistudio.monopoly.Column;

/**
 * Used when you want to manipulate just given field.
 * @author aegistudio
 */

public class FieldSpecifier implements Predicate<Column> {
	private final TreeSet<String> acceptedField;
	public FieldSpecifier(String... acceptedField) {
		this.acceptedField = new TreeSet<>();
		for(String field : acceptedField)
			this.acceptedField.add(field);
	}
	@Override
	public boolean test(Column arg0) {
		return this.acceptedField.contains(arg0.getName());
	}
}
