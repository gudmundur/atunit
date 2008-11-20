package atunit.mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;

import atunit.Mock;
import atunit.Stub;
import atunit.core.IMockFramework;

/**
 * Integrates Mockito into AtUnit.
 * 
 * @author Gili Tzabari
 */
public class MockitoFramework implements IMockFramework {
	public Map<Field, Object> getValues(Field[] fields) throws Exception {
		Map<Field, Object> result = new HashMap<Field, Object>();
		for (Field field : fields) {
			if (field.getAnnotation(Mock.class) != null || field.getAnnotation(Stub.class) != null)
				result.put(field, Mockito.mock(field.getType()));
		}
		return result;
	}
}