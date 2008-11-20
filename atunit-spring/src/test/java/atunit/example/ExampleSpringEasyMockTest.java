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
 * This example demonstrates the combined integration of Spring and EasyMock.
 * 
 * See ExampleGuiceAndJMockTest for notes on the advantages of combined
 * Container/MockFramework integration.
 * 
 */
@RunWith(AtUnit.class)
@Container(SpringContainer.class)
@MockFramework(EasyMockFramework.class)
public class ExampleSpringEasyMockTest {

	/*
	 * @Bean fields are autowired by type by default.
	 */
	@Bean
	@Unit
	UserManagerImpl manager;
	/*
	 * You can specify a name in the @Bean annotation, and the field will be
	 * wired to the bean of that name.
	 */
	@Bean("fred")
	User fred;

	/*
	 * Mocks and Stubs are automatically placed into the application context
	 * like any other bean, and are candidates for autowiring by type. If you
	 * also annotate them with Bean, you can give them a bean name which will be
	 * used in the context.
	 * 
	 * In other words: Declare a @Mock or @Stub field, and it's just like you've
	 * put it in the XML.
	 */
	@Bean("userDao")
	@Mock
	UserDao dao;
	@Bean("log")
	@Stub
	Logger log;

	@Test
	public void testGetUser() {
		expect(dao.load(1)).andReturn(fred);
		replay(dao);
		assertSame(fred, manager.getUser(1));
		verify(dao);
	}

}
