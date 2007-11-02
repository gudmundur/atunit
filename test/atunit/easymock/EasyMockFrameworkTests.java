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

package atunit.easymock;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Unit;

public class EasyMockFrameworkTests {
	
	JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
	}
	
	@Test
	public void tOptionMocks() {
		Result result = junit.run(TestClasses.OptionMocks.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	@Test
	public void tOptionMockFrameworkClass() {
		Result result = junit.run(TestClasses.OptionMockFrameworkClass.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
	
	protected static class TestClasses {
		
		@RunWith(AtUnit.class)
		@MockFramework(MockFramework.Option.EASYMOCK)
		public static class OptionMocks {
			@Unit protected String unit;
			@Mock protected StringFactory stringFactory;
			
			@Test
			public void tGetString() {
				EasyMock.expect(stringFactory.getString()).andReturn("my string");
				EasyMock.replay(stringFactory);
				
				assertEquals("my string", stringFactory.getString());
				
				EasyMock.verify(stringFactory);
			}
			
			public static interface StringFactory {
				public String getString();
			}
		}

		@RunWith(AtUnit.class)
		@MockFramework(MockFramework.Option.EASYMOCK)
		public static class OptionMockFrameworkClass {
			@Unit protected String unit;
			@Mock protected StringFactory stringFactory;
			
			@Test
			public void tGetString() {
				EasyMock.expect(stringFactory.getString()).andReturn("my string");
				EasyMock.replay(stringFactory);
				
				assertEquals("my string", stringFactory.getString());
				
				EasyMock.verify(stringFactory);
			}
			
			public static interface StringFactory {
				public String getString();
			}
		}
		
	}

}
