package io.e4.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class DatabaseListViewModel {
	
	private Collection<Database> items;
	
	public Collection<Database> getItems(){
		return items;
	}
	
	public void setItems(Collection<Database> items) {
		Objects.requireNonNull(items);
		this.items = items;
	}
	
	public static Collection<Database> getMockData(){
		Database db1 = new Database();
		db1.setName("db1");
		Database db2 = new Database();
		db2.setName("db2");
		return Arrays.asList(db1, db2);
	}
}
