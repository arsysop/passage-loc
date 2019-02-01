package org.eclipse.passage.loc.jface.dialogs;

import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public abstract class ViewerSearchFilter<T> extends ViewerFilter {
	
	protected final Class<T> elementClass;
	
	protected String searchText = "";
	
	protected ViewerSearchFilter(Class<T> elementClass) {
		this.elementClass = elementClass;
	}

	public void setFilteringText(String filteringText) {
		this.searchText = filteringText;
	}

	protected Pattern getSearchPattern() {
		Pattern pattern = Pattern.compile(".*" + searchText + ".*"); //$NON-NLS-1$ //$NON-NLS-2$
		return pattern;
	}

	protected String getNotNullValue(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchText.isEmpty()) {
			return true;
		}
		T converted = convertElement(viewer, parentElement, element, elementClass);
		if (converted != null) {
			return selectElement(viewer, parentElement, converted, searchText);
		}
		return false;
	}
	
	protected T convertElement(Viewer viewer, Object parentElement, Object element, Class<T> elementClass) {
		if (elementClass.isInstance(element)) {
			return elementClass.cast(element);
		}
		return null;
	}
	
	protected abstract boolean selectElement(Viewer viewer, Object parentElement, T element, String searchText);
}
