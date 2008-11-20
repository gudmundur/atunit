package atunit.spi.plugin;

import atunit.spi.exception.AtUnitPluginException;
import atunit.spi.exception.InvalidTestException;
import atunit.spi.model.TestClass;
import atunit.spi.model.TestFixtureEvent;
import atunit.spi.model.TestInstanceEvent;

public interface AtUnitPlugin {
	void validateTestClass(TestClass testClass) throws InvalidTestException, AtUnitPluginException;
	void handleTestFixtureEvent(TestFixtureEvent event) throws InvalidTestException, AtUnitPluginException;
	void handleTestInstanceEvent(TestInstanceEvent event) throws InvalidTestException, AtUnitPluginException;
}
