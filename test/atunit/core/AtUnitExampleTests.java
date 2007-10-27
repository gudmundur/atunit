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

import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.core.Mock;
import atunit.core.AtUnit;
import atunit.core.Unit;



/**
 * AtUnit streamlines and guides the writing of JUnit and JMock tests by
 * getting rid of tedious boilerplate code and enforcing good testing habits.
 * This example test demonstrates the use of AtUnit to declare the subject of
 * your test and easily obtain mock objects.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 * 
 */
@RunWith(AtUnit.class)
public class AtUnitExampleTests {

	/**
	 * You must have exactly one field annotated with {@link Unit}. This is the
	 * unit under test.  The annotation really only serves as encouragement
	 * of good testing practice.
	 */
	@Unit String unit;

	/**
	 * If you want to use JMock, you may declare exactly one {@link Mockery}.
	 * Any fields you then annotate with {@link Mock} will be automatically set
	 * with mocks.
	 */
	JUnit4Mockery mockery;

	/**
	 * Fields annotated with {@link Mock} are dynamically mocked and injected;
	 * just declare and you're done. This only works if you've declared a
	 * {@link Mockery}; if you haven't, an exception will be thrown.
	 */
	@Mock ExampleInterface myMock;

	
	@Test
	public void exampleTest() {
		mockery.checking(new Expectations() {
			{
				one(myMock).isAwesome();
				will(returnValue(true));
			}
		});

		assertTrue(myMock.isAwesome());
	}
	
	public static interface ExampleInterface {
		boolean isAwesome();
	}
}
