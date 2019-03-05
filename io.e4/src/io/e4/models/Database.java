package io.e4.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Database implements PropertyChangeListener{
	
	private String name;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public void setName(String name) {
		propertyChangeSupport.firePropertyChange("name", this.name, this.name = name);
	}
	public String getName() {
		return name;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

}
