package ru.arsysop.passage.loc.features.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.View;

import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.model.api.Feature;
import ru.arsysop.passage.loc.jface.dialogs.ViewerSearchFilter;

public class FeatureSearchFilter extends ViewerSearchFilter<Feature> {

	public FeatureSearchFilter() {
		super(Feature.class);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchText.isEmpty()) {
			return true;
		}
		if (element instanceof Feature) {
			Feature feature = (Feature) element;
			String name = feature.getName() == null ? "" : feature.getName();
			String identifier = feature.getIdentifier() == null ? "" : feature.getIdentifier();
			Pattern pattern = getSearchPattern();

			Matcher matcherByName = pattern.matcher(name);
			Matcher matcherById = pattern.matcher(identifier);
			if (matcherByName.matches() || matcherById.matches()) {
				return true;
			}
		}

		return false;
	}

}
