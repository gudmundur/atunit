package atunit.example;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.*;
import atunit.example.subjects.*;
import atunit.spring.Bean;

/**
 * This example demonstrates the combined integration of Spring and EasyMock.
 * 
 * See ExampleGuiceAndJMockTest for notes on the advantages of combined Container/MockFramework integration. 
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
@MockFramework(MockFramework.Option.EASYMOCK)
public class ExampleSpringEasyMockTest {

	/*
	 * @Bean fields are autowired by type by default. 
	 */
	@Bean @Unit UserManagerImpl manager;
	/*
	 * You can specify a name in the @Bean annotation, and the field will be
	 * wired to the bean of that name.
	 */
	@Bean("fred") User fred;
	
	/*
	 * Mocks and Stubs are automatically placed into the application context
	 * like any other bean, and are candidates for autowiring by type. If you
	 * also annotate them with Bean, you can give them a bean name which will be
	 * used in the context.
	 * 
	 * In other words: Declare a @Mock or @Stub field, and it's just like you've
	 * put it in the XML.
	 */
	@Bean("userDao") @Mock UserDao dao;
	@Bean("log") @Stub Logger log;
	
	@Test
	public void testGetUser() {
		expect(dao.load(1)).andReturn(fred);
		replay(dao);
		assertSame(fred, manager.getUser(1));
		verify(dao);
	}
	
}
