package atunit.example;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import atunit.example.subjects.*;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Names;

/**
 * This example demonstrates AtUnit's Guice integration.
 * 
 * Fields are fully injected by Guice, and are themselves injected into your
 * test.
 * 
 * An AtUnit test does *not* have to implement Module, but if it does the
 * bindings it configures will also be used.
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE) // use Guice for dependency injection
public class ExampleGuiceTest implements Module {

	@Inject @Unit GuiceUser user;
	
	/*
	 * If your test does implement Module, the module configuration will be
	 * merged with any bindings created by AtUnit.
	 * 
	 * This example is fairly contrived. Typically you'll just want to inject
	 * mock objects into your @Unit object, and AtUnit can do that without
	 * making you explicitly configure the bindings yourself. See
	 * ExampleGuiceAndJMockTest for an example.
	 * 
	 */
	public void configure(Binder b) {
		b.bind(String.class).annotatedWith(Names.named("user.name")).toInstance("fred");
		b.bind(Integer.class).annotatedWith(Names.named("user.id")).toInstance(500);
	}


	@Test
	public void testGetId() {
		assertEquals(500, user.getId().intValue());
	}
	
	@Test
	public void testGetUsername() {
		assertEquals("fred", user.getUsername());
	}

}
