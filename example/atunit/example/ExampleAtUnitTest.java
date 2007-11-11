package atunit.example;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import atunit.example.subjects.User;

/**
 * This example shows AtUnit at its most basic:  No mocks, no container.
 */
@RunWith(AtUnit.class) // tell JUnit to use AtUnit
public class ExampleAtUnitTest {

	/*
	 * An AtUnit test must have exactly one field annotated with @Unit to
	 * indicate the unit under test. (The @Unit annotation doesn't do anything,
	 * it just serves to encourage good testing practice.)
	 */	
	@Unit User user;

	@Before
	public void setUp() {
		user = new User(123, "testusername");
	}
	
	@Test
	public void testGetId() {
		assertEquals(123, user.getId().intValue());
	}
	
	@Test
	public void testGetUsername() {
		assertEquals("testusername", user.getUsername());
	}

}