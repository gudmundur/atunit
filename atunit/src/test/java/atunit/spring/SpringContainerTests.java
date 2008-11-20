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

package atunit.spring;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.ContainerClass;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Stub;
import atunit.Unit;

public class SpringContainerTests {

	JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
	}
	
	@Test
	public void tNoBeans() {
		Result result = junit.run(TestClasses.NoBeans.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	@Test
	public void tUndefinedBeanFilledIn() {
		Result result = junit.run(TestClasses.UndefinedBeanFilledIn.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tMockIntegration() {
		Result result = junit.run(TestClasses.MockIntegration.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tInheritance() {
		Result result = junit.run(TestClasses.Inheritance.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tNameBindings() {
		Result result = junit.run(TestClasses.MockIntegration.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tXmlContext() {
		Result result = junit.run(TestClasses.DefaultXmlContext.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tMergedContexts() {
		Result result = junit.run(TestClasses.MergedContexts.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tBrokenXmlContext() {
		Result result = junit.run(TestClasses.BrokenXmlContext.class);
		assertEquals(1, result.getRunCount());
		assertFalse(result.wasSuccessful());
		assertNotNull(result.getFailures().get(0).getException());
	}
	
	@Test
	public void tSpecifiedContext() {
		Result result = junit.run(TestClasses.SpecifiedXmlContext.class);
		assertEquals(1, result.getRunCount());
		assertTrue(result.wasSuccessful());
	}
	
	@Test
	public void tSpecifiedBadXmlContext() {
		Result result = junit.run(TestClasses.SpecifiedBadXmlContext.class);
		assertEquals(1, result.getRunCount());
		assertFalse(result.wasSuccessful());
		assertNotNull(result.getFailures().get(0).getException());
	}
	
	
	
	protected static class TestClasses {
		
		@RunWith(AtUnit.class)
		@ContainerClass(SpringContainer.class)
		public static abstract class SpringTest {
			
		}
		
		public static class NoBeans extends SpringTest {
			@Unit String unit;
			@Test
			public void tPass() {
				assertTrue(true);
			}
		}
		
		public static class UndefinedBeanFilledIn extends SpringTest {
			@Bean @Unit String unit;
			@Test
			public void tPass() {
				assertTrue(true);
			}
		}
		
		@MockFramework(MockFramework.Option.EASYMOCK)
		public static class MockIntegration extends SpringTest {
			@Unit String unit;
			@Bean @Stub List<String> stringList;
			@Bean @Mock Set<String> stringSet;
			
			@Test
			public void tPass() {
				assertNotNull(stringList);
				assertNotNull(stringSet);
				EasyMock.expect(stringSet.contains("mystring")).andReturn(true);
				EasyMock.replay(stringSet);
				assertTrue(stringSet.contains("mystring"));
			}
		}
		
		public static class Inheritance extends MockIntegration {
		}
		
		@MockFramework(MockFramework.Option.EASYMOCK)
		public static class BindByName extends SpringTest {
			@Unit String unit;
			@Bean("stringList") @Stub List<String> stringList;
			@Bean("stringList2") @Stub List<String> stringList2;
			
			@Test
			public void tPass() {
				assertNotNull(stringList);
				assertNotNull(stringList2);
				assertNotSame(stringList, stringList2);
			}
		}
		
		public static class DefaultXmlContext extends SpringTest {
			@Unit @Bean String unit;
			
			@Test
			public void tPass() {
				assertEquals("unit from context", unit);
			}
		}
		
		@Context("DefaultXmlContext.xml")
		public static class SpecifiedXmlContext extends SpringTest {
			@Unit @Bean String unit;
			
			@Test
			public void tPass() {
				assertEquals("unit from context", unit);
			}
		}
		
		@Context("ThisContextDoesNotExist")
		public static class SpecifiedBadXmlContext extends SpringTest {
			@Unit String unit;
			
			@Test
			public void tPass() {
				assertTrue(true);
			}
		}
		
		@MockFramework(MockFramework.Option.JMOCK)
		public static class MergedContexts  extends SpringTest {
			@Bean @Stub List<String> list;
			@Bean @Unit StringListHolder holder;
			
			@Test
			public void tMergedContexts() {
				assertNotNull(list);
				assertNotNull(holder);
				assertSame(list, holder.list);
			}
		}
		
		public static class BrokenXmlContext extends SpringTest {
			
			@Bean @Unit String unit;

			@Test
			public void tXmlContext() {
				assertNotNull(unit);
			}
		}
	}
	
	public static class StringListHolder {
		
		protected List<String> list;
		
		public void setList(List<String> list) {
			this.list = list;
		}
	}
}
