package atunit.easymock;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;

import atunit.core.Mock;
import atunit.core.MockFramework;

public class EasyMockFramework implements MockFramework {

	public Map<Field, Object> getValues(Field[] fields) throws Exception {
		Map<Field,Object> mocks = new HashMap<Field,Object>();
	
		for ( Field field : fields ) {
			Mock anno = field.getAnnotation(Mock.class);
			if ( anno != null ) {
				if ( anno.ignored() == true ) {
					mocks.put(field, EasyMock.createNiceMock(field.getType()));
				} else {
					mocks.put(field, EasyMock.createStrictMock(field.getType()));
				}
			}
		}
		
		return mocks;
	}

}
