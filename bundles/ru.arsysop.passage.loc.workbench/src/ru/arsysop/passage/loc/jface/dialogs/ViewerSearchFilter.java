package ru.arsysop.passage.loc.jface.dialogs;

import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
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
		//first, check the label. probably, we do not need subclasses
		if (viewer instanceof ContentViewer) {
			ContentViewer contentViewer = (ContentViewer) viewer;
			IBaseLabelProvider baseLabelProvider = contentViewer.getLabelProvider();
			if (baseLabelProvider instanceof ILabelProvider) {
				ILabelProvider labelProvider = (ILabelProvider) baseLabelProvider;
				String label = labelProvider.getText(element);
				if (getSearchPattern().matcher(label).matches()) {
					return true;
				}
			}
		}
		if (elementClass.isInstance(element)) {
			T casted = elementClass.cast(element);
//			return selectElement(viewer, parentElement, casted, searchText);
		}
		return false;
	}
	
	
//FIXME: implement this method for inherited types
//	protected abstract boolean selectElement(Viewer viewer, Object parentElement, T element, String searchText);
}
