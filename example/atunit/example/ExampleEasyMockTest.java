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

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.MockFramework;
import atunit.Unit;
import atunit.core.Mock;

/**
 * This example demonstrates AtUnit's EasyMock integration.
 * 
 * Note the MockFramework annotation which tells AtUnit to use EasyMock.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 */
@RunWith(AtUnit.class)
@MockFramework(MockFramework.Option.EASYMOCK)
public class ExampleEasyMockTest {

	@Unit Manager manager;
	@Mock DAO dao;
	
	@Before
	public void setUp() {
		manager = new Manager(dao);
	}
	
	@Test
	public void testGetStringForSomeReason() {
		expect(dao.getString()).andReturn("test string");
		replay(dao);
		
		assertEquals("test string", manager.getAStringForSomeReason());
		
		verify(dao);
	}
	
	
	protected static class Manager {
		
		final DAO dao;
		
		public Manager(DAO dao) {
			this.dao = dao;
		}
		
		public String getAStringForSomeReason() {
			return dao.getString();
		}
	}
	
	protected static interface DAO {
		public String getString();
	}
	
}
