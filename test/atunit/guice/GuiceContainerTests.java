package atunit.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import atunit.example.ExampleGuiceTest;

public class GuiceContainerTests {

JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
		
	}
	
	@Test
	public void tExample() {
		Result result = junit.run(ExampleGuiceTest.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}
}
