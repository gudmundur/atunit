/**
 * Copyright (C) 2007 Logan Johnson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package atunit.example;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Unit;
import atunit.spring.Bean;

/**
 * Demonstrates use of Spring container in conjunction with a mock framework, in this case EasyMock.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 */

@RunWith(AtUnit.class)
@Container(Container.Option.SPRING)
@MockFramework(MockFramework.Option.EASYMOCK)
public class ExampleSpringEasyMockTest {

	/**
	 * Beans are autowired by type, by default.
	 */
	@Bean @Unit SimpleManager manager;
	
	/**
	 * Mocks and Stubs that are also annotated with Bean will be placed into the application context like
	 * any other bean.  If a name is specified, it will be used.  If no name is specified, one will be generated.
	 * Mock or Stub Beans are candidates for autowiring by type.
	 * 
	 * In other words:  Declare a Mock or Stub field, annotate it with Bean, and it's just like you've put it
	 * in the XML.
	 * 
	 */
	@Bean("daoBean") @Mock SimpleDao dao;

	
	@Test
	public void testMocksAndContext() {
		assertNotNull(manager);
		assertNotNull(dao);
		assertSame(dao, manager.getDao());
		
		EasyMock.expect(dao.loadString()).andReturn("loaded string");
		EasyMock.replay(dao);
		
		assertEquals("loaded string", manager.getString());
		
		EasyMock.verify(dao);
	}
	
	public static class SimpleManager {
		private SimpleDao dao;
		public void setDao(SimpleDao dao) {
			this.dao = dao;
		}
		public SimpleDao getDao() {
			return dao;
		}
		public String getString() {
			return dao.loadString();
		}
	}
	
	public static interface SimpleDao {
		public String loadString();
	}
	
}
