package io.e4.mysql.ui.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import io.e4.mysql.E4Constants;

public class ShowMySqlSettingsHandler {
	//@Execute
	public void exe(MApplication application, EPartService partService, EModelService modelService) {
		List<MPerspective> perspectives = modelService.findElements(application, E4Constants.MYSQL_PERSPECTIVE_ID, MPerspective.class, null);
		System.out.println("Soft switch to perspective");
		partService.switchPerspective(perspectives.get(0));
	}
	@Execute
	public void execute(MApplication application, EPartService partService, EModelService modelService, MWindow window, 
			@Optional MPerspectiveStack perspectiveStack) {
		java.util.Optional<MPerspective> opPersp = partService.switchPerspective(E4Constants.MYSQL_PERSPECTIVE_ID);
		if(opPersp.isPresent()) return;
		MPerspective myPerspective = null;
		List<MUIElement> snips = application.getSnippets();
		System.out.println("Total snippets: " + snips.size());
		for (MUIElement snip : snips) {
			System.out.println("Element: " + snip.getElementId());
			//myPerspective = (MPerspective) ((MPerspectiveStack) snip).getChildren().get(0);
			//partService.switchPerspective(myPerspective);
			
			if (snip.getElementId().equals(E4Constants.MYSQL_PERSPECTIVE_ID)) {
		        if (snip instanceof MPerspective) {
		            myPerspective = (MPerspective) snip;
		            System.out.println("Perspective found...");
		            
		            MPerspective activePerspective = modelService.getActivePerspective(window);
		            perspectiveStack = (MPerspectiveStack) (MElementContainer<?>) activePerspective.getParent();
		            perspectiveStack.getChildren().add(myPerspective);
		            //modelService.
		            //partService.switchPerspective(myPerspective);
		            //partService.switchPerspective(myPerspective);
		            perspectiveStack.getChildren().add(myPerspective);
		            partService.switchPerspective(myPerspective);
		            break;
		        }
		    }
		}
		System.out.println("Show mysql settings");
		//partService.showPart(E4Constants.SETTINGS_PART_DESCRIPTOR_ID, PartState.ACTIVATE);
		//partService.switchPerspective(E4Constants.MYSQL_PERSPECTIVE_ID);
	}

}
