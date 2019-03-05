package io.e4;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

import io.e4.models.DatabaseListViewModel;

public class Addon {
	@PostConstruct
	public void init(IEclipseContext context) {
		DatabaseListViewModel dlvm = new DatabaseListViewModel();
		ContextInjectionFactory.inject(dlvm, context);
		context.set(DatabaseListViewModel.class, dlvm);
	}

}
