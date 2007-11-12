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

package atunit.jmock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Unit;
import atunit.core.IncompatibleAnnotationException;

public class JMockFrameworkTests {
	
	JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
	}
	
	@Test
	public void tWithMockery() {
		Result result = junit.run(TestClasses.WithMockery.class);
		assertTrue(result.wasSuccessful());
		assertEquals(2, result.getRunCount());
	}
	
	@Test
	public void tMockWithoutMockery() {
		Result result = junit.run(TestClasses.MockWithoutMockery.class);
		assertFalse(result.wasSuccessful());
		assertTrue(result.getFailures().get(0).getException() instanceof NoMockeryException);
	}
	
	@Test
	public void tIncompatibleAnnotations() {
		for ( Class<?> testClass : new Class[] { TestClasses.MockUnit.class, 
				                                 TestClasses.MockeryUnit.class, 
				                                 TestClasses.MockeryMock.class } ) {
			
			Result result = junit.run(testClass);
			assertFalse(result.wasSuccessful());
			assertTrue(result.getFailures().get(0).getException() instanceof IncompatibleAnnotationException);
		}
	}
	
	
	
	protected static class TestClasses {
		
		@RunWith(AtUnit.class)
		public static abstract class AbstractAtUnitTest {
			@Test
			public void tPass() {
				assertTrue(true);
			}
		}
		
		@MockFramework(MockFramework.Option.JMOCK)
		public static abstract class AbstractJMockTest extends AbstractAtUnitTest  {
			
		}
		
		public static class HappyTest extends AbstractJMockTest {
			@Unit String unit;
		}
		
		public static class WithMockery extends AbstractJMockTest {
			Mockery mockery;
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
		
		public static class MockWithoutMockery extends AbstractJMockTest {
			@Unit String unit;
			@Mock ExampleInterface mock;
		}
		
		public static class MockUnit extends AbstractJMockTest {
			@Mock @Unit ExampleInterface mock;
			public MockUnit() {}
		}
		
		public static class MockeryUnit extends AbstractJMockTest {
			@Unit Mockery mockery;
			public MockeryUnit() {}
		}
		
		public static class MockeryMock extends AbstractJMockTest {
			@Mock Mockery mockery;
			@Unit String unit;
			public MockeryMock() {}
		}
		
	}
	
	public static interface ExampleInterface {
		boolean isAwesome();
	}
	

}
