package atunit.spi.plugin;

import atunit.spi.model.TestFixture;

public interface ContainerPlugin extends AtUnitPlugin {
	Object instantiate(TestFixture testClass) throws Exception;
}
