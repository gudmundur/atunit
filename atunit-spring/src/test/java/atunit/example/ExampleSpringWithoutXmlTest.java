package atunit.example;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Stub;
import atunit.Unit;
import atunit.easymock.EasyMockFramework;
import atunit.example.subjects.Logger;
import atunit.example.subjects.User;
import atunit.example.subjects.UserDao;
import atunit.example.subjects.UserManagerImpl;
import atunit.spring.Bean;
import atunit.spring.SpringContainer;

/**
 * This example shows Spring integration without an XML file.
 * 
 * This test is the same as ExampleSpringEasyMockTest, except that there is no
 * XML file.
 * 
 */
@RunWith(AtUnit.class)
@Container(SpringContainer.class)
@MockFramework(EasyMockFramework.class)
public class ExampleSpringWithoutXmlTest {

	@Bean
	@Unit
	UserManagerImpl manager;
	@Bean("userDao")
	@Mock
	UserDao dao;
	@Bean("log")
	@Stub
	Logger log;
	@Bean("fred")
	User fred;

	@Test
	public void testGetUser() {
		expect(dao.load(1)).andReturn(fred);
		replay(dao);
		assertSame(fred, manager.getUser(1));
		verify(dao);
	}

}
