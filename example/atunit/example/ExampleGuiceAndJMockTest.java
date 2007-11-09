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

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;



import atunit.AtUnit;
import atunit.Container;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Stub;
import atunit.Unit;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;


/**
 * This example shows the full power of AtUnit. Mock objects are supplied by
 * JMock, just as in {@link ExampleJMockTest}. Dependency injection is provided
 * by Guice, just as in ExampleGuiceTest.
 * 
 * Any fields created by the MockFramework and not injected by the Container
 * will be injected by AtUnit itself, so there's usually no need to use Guice
 * annotations on your Mockery, mocks, or stubs.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
@MockFramework(MockFramework.Option.JMOCK)
public class ExampleGuiceAndJMockTest implements Module {
	
	@Inject @Unit ExampleClass myUnit;
	
	Mockery mockery;
	@Mock ExampleInterface myMock;
	@Stub IgnoredInterface ignored;
	
	@Inject @Named("field") String setting;
	

	public void configure(Binder b) {
		b.bind(String.class).annotatedWith(Names.named("field")).toInstance("hooray");
		b.bind(String.class).annotatedWith(Names.named("non-field")).toInstance("yippee");
	}
	
	
	@Test
	public void tInjection() {

		// verify that the injected Mockery works
		mockery.checking(new Expectations() {{ 
			one (myMock).isAwesome();
				will(returnValue(true));
		}});
		assertTrue(myMock.isAwesome());
		
		// verify that bindings from test class module are set
		assertEquals("hooray", setting);
		
		// verify that injection happens between fields
		assertSame(myMock, myUnit.getFace());
		
		// verify that injection happens between fields and non-field bindings
		assertEquals("yippee", myUnit.getString());
		
		// verify that annotation-driven expectations (currently just 'ignored=true') are respected
		ignored.failIfNotIgnored();
	}
	
	
	public static interface IgnoredInterface {
		public void failIfNotIgnored();
	}
	
	public static class ExampleClass {
		
		final ExampleInterface face;
		final String string;
		
		@Inject
		public ExampleClass(ExampleInterface face, @Named("non-field") String string) {
			this.face = face;
			this.string = string;
		}
		public ExampleInterface getFace() {
			return face;
		}
		public String getString() {
			return string;
		}
	}
	
	public static interface ExampleInterface {
		boolean isAwesome();
	}
}
