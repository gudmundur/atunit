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

package atunit.example;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import atunit.spring.Bean;

/**
 * Demonstrates automatic Spring configuration loading and injection of beans
 * from the application context.
 * 
 * By default, AtUnit will look for a configuration file named after the test
 * class plus a ".xml" suffix, parallel to the test class in the classpath. In
 * this example, AtUnit looks in the classpath for
 * atunit/spring/ExampleSpringTest.xml.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
public class ExampleSpringTest {
	
	/**
	 * Any fields annotated with Bean will be injected from the context.  By default, annotated fields are
	 * autowired by type.
	 */
	@Bean @Unit StringHolder holder;
	
	/**
	 * If autowiring by type is not sufficient, you can specify the name of the bean to inject.
	 */
	@Bean("myString") String myString;
	
	@Test
	public void testInjectionFromContext() {
		assertEquals("string loaded from context", myString);
		assertSame(myString, holder.getString());
	}
	
	public static class StringHolder {
		private String string;
		public void setString(String string) {
			this.string = string;
		}
		public String getString() {
			return string;
		}
	}
}
