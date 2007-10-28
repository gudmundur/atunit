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

import atunit.core.AtUnit;
import atunit.core.AtUnitOptions;
import atunit.core.Mock;
import atunit.core.Unit;
import atunit.core.AtUnitOptions.Mocks;

/**
 * This example demonstrates AtUnit's EasyMock integration.
 * 
 * Notice the AtUnitOptions annotation on this class. The parameter 'mocks' sets
 * the mock framework you want AtUnit to use.
 * 
 * @author Logan Johnson <logan.johnson@gmail.com>
 */
@RunWith(AtUnit.class)
@AtUnitOptions(mocks=Mocks.EASYMOCK)
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
