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

package atunit.guice;

import java.lang.reflect.Field;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.internal.runners.InitializationError;


import atunit.core.AtUnit;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


public class GAtUnit extends AtUnit {

	public GAtUnit(Class<?> testClass) throws InitializationError {
		super(testClass);
	}

	@Override
	protected Object createTest(Class<?> testClass,
			Map<Field, Object> fieldValues, Mockery mockery,
			Expectations expectations) throws InstantiationException,
			IllegalAccessException {
		
		FieldModule fields = new FieldModule(fieldValues);
		
		Injector injector;
		if ( Module.class.isAssignableFrom(testClass)) {
			injector = Guice.createInjector(fields, (Module)testClass.newInstance() );
		} else {
			injector = Guice.createInjector(fields);
		}
		
		for ( Field field : fieldValues.keySet() ) {
			if ( Mockery.class.isAssignableFrom(field.getType())) {
				((Mockery)injector.getInstance(field.getType())).checking(expectations);
			}
		}
		return injector.getInstance(testClass);
	}
	
	protected class FieldModule extends AbstractModule {
		final Map<Field,Object> fields;
		
		public FieldModule(Map<Field,Object> fields) {
			this.fields = fields;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		protected void configure() {
			for ( Field field : fields.keySet() ) {
				Class fieldType = field.getType();
				bind(fieldType).toInstance(fields.get(field));
			}
		}
		
	}

	
}
