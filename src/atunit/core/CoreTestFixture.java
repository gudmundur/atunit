package atunit.core;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import atunit.spi.exception.InvalidTestException;
import atunit.spi.model.TestFixture;
import atunit.spi.plugin.AtUnitPlugin;
import atunit.spi.plugin.ContainerPlugin;

public class CoreTestFixture implements TestFixture {
	
	final private Class<?> testClass;
	final private Map<Field,Object> fields;
	final private Set<Field> mockFields;
	final private Set<Field> stubFields;
	final private Field unitField;
	final private Class<? extends ContainerPlugin> containerClass;
	final private List<Class<? extends AtUnitPlugin>> pluginClasses;
	
	public CoreTestFixture(Class<?> testClass) throws InvalidTestException {
		this.testClass = testClass;
		
		fields = Maps.newHashMap();
		for ( Field field : TestClassUtils.getFields(testClass)) {
			fields.put(field, null);
		}
		mockFields = TestClassUtils.getMockFields(fields.keySet());
		stubFields = TestClassUtils.getStubFields(fields.keySet());
		unitField = TestClassUtils.getUnitField(fields.keySet());
		containerClass = PluginUtils.getContainerPluginClass(testClass);
		pluginClasses = PluginUtils.getPluginClasses(testClass);
	}

	public Object getValue(Field field) {
		return fields.get(field);
	}

	public void setValue(Field field, Object value) {
		fields.put(field, value);
	}

	public Set<Field> getFields() {
		return Sets.immutableSet(fields.keySet());
	}

	public Class<?> getTestClass() {
		return testClass;
	}

	public Set<Field> getMockFields() {
		return mockFields;
	}

	public Set<Field> getStubFields() {
		return stubFields;
	}

	public Field getUnitField() {
		return unitField;
	}
	
	public Class<? extends ContainerPlugin> getContainerPluginClass() {
		return containerClass;
	}
	
	public List<Class<? extends AtUnitPlugin>> getPluginClasses() {
		return pluginClasses;
	}

}
