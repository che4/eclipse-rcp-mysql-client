package io.e4.parts;

import javax.annotation.PostConstruct;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import io.e4.models.Database;
import io.e4.models.DatabaseListViewModel;

public class DatabaseListPart {
	
	private TableViewer tableViewer;
	private WritableList<Database> input;
	
	@PostConstruct
	public void init(Composite parent, IEclipseContext eclipseContext) {
		DatabaseListViewModel dlvm = eclipseContext.get(DatabaseListViewModel.class);
		tableViewer = new TableViewer(parent);
		//tableViewer.setContentProvider(org.eclipse.jface.viewers.ArrayContentProvider.getInstance());;
		//tableViewer.setInput(dlvm.getItems().stream().map( db -> db.getName()).collect( java.util.stream.Collectors.toList() ));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		tableViewer.getTable().setLayoutData(gridData);
		input = new WritableList<>(dlvm.getItems(), Database.class);
		
		ViewerSupport.bind(tableViewer, input, BeanProperties.values("name"));
	}

}
