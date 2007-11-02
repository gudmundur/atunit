/**
 * Copyright (C) 2007 Logan Johnson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package atunit.jmock;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import atunit.Stub;
import atunit.core.IncompatibleAnnotationException;
import atunit.core.Mock;
import atunit.core.MockFramework;

public class JMockFramework implements MockFramework {

	public Map<Field, Object> getValues(Field[] fields) throws Exception {
		final Map<Field,Object> jmockFields = new HashMap<Field,Object>();
		
		Mockery mockery = null;
		final Set<Object> ignored = new HashSet<Object>();
		
		for ( Field field : fields ) {
			if ( Mockery.class.isAssignableFrom(field.getType())) {
				if ( field.getAnnotation(Mock.class) != null )
					throw new IncompatibleAnnotationException(Mock.class, field.getType());
				
				if ( Mockery.class.equals(field.getType())) {
					mockery = new JUnit4Mockery();
				} else {
					mockery = (Mockery)field.getType().newInstance();
				}
				jmockFields.put(field, mockery);
				break;
			}
		}
		
		for ( Field field : fields ) {
			boolean isMock = (field.getAnnotation(Mock.class) != null);
			boolean isStub = (field.getAnnotation(Stub.class) != null);
			if ( !isMock && !isStub ) continue;
			if ( isMock && (mockery == null) ) throw new NoMockeryException();
			
			Class<?> fieldType = field.getType();
			if ( fieldType.isArray() ) {
				Object[] array = (Object[])Array.newInstance(fieldType.getComponentType(), 3);
				for ( int i = 0; i < array.length; i++ ) {
					array[i] = mockery.mock(fieldType.getComponentType());
					if ( isStub ) {
						ignored.add(array[i]);
					}
				}
				jmockFields.put(field, array);
				
			} else {
				Object mock = mockery.mock(field.getType());
				if ( isStub ) {
					ignored.add(mock);
				}
				
				jmockFields.put(field, mock);
			}

		}
		
		if ( !ignored.isEmpty() ) {
			Expectations expectations = new Expectations() {{
				for ( Object mock : ignored ) {
					ignoring(mock);
				}
			}};
			mockery.checking(expectations);
		}
		
		return jmockFields;
	}
	
	@SuppressWarnings("serial")
	public static class NoMockeryException extends Exception {
	}
	@SuppressWarnings("serial")
	public static class TooManyMockeriesException extends Exception {
		public TooManyMockeriesException(String msg) {
			super(msg);
		}
	}

}
