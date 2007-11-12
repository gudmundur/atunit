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

package atunit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;

import atunit.core.Container;
import atunit.core.IncompatibleAnnotationException;
import atunit.core.MockFramework;
import atunit.core.NoContainer;
import atunit.core.NoMockFramework;
import atunit.easymock.EasyMockFramework;
import atunit.guice.GuiceContainer;
import atunit.jmock.JMockFramework;
import atunit.spring.SpringContainer;

/**
 * This is the JUnit test runner used for AtUnit tests. It delegates to the
 * configured {@link atunit.core.MockFramework} for mock and stub object
 * creation and to the configured {@link atunit.core.Container} for dependency
 * injection.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 * @see <a href="example/ExampleAtUnitTest.java.xhtml"/>ExampleAtUnitTest.java</a>
 */
public class AtUnit extends JUnit4ClassRunner {

	public AtUnit(Class<?> testClass) throws InitializationError {
		super(testClass);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object createTest() throws Exception {
		Class<?> c = getTestClass().getJavaClass();
		
		Container container = getContainerFor(c);
		MockFramework mockFramework = getMockFrameworkFor(c);
				
		// make sure we have one (and only one) @Unit field
		Field unitField = getUnitField(c);
		if ( unitField.getAnnotation(Mock.class) != null ) {
			throw new IncompatibleAnnotationException(Unit.class, Mock.class);
		}
		
		final Map<Field,Object> fieldValues = mockFramework.getValues(c.getDeclaredFields());
		if ( fieldValues.containsKey(unitField)) {
			throw new IncompatibleAnnotationException(Unit.class, unitField.getType());
		}
		
		Object test = container.createTest(c, fieldValues);

		// any field values created by AtUnit but not injected by the container are injected here.
		for ( Field field : fieldValues.keySet() ) {
			field.setAccessible(true);
			if ( field.get(test) == null ) {
				field.set(test, fieldValues.get(field));
			}
		}
		
		return test;
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
	
	
	protected Container getContainerFor(Class<?> testClass) throws Exception {
		Class<? extends Container> containerClass = NoContainer.class;
		
		atunit.Container containerAnno = testClass.getAnnotation(atunit.Container.class);
		atunit.ContainerClass containerClassAnno = testClass.getAnnotation(atunit.ContainerClass.class);
		
		if ( containerAnno != null && containerClassAnno != null )
			throw new IncompatibleAnnotationException(atunit.Container.class, atunit.ContainerClass.class);

		if ( containerAnno != null ) {
			switch ( containerAnno.value() ) {
				case GUICE: containerClass = GuiceContainer.class; break;
				case SPRING: containerClass = SpringContainer.class; break;
			}
		}
		
		if ( containerClassAnno != null ) {
			containerClass = containerClassAnno.value();
		}
		
		return containerClass.newInstance();
	}
	
	protected MockFramework getMockFrameworkFor(Class<?> testClass) throws Exception {
		Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;
		
		atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(atunit.MockFramework.class);
		atunit.MockFrameworkClass mockFrameworkClassAnno = testClass.getAnnotation(atunit.MockFrameworkClass.class);
		
		if ( mockFrameworkAnno != null && mockFrameworkClassAnno != null )
			throw new IncompatibleAnnotationException(atunit.MockFramework.class, atunit.MockFrameworkClass.class);

		if ( mockFrameworkAnno != null ) {
			switch ( mockFrameworkAnno.value() ) {
				case EASYMOCK: mockFrameworkClass = EasyMockFramework.class; break;
				case JMOCK: mockFrameworkClass = JMockFramework.class; break;
			}
		}
		
		if ( mockFrameworkClassAnno != null ) {
			mockFrameworkClass = mockFrameworkClassAnno.value();
		}
		
		return mockFrameworkClass.newInstance();
	}

}
