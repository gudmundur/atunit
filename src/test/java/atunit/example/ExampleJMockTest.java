package atunit.example;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import atunit.example.subjects.*;

/**
 * This example demonstrates AtUnit's JMock integration.
 */
@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.JMOCK) // use JMock for mock objects
public class ExampleJMockTest {

	@Unit UserManagerImpl manager;
	User emptyUser;

	/*
	 * You must declare exactly one field of type (or assignable from type) Mockery.
	 */
	Mockery mockery;

	/*
	 * Fields annotated with @Mock are mocked and injected.  An exception will be thrown
	 * if the field type is not an interface.
	 * 
	 * A @Stub is just a @Mock which is ignored by JMock.
	 */
	@Mock UserDao dao;
	@Stub Logger log;

	
	@Before
	public void setUp() {
		manager = new UserManagerImpl(dao, log);
		emptyUser = new User();
	}
	
	@Test
	public void testGetUser() {
		mockery.checking(new Expectations() {{ 
			one (dao).load(with(equal(500)));
				will(returnValue(emptyUser));
		}});
		assertSame(emptyUser, manager.getUser(500));
	}
	

}
