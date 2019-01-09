package ru.arsysop.passage.loc.edit.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.lic.registry.FeaturesRegistry;
import ru.arsysop.passage.lic.registry.ProductsRegistry;
import ru.arsysop.passage.loc.features.ui.FeaturesUi;
import ru.arsysop.passage.loc.products.ui.ProductsUi;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class DomainRegistryHandlerCreate {

	private static final String REGISTRY_RESOURCE_CREATE = "ru.arsysop.passage.loc.edit.ui.commandparameter.domain.resource.create";
	private static final String REGISTRY_RESOURCE_CREATE_FEATURE = REGISTRY_RESOURCE_CREATE + ".feature";
	private static final String REGISTRY_RESOURCE_CREATE_PRODUCT = REGISTRY_RESOURCE_CREATE + ".product";
	private String domain = "";
	private String perspectiveId = "";
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, MApplication application,
			EPartService partService, IEclipseContext context,
			@Named(REGISTRY_RESOURCE_CREATE) String domainRegistryId) {
		
		if (REGISTRY_RESOURCE_CREATE_FEATURE.equals(domainRegistryId)) {
			domain = FeaturesRegistry.DOMAIN_NAME;
			perspectiveId = FeaturesUi.PERSPECTIVE_MAIN;
		}
		if (REGISTRY_RESOURCE_CREATE_PRODUCT.equals(domainRegistryId)) {
			domain = ProductsRegistry.DOMAIN_NAME;
			perspectiveId = ProductsUi.PERSPECTIVE_MAIN;
		}

		LocWokbench.createDomainResource(context, domain, perspectiveId);

	}
}
