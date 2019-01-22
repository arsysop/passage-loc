package ru.arsysop.passage.lic.emf.edit;

public interface SelectionCommandAdvisor {

	String getSelectionTitle(String classifier);

	Iterable<?> getSelectionInput(String classifier);
}
