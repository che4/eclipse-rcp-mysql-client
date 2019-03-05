package io.e4.api;

import java.sql.Connection;

public interface DbConnector {
	/**
	 * 
	 * @return type of connector, e.g.: mysql
	 */
	String getType();
	Connection getConnection(DbConnectionSettings settings) throws Exception;
}
