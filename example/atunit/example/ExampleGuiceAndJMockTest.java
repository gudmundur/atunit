
package atunit.example;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import atunit.example.subjects.*;

import com.google.inject.Inject;


/**
 * This example demonstrates the combined integration of Guice and JMock. The
 * combined integration of a mock framework and a dependency injection container
 * is where AtUnit really shines, because it allows you to simply declare some
 * fields and start testing.
 * 
 * See ExampleGuiceTest and ExampleJMockTest for introductions to AtUnit's Guice
 * and JMock support.
 * 
 * Please note also that any Container and MockFramework can be used together.
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
@MockFramework(MockFramework.Option.JMOCK)
public class ExampleGuiceAndJMockTest {

	@Inject @Unit GuiceUserManager manager;
	@Inject User emptyUser;
	
	/*
	 * Just as it does when no Container is used, AtUnit creates the Mockery and
	 * uses it to create any @Mock or @Stub fields. However, once they're
	 * created, AtUnit binds them into the Guice injector so that Guice can
	 * inject them anywhere they're needed.
	 * 
	 * Since AtUnit creates these fields, you have two choices: You can mark
	 * them with @Inject, in which case they will be directly injected by Guice
	 * like any other field; or you can forego @Inject and AtUnit will set them
	 * after Guice is finished. Since you very rarely need the full power of
	 * Guice when actually setting these kinds of fields, you can usually do
	 * without @Inject on them.
	 * 
	 */
	Mockery mockery;
	@Mock UserDao dao;
	@Stub Logger ignoredLogger;

	
	@Test
	public void testGetUser() {

		mockery.checking(new Expectations() {{ 
			one (dao).load(with(equal(500)));
				will(returnValue(emptyUser));
		}});
		
		assertSame(emptyUser, manager.getUser(500));
	}
	
}
