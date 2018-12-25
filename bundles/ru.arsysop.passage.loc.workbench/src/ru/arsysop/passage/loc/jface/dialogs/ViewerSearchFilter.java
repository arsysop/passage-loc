package ru.arsysop.passage.loc.jface.dialogs;

import org.eclipse.jface.viewers.ViewerFilter;

public abstract class ViewerSearchFilter extends ViewerFilter {

	protected String searchText = "";

	public void setFilteringText(String filteringText) {
		this.searchText = filteringText;
	}

}
