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

import org.junit.Test;
import org.junit.runner.RunWith;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * This example demonstrates AtUnit's Guice integration.
 * 
 * Note the Container annotation which tells AtUnit to use Guice.
 * 
 * Fields are fully injected by Guice, and are themselves injected into your
 * test. Your test does not have to implement Module, but if it does the
 * bindings it configures will also be used.
 * 
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class ExampleGuiceTest implements Module {

	@Inject @Unit InjectedStringHolder holder;
	@Inject @Named("my string") String boundString;

	public void configure(Binder b) {
		b.bind(String.class).annotatedWith(Names.named("my string")).toInstance("configured");
	}

	@Test
	public void tGetString() {
		assertEquals("configured", boundString);
		assertEquals(boundString, holder.getString());
	}

	
	protected static class InjectedStringHolder {
		final String string;

		@Inject
		public InjectedStringHolder(@Named("my string") String stringToHold) {
			this.string = stringToHold;
		}

		public String getString() {
			return string;
		}
	}
}
