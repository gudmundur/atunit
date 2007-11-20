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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;

import atunit.core.Container;

public class GuiceContainer implements Container {

	public Object createTest(Class<?> testClass, Map<Field, Object> fieldValues) throws Exception {
		
		FieldModule fields = new FieldModule(fieldValues);
		
		Injector injector;
		if ( Module.class.isAssignableFrom(testClass)) {
			injector = Guice.createInjector(fields, (Module)testClass.newInstance() );
		} else {
			injector = Guice.createInjector(fields);
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
			
			// map field values by type
			Multimap<Class, Field> fieldsByType = Multimaps.newHashMultimap();
			for ( Field field : fields.keySet() ) {
				fieldsByType.put(field.getType(), field);
			}
			
			// for any types that don't have duplicates, bind instances.
			for ( Class type : fieldsByType.keySet() ) {
				Collection<Field> fields = fieldsByType.get(type);
				if ( fields.size() == 1 ) {
					Field field = Iterables.getOnlyElement(fields);
					bind(type).toInstance(this.fields.get(field));
				}
			}
		}
	}
	
}
