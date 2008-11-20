package atunit.example;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Stub;
import atunit.Unit;
import atunit.easymock.EasyMockFramework;
import atunit.example.subjects.Logger;
import atunit.example.subjects.User;
import atunit.example.subjects.UserDao;
import atunit.example.subjects.UserManagerImpl;

/**
 * This example demonstrates AtUnit's EasyMock integration.
 * 
 */
@RunWith(AtUnit.class)
@MockFramework(EasyMockFramework.class)
// tells AtUnit to use EasyMock
public class ExampleEasyMockTest {

	@Unit
	UserManagerImpl manager;
	User fred;

	/*
	 * Any field annotated with @Mock or @Stub will be populated automatically
	 * by AtUnit with a mock object provided by EasyMock.
	 */
	@Mock
	UserDao dao;
	@Stub
	Logger log;

	@Before
	public void setUp() {
		manager = new UserManagerImpl(dao, log);
		fred = new User(1, "Fred");
	}

	@Test
	public void testGetStringForSomeReason() {
		expect(dao.load(1)).andReturn(fred);
		replay(dao);
		assertSame(fred, manager.getUser(1));
		verify(dao);
	}

}
