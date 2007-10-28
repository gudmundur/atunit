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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.core.AtUnit;
import atunit.core.Unit;



/**
 * This example shows AtUnit at its most basic:  No mocks, no container.
 * @author logan
 *
 */
@RunWith(AtUnit.class)
public class ExampleAtUnitTest {

	/**
	 * You must have exactly one field annotated with {@link Unit}. This is the
	 * unit under test.  The annotation really only serves as encouragement
	 * of good testing practice.
	 */
	@Unit String unit;

	@Before
	public void setUp() {
		unit = "set";
	}
	
	@Test
	public void testUnit() {
		assertEquals("set", unit);
	}

}
