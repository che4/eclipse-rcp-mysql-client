package io.e4.api;

public interface DbConnectionSettings {
	String getConnectorType();
	String getHost();
	String getUsername();
	String getPassword();
	boolean isSsl();
}
