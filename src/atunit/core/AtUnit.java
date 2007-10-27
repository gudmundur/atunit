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

package atunit.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;

public class AtUnit extends JUnit4ClassRunner {

	public AtUnit(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object createTest() throws Exception {
		Class<?> c = getTestClass().getJavaClass();
		final Map<Field,Object> fieldValues = new HashMap<Field,Object>();
		
		// make sure we have one (and only one) @Unit field
		Field unitField = getUnitField(c);
		if ( hasAnnotation(unitField, Mock.class) ) {
			throw new IncompatibleAnnotationException();
		}
		
		// look for a Mockery, and make sure it's not annotated @Unit or @Mock
		Field mockeryField = getMockeryField(c);
		Mockery mockery = null;
		if ( mockeryField != null ) {
			if ( hasAnnotation(mockeryField, Unit.class) || 
				 hasAnnotation(mockeryField, Mock.class) ) {
				throw new IncompatibleAnnotationException();
			}
			mockery = (Mockery)mockeryField.getType().newInstance();
			fieldValues.put(mockeryField,mockery);
		}
		
		final Set<Object> ignored = new HashSet<Object>();
		
		
		
		for ( Field field : getMockFields(c)) {
			if ( mockery == null ) throw new NoMockeryException();
			for ( Annotation anno : field.getAnnotations() ) {
				if ( Mock.class.isAssignableFrom(anno.annotationType())) {
					Class fieldType = field.getType();
					if ( fieldType.isArray() ) {
						Object[] array = (Object[])Array.newInstance(fieldType.getComponentType(), 3);
						for ( int i = 0; i < array.length; i++ ) {
							Object mock = mockery.mock(fieldType.getComponentType());
							array[i] = mock;
							if ( ((Mock)anno).ignored() ) {
								ignored.add(mock);
							}
						}
						fieldValues.put(field, array);
					} else {
						Object mock = mockery.mock(fieldType);
						fieldValues.put(field, mock);
						if ( ((Mock)anno).ignored() ) {
							ignored.add(mock);
						}
					}
					break;
				}
			}
		}
		
		Expectations expectations;
		if ( !ignored.isEmpty() ) {
			expectations = new Expectations() {{ 
				for ( Object mock : ignored ) {
					ignoring(mock);
				}
			}};
		} else {
			expectations = new Expectations() {};
		}
		
		return createTest(c, fieldValues, mockery, expectations);
	}
	
	protected Object createTest(Class<?> testClass, Map<Field,Object> fieldValues, Mockery mockery, Expectations expectations) throws InstantiationException, IllegalAccessException {
		if ( mockery != null ) 
			mockery.checking(expectations);
		Object testInstance = testClass.newInstance();
		for ( Field field : fieldValues.keySet() ) {
			field.set(testInstance, fieldValues.get(field));
		}
		return testInstance;
	}
	
	protected Set<Field> getMockFields(Class<?> c) throws IncompatibleAnnotationException {
		Set<Field> fields = new HashSet<Field>();
		
		for ( Field field : c.getDeclaredFields() ) {
			for ( Annotation anno : field.getAnnotations() ) {
				if ( Mock.class.isAssignableFrom(anno.annotationType())) {
					if ( ! field.getType().isInterface() ) throw new IncompatibleAnnotationException();
					fields.add(field);
				}
			}
		}
		
		return fields;
	}
	
	protected boolean hasAnnotation(Field field, Class<? extends Annotation> annotation) {
		for ( Annotation anno : field.getAnnotations() ) {
			if ( annotation.isAssignableFrom(anno.annotationType()) ) {
				return true;
			}
		}
		return false;
	}
	
	protected Field getUnitField(Class<?> testClass) throws NoUnitException, TooManyUnitsException {
		Field unitField = null;
		for ( Field field : testClass.getDeclaredFields() ) {
			for ( Annotation anno : field.getAnnotations() ) {
				if ( Unit.class.isAssignableFrom(anno.annotationType())) {
					if ( unitField != null ) throw new TooManyUnitsException("Already had field " + unitField + " when I found field " + field);
					unitField = field;
				}
			}
		}
		if ( unitField == null ) throw new NoUnitException();
		return unitField;
	}
	
	protected Field getMockeryField(Class<?> testClass) throws NoMockeryException, TooManyMockeriesException {
		Field mockeryField = null;
		for ( Field field : testClass.getDeclaredFields() ) {
			Class<?> fieldType = field.getType();
			if ( Mockery.class.isAssignableFrom(fieldType)) {
				if ( mockeryField != null ) {
					throw new TooManyMockeriesException("Already had field " + mockeryField + " when I found field " + field);
				}
				mockeryField = field;
			}
 		}
		return mockeryField;
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
	
	@SuppressWarnings("serial")
	public static class NoUnitException extends Exception {
	}
	
	@SuppressWarnings("serial")
	public static class TooManyUnitsException extends Exception {
		public TooManyUnitsException(String msg) {
			super(msg);
		}
	}
	
	@SuppressWarnings("serial")
	public static class IncompatibleAnnotationException extends Exception {
	}

}
