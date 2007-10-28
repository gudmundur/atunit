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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;

import atunit.easymock.EasyMockFramework;
import atunit.guice.GuiceContainer;
import atunit.jmock.JMockFramework;

public class AtUnit extends JUnit4ClassRunner {

	public AtUnit(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object createTest() throws Exception {
		Class<?> c = getTestClass().getJavaClass();
		
		AtUnitOptions options = c.getAnnotation(AtUnitOptions.class);
		MockFramework mockFramework = getMockFramework(options);
		Container container = getContainer(options);
				
		final Map<Field,Object> fieldValues = new HashMap<Field,Object>();
		
		// make sure we have one (and only one) @Unit field
		Field unitField = getUnitField(c);
		if ( unitField.getAnnotation(Mock.class) != null ) {
			throw new IncompatibleAnnotationException(Unit.class, Mock.class);
		}
		
		Map<Field,Object> mfValues = mockFramework.getValues(c.getDeclaredFields());
		if ( mfValues.containsKey(unitField)) {
			throw new IncompatibleAnnotationException(Unit.class, unitField.getType());
		}
		for ( Field field : mfValues.keySet() ) {
			fieldValues.put(field, mfValues.get(field));
		}
		
		return container.createTest(c, fieldValues);
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
	
	protected MockFramework getMockFramework(AtUnitOptions options) throws OptionsException {
		AtUnitOptions.Mocks mocksOption = null;
		Class<? extends MockFramework> mockFrameworkClassOption = null;
		Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;
		if ( options != null ) {
			mocksOption = options.mocks();
			mockFrameworkClassOption = options.mockFrameworkClass();
		}
		
		if ( AtUnitOptions.Mocks.JMOCK.equals(mocksOption) ) {
			mockFrameworkClass = JMockFramework.class;
		} else if ( AtUnitOptions.Mocks.EASYMOCK.equals(mocksOption) ) {
			mockFrameworkClass = EasyMockFramework.class;
		}
		
		if ( (options != null) && (mockFrameworkClassOption != NoMockFramework.class) && (mockFrameworkClassOption != mockFrameworkClass) ) {
			if ( mockFrameworkClass != NoMockFramework.class ) {
				throw new OptionsException("Options 'mocks' and 'mockFrameworkClass' conflict");
			}
			mockFrameworkClass = mockFrameworkClassOption;
		}
		
		try {
			return mockFrameworkClass.newInstance();
		} catch (Exception e) {
			throw new OptionsException("Could not instantiate mock framework class", e);
		}
		
	}
	
	
	
	protected Container getContainer(AtUnitOptions options) throws OptionsException {
		AtUnitOptions.Container containerOption = null;
		Class<? extends Container> containerClassOption = null;
		Class<? extends Container> containerClass = NoContainer.class;
		if ( options != null ) {
			containerOption = options.container();
			containerClassOption = options.containerClass();
		}
		
		if ( containerOption == AtUnitOptions.Container.GUICE ) {
			containerClass = GuiceContainer.class;
		}
		
		if ( (options != null) && (containerClassOption != NoContainer.class) && (containerClassOption != containerClass) ) {
			if ( containerClass != NoContainer.class ) {
				throw new OptionsException("Options 'container' and 'containerClass' conflict");
			}
			containerClass = containerClassOption;
		}
		
		try {
			return containerClass.newInstance();
		} catch (Exception e) {
			throw new OptionsException("Could not instantiate container class", e);
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

}
