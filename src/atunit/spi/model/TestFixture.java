package atunit.spi.model;

import java.lang.reflect.Field;

public interface TestFixture extends TestClass {
	Object getValue(Field field);
	void setValue(Field field, Object value);
}
