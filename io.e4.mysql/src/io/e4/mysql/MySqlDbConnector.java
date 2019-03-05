package io.e4.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import io.e4.api.DbConnectionSettings;
import io.e4.api.DbConnector;

public class MySqlDbConnector implements DbConnector {
	
	public void init() {}
	
	public void stop() {}
	
	@Override
	public String getType() {
		return "mysql";
	}
	
	@Override
	public Connection getConnection(DbConnectionSettings settings) throws InstantiationException, IllegalAccessException, SQLException {
		Properties connectionProps = new Properties();
		connectionProps.put("user", settings.getUsername());
		if(settings.getPassword()!=null) {
			connectionProps.put("password", settings.getPassword());
		}
		connectionProps.put("useSSL", String.valueOf(settings.isSsl()) );
		com.mysql.jdbc.Driver.class.newInstance();
		return DriverManager.getConnection("jdbc:" + getType() + "://" + settings.getHost() + "/", connectionProps);
	}

}
