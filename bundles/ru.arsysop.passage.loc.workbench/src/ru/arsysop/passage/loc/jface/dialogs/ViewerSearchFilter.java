package ru.arsysop.passage.loc.jface.dialogs;

import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ViewerFilter;

public abstract class ViewerSearchFilter extends ViewerFilter {

	protected String searchText = "";

	public void setFilteringText(String filteringText) {
		this.searchText = filteringText;
	}

	protected Pattern getSearchPattern() {
		Pattern pattern = Pattern.compile(".*" + searchText + ".*"); //$NON-NLS-1$ //$NON-NLS-2$
		return pattern;
	}
}
