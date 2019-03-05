package io.e4.models;

import io.e4.api.DbConnectionSettings;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.osgi.service.prefs.BackingStoreException;

@SuppressWarnings("restriction")
public class MySqlConnection implements DbConnectionSettings, Cloneable {
	@Inject
	@Preference(value="host")
	private String host;
	@Inject
	@Preference(value="ssl")
	private boolean isSsl;
	@Inject
	@Preference(value="username")
	private String username;
	private String password;
	@Inject
	@Preference(value="saveOnSuccess")
	private boolean isSaveOnSuccess;
	
	@Inject
	@Preference
	private IEclipsePreferences prefs;
	private ISecurePreferences secPrefs;
	
	@PostConstruct
	public void init() {
		ISecurePreferences preferences = SecurePreferencesFactory.getDefault();
		secPrefs = preferences.node("io.e4");
		try {
			this.password = secPrefs.get("password", null);
		} catch (StorageException e) {}
	}
	@Override
	public String getConnectorType() {
		return "mysql";
	}
	@Override
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@Override
	public boolean isSsl() {
		return isSsl;
	}
	public void setSsl(boolean isSsl) {
		this.isSsl = isSsl;
	}
	@Override
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isSaveOnSuccess() {
		return isSaveOnSuccess;
	}
	public void setSaveOnSuccess(boolean isSaveOnSuccess) {
		this.isSaveOnSuccess = isSaveOnSuccess;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + (isSsl ? 1231 : 1237);
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	
	public void save() {
		prefs.put("host", host);
		prefs.putBoolean("ssl", isSsl);
		prefs.put("username", username);
		prefs.putBoolean("saveOnSuccess", isSaveOnSuccess);
		try {
			prefs.flush();
		} catch (BackingStoreException e1) {
			e1.printStackTrace();
		}
		try {
			secPrefs.put("password", password, true);
			secPrefs.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MySqlConnection other = (MySqlConnection) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (isSsl != other.isSsl)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	@Override
	public MySqlConnection clone() throws CloneNotSupportedException {
		return (MySqlConnection) super.clone();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("===== Sql connection settings =====").append(System.lineSeparator())
			.append("Host: ").append(host).append(System.lineSeparator())
			.append("SSL : ").append(isSsl).append(System.lineSeparator())
			.append("Username: ").append(username).append(System.lineSeparator())
			.append("Password: ").append(password).append(System.lineSeparator())
			.append("===== end of Sql connection settings =====");
		
		return sb.toString();
	}
	
}
