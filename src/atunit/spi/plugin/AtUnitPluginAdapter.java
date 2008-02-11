package atunit.spi.plugin;

import atunit.spi.exception.AtUnitPluginException;
import atunit.spi.exception.InvalidTestException;
import atunit.spi.model.TestClass;
import atunit.spi.model.TestFixtureEvent;
import atunit.spi.model.TestInstanceEvent;

public abstract class AtUnitPluginAdapter implements AtUnitPlugin {
	
	public void validateTestClass(TestClass testClass) throws InvalidTestException, AtUnitPluginException {
	}

	public void handleTestFixtureEvent(TestFixtureEvent event) throws InvalidTestException, AtUnitPluginException {
	}

	public void handleTestInstanceEvent(TestInstanceEvent event) throws InvalidTestException, AtUnitPluginException {
	}
	
}
