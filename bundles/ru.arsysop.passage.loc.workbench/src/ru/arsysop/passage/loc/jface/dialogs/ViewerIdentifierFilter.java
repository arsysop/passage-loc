package ru.arsysop.passage.loc.jface.dialogs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import ru.arsysop.passage.lic.registry.Identified;

public class ViewerIdentifierFilter extends ViewerFilter {

	private String filteringText = "";

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		if (filteringText.isEmpty()) {
			return true;
		}
		if (element instanceof Identified) {
			String identifier = ((Identified) element).getIdentifier();
			Pattern pattern = Pattern.compile(".*" + filteringText + ".*");
			Matcher matcher = pattern.matcher(identifier);
			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	public void setFilteringText(String filteringText) {
		this.filteringText = filteringText;
	}

}
