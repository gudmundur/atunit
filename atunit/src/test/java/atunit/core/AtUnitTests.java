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

package atunit.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.MockFramework;
import atunit.NoUnitException;
import atunit.TooManyUnitsException;
import atunit.Unit;

public class AtUnitTests {

	JUnitCore junit;

	@Before
	public void setUp() {
		junit = new JUnitCore();
	}

	@Test
	public void tHappyTest() {
		Result result = junit.run(TestClasses.HappyTest.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}

	@Test
	public void tClassAnnotations() {
		Result result = junit.run(TestClasses.ClassAnnotationsTest.class);
		assertTrue(result.wasSuccessful());
		assertEquals(1, result.getRunCount());
	}

	@Test
	public void tNoUnit() {
		Result result = junit.run(TestClasses.NoUnit.class);
		assertFalse(result.wasSuccessful());
		assertTrue(result.getFailures().get(0).getException() instanceof NoUnitException);
	}

	@Test
	public void tTooManyUnits() {
		Result result = junit.run(TestClasses.TooManyUnits.class);
		assertFalse(result.wasSuccessful());
		assertTrue(result.getFailures().get(0).getException() instanceof TooManyUnitsException);
	}

	@Test
	public void tInheritedUnit() {
		Result result = junit.run(TestClasses.InheritedUnit.class);
		assertTrue(result.wasSuccessful());
	}

	protected static class TestClasses {

		@RunWith(AtUnit.class)
		public static abstract class AbstractAtUnitTest {
			@Test
			public void tPass() {
				assertTrue(true);
			}
		}

		@RunWith(AtUnit.class)
		@MockFramework(NoMockFramework.class)
		@Container(NoContainer.class)
		public static class ClassAnnotationsTest {
			@Unit
			String unit;

			@Test
			public void tPass() {
			}
		}

		public static class HappyTest extends AbstractAtUnitTest {
			@Unit
			String unit;
		}

		public static class NoUnit extends AbstractAtUnitTest {
		}

		public static class TooManyUnits extends AbstractAtUnitTest {
			@Unit
			String firstUnit;
			@Unit
			String secondUnit;
		}

		public static class InheritedUnit extends HappyTest {

			@Before
			public void setUp() {
				unit = getClass().getName();
			}

			public void tUnitInheritance() {
				assertEquals(getClass().getName(), unit);
			}
		}

	}
}
