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
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;

import atunit.core.IContainer;
import atunit.core.IncompatibleAnnotationException;
import atunit.core.IMockFramework;
import atunit.core.NoContainer;
import atunit.core.NoMockFramework;

import com.google.common.collect.Sets;

/**
 * This is the JUnit test runner used for AtUnit tests. It delegates to the
 * configured {@link atunit.core.IMockFramework} for mock and stub object
 * creation and to the configured {@link atunit.core.IContainer} for dependency
 * injection.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 * @see <a
 *      href="example/ExampleAtUnitTest.java.xhtml"/>ExampleAtUnitTest.java</a>
 */
public class AtUnit extends JUnit4ClassRunner {

	public AtUnit(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected Object createTest() throws Exception {
		Class<?> c = getTestClass().getJavaClass();
		Set<Field> testFields = getFields(c);

		IContainer container = getContainerFor(c);
		IMockFramework mockFramework = getMockFrameworkFor(c);

		// make sure we have one (and only one) @Unit field
		Field unitField = getUnitField(testFields);
		if (unitField.getAnnotation(Mock.class) != null) {
			throw new IncompatibleAnnotationException(Unit.class, Mock.class);
		}

		final Map<Field, Object> fieldValues = mockFramework.getValues(testFields.toArray(new Field[0]));
		if (fieldValues.containsKey(unitField)) {
			throw new IncompatibleAnnotationException(Unit.class, unitField.getType());
		}

		Object test = container.createTest(c, fieldValues);

		// any field values created by AtUnit but not injected by the container
		// are injected here.
		for (Field field : fieldValues.keySet()) {
			field.setAccessible(true);
			if (field.get(test) == null) {
				field.set(test, fieldValues.get(field));
			}
		}

		return test;
	}

	/**
	 * Gets all declared fields and all inherited fields.
	 */
	protected Set<Field> getFields(Class<?> c) {
		Set<Field> fields = Sets.newHashSet(c.getDeclaredFields());
		while ((c = c.getSuperclass()) != null) {
			for (Field f : c.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers())) {
					fields.add(f);
				}
			}
		}
		return fields;
	}

	protected Field getUnitField(Set<Field> fields) throws NoUnitException, TooManyUnitsException {
		Field unitField = null;
		for (Field field : fields) {
			for (Annotation anno : field.getAnnotations()) {
				if (Unit.class.isAssignableFrom(anno.annotationType())) {
					if (unitField != null)
						throw new TooManyUnitsException("Already had field " + unitField + " when I found field "
								+ field);
					unitField = field;
				}
			}
		}
		if (unitField == null)
			throw new NoUnitException();
		return unitField;
	}

	protected IContainer getContainerFor(Class<?> testClass) throws Exception {
		Class<? extends IContainer> containerClass = NoContainer.class;

		atunit.Container containerAnno = testClass.getAnnotation(atunit.Container.class);

		if (containerAnno != null) {
			containerClass = containerAnno.value();
		}

		return containerClass.newInstance();
	}

	protected IMockFramework getMockFrameworkFor(Class<?> testClass) throws Exception {
		Class<? extends IMockFramework> mockFrameworkClass = NoMockFramework.class;

		atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(atunit.MockFramework.class);

		if (mockFrameworkAnno != null) {
			mockFrameworkClass = mockFrameworkAnno.value();
		}

		return mockFrameworkClass.newInstance();
	}

}
