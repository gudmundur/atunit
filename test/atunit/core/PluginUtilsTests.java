package atunit.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import atunit.Plugins;
import atunit.Unit;
import atunit.spi.exception.IncompatiblePluginsException;
import atunit.spi.model.TestFixture;
import atunit.spi.plugin.AtUnitPlugin;
import atunit.spi.plugin.AtUnitPluginAdapter;
import atunit.spi.plugin.ContainerPlugin;

public class PluginUtilsTests {

	@Test
	public void tGetPluginClasses() throws Exception {
		List<Class<? extends AtUnitPlugin>> expectedPlugins = Lists.newArrayList();
		expectedPlugins.add(DummyPlugin1.class);
		expectedPlugins.add(DummyPlugin2.class);
		expectedPlugins.add(DummyContainerPlugin.class);
		List<Class<? extends AtUnitPlugin>> plugins = PluginUtils.getPluginClasses(TestClasses.TestWithPlugins.class);
		
		assertEquals(expectedPlugins, plugins);
	}
	
	@Test
	public void tGetContainerPluginClass() throws Exception {
		Class<? extends ContainerPlugin> plugin = PluginUtils.getContainerPluginClass(TestClasses.TestWithPlugins.class);
		assertEquals(DummyContainerPlugin.class, plugin);
		
		assertNull(PluginUtils.getContainerPluginClass(TestClasses.NoContainer.class));
	}
	
	@Test(expected=IncompatiblePluginsException.class)
	public void tGetContainerPluginTooMany() throws Exception {
		PluginUtils.getContainerPluginClass(TestClasses.TooManyContainers.class);
	}
	
	
	protected static class DummyPlugin1 extends AtUnitPluginAdapter {
	}
	protected static class DummyPlugin2 extends AtUnitPluginAdapter {
	}
	
	protected static class DummyContainerPlugin extends AtUnitPluginAdapter implements ContainerPlugin {
		public Object instantiate(TestFixture testClass) throws Exception {
			return null;
		}
	}
	
	protected static class DummyContainerPlugin2 extends DummyContainerPlugin {
	}
	
	protected static class TestClasses {
		
		@Plugins({DummyPlugin1.class,DummyPlugin2.class,DummyContainerPlugin.class})
		public static class TestWithPlugins {
			@SuppressWarnings("unused")
			@Unit private String unitField;
		}

		@Plugins({DummyPlugin1.class,DummyPlugin2.class})
		public static class NoContainer {
		}
		
		@Plugins({DummyPlugin1.class,DummyPlugin2.class,DummyContainerPlugin.class, DummyContainerPlugin2.class})
		public static class TooManyContainers {
		}
		
	}
	
}
