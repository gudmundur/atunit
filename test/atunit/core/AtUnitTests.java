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

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import atunit.core.Mock;
import atunit.core.AtUnit;
import atunit.core.AtUnitExampleTests.ExampleInterface;




public class AtUnitTests {

	JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
	}

	@Test
	public void tHappyTest() {
		Result result = junit.run(TestClasses.HappyTest.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	@Test
	public void tWithMockery() {
		Result result = junit.run(TestClasses.WithMockery.class);
		assertTrue(result.wasSuccessful());
		assertEquals(2, result.getRunCount());
	}
	
	@Test
	public void tNoUnit() {
		Result result = junit.run(TestClasses.NoUnit.class);
		assertFalse(result.wasSuccessful());
		assertTrue(result.getFailures().get(0).getException() instanceof AtUnit.NoUnitException);
	}
	
	@Test
	public void tTooManyUnits() {
		Result result = junit.run(TestClasses.TooManyUnits.class);
		assertFalse(result.wasSuccessful());
		assertTrue(result.getFailures().get(0).getException() instanceof AtUnit.TooManyUnitsException);
	}
	
	@Test
	public void tMockWithoutMockery() {
		Result result = junit.run(TestClasses.MockWithoutMockery.class);
		assertFalse(result.wasSuccessful());
		assertTrue(result.getFailures().get(0).getException() instanceof AtUnit.NoMockeryException);
	}
	
	@Test
	public void tIncompatibleAnnotations() {
		for ( Class<?> testClass : new Class[] { TestClasses.MockUnit.class, 
				                                 TestClasses.MockeryUnit.class, 
				                                 TestClasses.MockeryMock.class } ) {
			Result result = junit.run(testClass);
			assertFalse(result.wasSuccessful());
			assertTrue(result.getFailures().get(0).getException() instanceof AtUnit.IncompatibleAnnotationException);
		}
	}
	
	
	protected static class TestClasses {
		@RunWith(AtUnit.class)
		public static abstract class AbstractPopQuizTest {
			@Test
			public void tPass() {
				assertTrue(true);
			}
		}
		
		public static class HappyTest extends AbstractPopQuizTest {
			@Unit String unit;
		}
		
		public static class NoUnit extends AbstractPopQuizTest {
		}
		
		public static class TooManyUnits extends AbstractPopQuizTest {
			@Unit String firstUnit;
			@Unit String secondUnit;
		}
		
		public static class WithMockery extends AbstractPopQuizTest {
			JUnit4Mockery mockery;
			@Unit String unit;
			@Mock ExampleInterface myMock;
			
			@Test
			public void tMockery() {
				mockery.checking(new Expectations() {{ 
					one (myMock).isAwesome();
						will(returnValue(true));
				}});
				
				assertTrue(myMock.isAwesome());
			}
		}
		
		public static class MockWithoutMockery extends AbstractPopQuizTest {
			@Unit String unit;
			@Mock ExampleInterface mock;
		}
		
		public static class MockUnit extends AbstractPopQuizTest {
			@Mock @Unit ExampleInterface mock;
			public MockUnit() {}
		}
		
		public static class MockeryUnit extends AbstractPopQuizTest {
			@Unit JUnit4Mockery mockery;
			public MockeryUnit() {}
		}
		
		public static class MockeryMock extends AbstractPopQuizTest {
			@Mock Mockery mockery;
			@Unit String unit;
			public MockeryMock() {}
		}
	}
}
