 
package ru.arsysop.passage.loc.licenses.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import ru.arsysop.passage.loc.edit.UserDomainRegistry;
import ru.arsysop.passage.loc.workbench.LocWokbench;

public class UserOpenHandler {

	@Execute
	public void execute(Shell shell, UserDomainRegistry registry) {
		String selected = LocWokbench.selectLoadPath(shell);
		if (selected != null) {
			registry.registerSource(selected);
		}
	}
		
}