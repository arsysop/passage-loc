package ru.arsysop.passage.lic.registry;

//FIXME: replacement for BaseDescriptorRegistry
public interface DescriptorRegistry<D extends BaseDescriptor> extends BaseDescriptorRegistry<D>{
	
	void registerSource(String source);

	void unregisterSource(String source);
	
	Iterable<String> getSources();

}
