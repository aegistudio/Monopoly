package net.aegistudio.monopoly.query;

import java.util.function.Function;

public class ListBuilder<T> {
	private final String delimeter;
	private final Function<T, String> dispatcher;
	public ListBuilder(String delimeter, Function<T, String> dispatcher) {
		this.delimeter = delimeter;
		this.dispatcher = dispatcher;
	}
	
	public ListBuilder(Function<T, String> dispatcher) {
		this(", ", dispatcher);
	}
	
	public String build(Iterable<T> iterable) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(T i : iterable) {
			String dispatched = dispatcher.apply(i);
			if(dispatched == null) continue;
			
			if(first) first = false;
			else builder.append(delimeter);
			
			builder.append(dispatched);
		}
		return new String(builder);
	}
}
