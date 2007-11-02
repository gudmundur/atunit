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

import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.MockFramework;
import atunit.Unit;
import atunit.core.Mock;

/**
 * This example shows AtUnit's JMock integration, with no container.
 * 
 * Note the MockFramework annotation which tells AtUnit to use JMock.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 */
@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.JMOCK)
public class ExampleJMockTest {
	
	/**
	 * You must declare exactly one field of type (or assignable from type) {@link Mockery}.
	 */
	Mockery mockery;

	/**
	 * Fields annotated with {@link Mock} are dynamically mocked and injected.
	 * Just declare and you're done. An exception will be thrown if the field
	 * type is not an interface.
	 */
	@Mock ExampleInterface myMock;
	
	/**
	 * As always, exactly one field must be declared the unit under test.  This field cannot be a mock.
	 */
	@Unit ExampleClass unit;
	
	@Before
	public void setUp() {
		unit = new ExampleClass(myMock);
	}
	
	@Test
	public void exampleTest() {
		
		mockery.checking(new Expectations() {{
			one(myMock).isAwesome();
				will(returnValue(true));
		}});
		
		assertTrue(unit.isAtUnitAwesome());
	}
	
	
	public static interface ExampleInterface {
		boolean isAwesome();
	}
	
	public static class ExampleClass {
		
		private ExampleInterface ei;

		public ExampleClass(ExampleInterface ei) {
			this.ei = ei;
		}
		
		public boolean isAtUnitAwesome() {
			return ei.isAwesome();
		}
	}
	

}
