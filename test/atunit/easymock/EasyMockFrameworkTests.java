package atunit.easymock;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import atunit.core.AtUnit;
import atunit.core.AtUnitOptions;
import atunit.core.Mock;
import atunit.core.Unit;
import atunit.core.AtUnitOptions.Mocks;

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
		@AtUnitOptions(mocks=Mocks.EASYMOCK)
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
		@AtUnitOptions(mockFrameworkClass=EasyMockFramework.class)
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
