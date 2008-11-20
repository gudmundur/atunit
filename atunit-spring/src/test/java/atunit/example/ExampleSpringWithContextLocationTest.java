package atunit.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import atunit.example.subjects.User;
import atunit.spring.Bean;
import atunit.spring.Context;
import atunit.spring.SpringContainer;

/**
 * This example is exactly the same as ExampleSpringTest, except that it
 * specifies the location of a context XML file to use.
 * 
 */
@RunWith(AtUnit.class)
@Container(SpringContainer.class)
/*
 * The @Context annotation tells AtUnit to use the named context XML file,
 * instead of looking for a file named after the test class.
 */
@Context("ExampleSpringTest.xml")
public class ExampleSpringWithContextLocationTest {

	@Bean
	@Unit
	User user;
	@Bean("username")
	String username;

	@Test
	public void testGetId() {
		assertEquals(500, user.getId().intValue());
	}

	@Test
	public void testGetUsername() {
		assertEquals("fred", user.getUsername());
	}
}
