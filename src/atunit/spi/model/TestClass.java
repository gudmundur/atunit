package atunit.spi.model;

import java.lang.reflect.Field;
import java.util.Set;

public interface TestClass {
	Class<?> getTestClass();
	Field getUnitField();
	Set<Field> getFields();
	Set<Field> getMockFields();
	Set<Field> getStubFields();
	
}
