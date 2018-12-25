package ru.arsysop.passage.loc.products.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.model.api.Product;
import ru.arsysop.passage.loc.jface.dialogs.ViewerSearchFilter;

public class ProductSearchFilter extends ViewerSearchFilter {

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchText.isEmpty()) {
			return true;
		}
		if (element instanceof Product) {
			String name = ((Product) element).getName();
			Pattern pattern = Pattern.compile(".*" + searchText + ".*"); //$NON-NLS-1$ //$NON-NLS-2$
			Matcher matcher = pattern.matcher(name);
			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}
}
