package io.e4.parts;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import io.e4.E4Constants;
import io.e4.api.DbConnector;
import io.e4.models.Database;
import io.e4.models.DatabaseListViewModel;
import io.e4.models.MySqlConnection;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class DbSettingsPart {

	private Label lblSave;
	private Combo selectSave;
	
	private Text inputUsername;
	private Text inputPassword;
	
	private MySqlConnection initialSettings;
	
	@Inject
	private EPartService partService;
	@Inject
	private IEclipseContext eclipseContext;

	@PostConstruct
	public void createComposite(Composite parent) {
		MySqlConnection conn = ContextInjectionFactory.make(MySqlConnection.class, eclipseContext);
		try {
			initialSettings = conn.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		
		parent.setLayout(new GridLayout(3, false));
		
		
		Label lblDatabaseHost = new Label(parent, SWT.NONE);
		lblDatabaseHost.setText("Database host");

		Text inputDatabaseHost = new Text(parent, SWT.BORDER);
		inputDatabaseHost.setToolTipText("e.g.: localhost:3306");
		inputDatabaseHost.setMessage(conn.getHost());
		inputDatabaseHost.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button btnSsltls = new Button(parent, SWT.CHECK);
		btnSsltls.setSelection(conn.isSsl());
		btnSsltls.setText("SSL/TLS");
				
		Label lblUsername = new Label(parent, SWT.NONE);
		lblUsername.setText("Username");
		
		inputUsername = new Text(parent, SWT.BORDER);
		inputUsername.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		inputUsername.setMessage(conn.getUsername());
		new Label(parent, SWT.NONE);
		
		Label lblPassword = new Label(parent, SWT.NONE);
		lblPassword.setText("Password");
		
		inputPassword = new Text(parent, SWT.BORDER | SWT.PASSWORD);
		inputPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(conn.getPassword()!=null) {
			inputPassword.setText(conn.getPassword());
		} else {
			inputPassword.setMessage("password is empty");
		}
		new Label(parent, SWT.NONE);
		
		lblSave = new Label(parent, SWT.NONE);
		lblSave.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 2, 1));
		lblSave.setText("Save this configuration?");
		lblSave.setEnabled(false);
		
		selectSave = new Combo(parent, SWT.NONE);
		selectSave.setItems(new String[] {"Yes", "No"});
		selectSave.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		selectSave.select(conn.isSaveOnSuccess()?0:1);
		selectSave.setEnabled(false);
		new Label(parent, SWT.NONE);
		
		
		Button btnConnectToDatabase = new Button(parent, SWT.NONE);
		btnConnectToDatabase.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		btnConnectToDatabase.setText("Connect to database");
		btnConnectToDatabase.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getDatabaseListFromDb(conn, parent.getShell());
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		new Label(parent, SWT.NONE);
		
		inputDatabaseHost.addModifyListener(e -> {
			conn.setHost(inputDatabaseHost.getText());
			compareState(conn, initialSettings);
		});
		
		btnSsltls.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				conn.setSsl(btnSsltls.getSelection());
				compareState(conn, initialSettings);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e);}
		});
		
		inputUsername.addModifyListener(e -> {
			conn.setUsername(inputUsername.getText());
			compareState(conn, initialSettings);
		});
		
		inputPassword.addModifyListener(e -> {
			conn.setPassword(inputPassword.getText());
			compareState(conn, initialSettings);
		});
		
		selectSave.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String saveOnSuccess = selectSave.getItem(selectSave.getSelectionIndex());
				System.out.println("Save on success: " + saveOnSuccess);
				if(saveOnSuccess.equalsIgnoreCase("yes")) {
					conn.setSaveOnSuccess(true);
				} else {
					conn.setSaveOnSuccess(false);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { widgetSelected(e);}
		});
	}

	private void compareState(MySqlConnection conn, MySqlConnection initialSettings) {
		Objects.requireNonNull(conn);
		Objects.requireNonNull(initialSettings);
		boolean isTheSame = conn.equals(initialSettings);
		lblSave.setEnabled(!isTheSame);
		selectSave.setEnabled(!isTheSame);
	}
	
	private void getDatabaseListFromDb(MySqlConnection conn, Shell shell) {
		try {
			Bundle bundle = FrameworkUtil.getBundle(this.getClass());
			BundleContext bundleContext = bundle.getBundleContext();
			String osgiFilter = "(db-type=" + conn.getConnectorType() + ")";
			Collection<ServiceReference<DbConnector>> connectorsList = bundleContext.getServiceReferences(DbConnector.class, osgiFilter);
			Optional<ServiceReference<DbConnector>> opSref = connectorsList.stream().findFirst();
			if(!opSref.isPresent()) {
				MessageDialog.openWarning(shell, "DbConnector isn't found", "DbConnector for " + conn.getConnectorType() + " is not found");
				return;
			}
			DbConnector dbConnector = bundleContext.getService(opSref.get());
			
			try (Connection sqlConnection = dbConnector.getConnection(conn);
					Statement stmt = sqlConnection.createStatement();
					ResultSet resultset = stmt.executeQuery("SHOW DATABASES;")) {
				List<Database> dbList = new ArrayList<>();
				while(resultset.next()) {
					Database db = new Database();
					db.setName( resultset.getString("Database") );
					dbList.add(db);
				}
				
				DatabaseListViewModel dblViewModel = eclipseContext.get(DatabaseListViewModel.class);
				dblViewModel.setItems(dbList);
				
				MPart mpart = partService.findPart(E4Constants.DBLIST_PART_DESCRIPTOR_ID);
				mpart.setVisible(true);
				partService.showPart(mpart, PartState.ACTIVATE);
				if(conn.isSaveOnSuccess()) {
					conn.save();
					initialSettings = conn.clone();
					compareState(conn, initialSettings);
				}
			}
			
		} catch (Exception e) {
			String message;
			if(e.getMessage() == null) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				message = sw.toString();
			} else {
				message = e.getMessage();
			}
			MessageDialog.openError(shell, "Database connection failed", message);
		}
	}
}