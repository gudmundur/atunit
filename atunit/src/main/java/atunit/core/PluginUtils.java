package atunit.core;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import atunit.Plugins;
import atunit.spi.exception.IncompatiblePluginsException;
import atunit.spi.plugin.AtUnitPlugin;
import atunit.spi.plugin.ContainerPlugin;

public class PluginUtils {

	
	/**
	 * Gets the single ContainerPlugin specified in the Plugins annotation's list.
	 * @return the ContainerPlugin's class, or null if none is specified.
	 * @throws IncompatiblePluginsException if multiple ContainerPlugins are specified in the annotation.
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends ContainerPlugin> getContainerPluginClass(Class<?> testClass) throws IncompatiblePluginsException {
		Set<Class<? extends ContainerPlugin>> containers = Sets.newHashSet();
		
		for ( Class<?> plugin : getPluginClasses(testClass)) {
			if ( ContainerPlugin.class.isAssignableFrom(plugin) ) {
				Class<? extends ContainerPlugin> container = (Class<? extends ContainerPlugin>)plugin;
				containers.add(container);
			}
		}
		
		Class<? extends ContainerPlugin>[] containersArray = new Class[0];
		
		switch(containers.size()) {
			case 0: return null;
			case 1: return Iterables.getOnlyElement(containers);
			default: throw new IncompatiblePluginsException(containers.toArray(containersArray));
		}
	}

	/**
	 * Gets all plugins specified by the Plugins annotation.  Any duplicates are consolidated.
	 */
	public static List<Class<? extends AtUnitPlugin>> getPluginClasses(Class<?> testClass) {
		Plugins anno = testClass.getAnnotation(Plugins.class);
		if ( anno != null ) {
			// whack any duplicates
			Set<Class<? extends AtUnitPlugin>> plugins = Sets.newLinkedHashSet(anno.value()); 
			return Lists.immutableList(plugins);
		} else {
			return Lists.immutableList();
		}
		
	}

	
}
