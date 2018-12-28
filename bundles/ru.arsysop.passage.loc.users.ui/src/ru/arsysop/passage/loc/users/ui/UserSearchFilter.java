package ru.arsysop.passage.loc.users.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;

import ru.arsysop.passage.lic.model.api.User;
import ru.arsysop.passage.loc.jface.dialogs.ViewerSearchFilter;

public class UserSearchFilter extends ViewerSearchFilter<User> {

	public UserSearchFilter() {
		super(User.class);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchText.isEmpty()) {
			return true;
		}
		if (element instanceof User) {
			String name = getNotNullValue(((User) element).getFullName());
			String identifier = getNotNullValue(((User) element).getEmail());
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
