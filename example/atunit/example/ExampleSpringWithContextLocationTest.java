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
import atunit.example.ExampleSpringTest.StringHolder;
import atunit.spring.Bean;
import atunit.spring.Context;

/**
 * If you want to manually specify the location of a Spring configuration file to use,
 * you can do so with the Context annotation on your test class.  This test is exactly
 * the same as {@link ExampleSpringTest}, except for the Context annotation.
 * 
 * Normally AtUnit would look for ExampleSpringWithContextLocationTest.xml, but I've told
 * it to use ExampleSpringTest.xml instead.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
@Context("ExampleSpringTest.xml")
public class ExampleSpringWithContextLocationTest {
	
	@Bean @Unit StringHolder holder;
	@Bean("myString") String myString;
	
	@Test
	public void testInjectionFromContext() {
		assertEquals("string loaded from context", myString);
		assertSame(myString, holder.getString());
	}
	
}
