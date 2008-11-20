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

package atunit.jmock;

import org.jmock.Mockery;

/**
 * Thrown when a test has fields marked as mocks, but no field with a type
 * assignable from {@link Mockery}.
 * <p>
 * JMock mock objects are useless without access to the {@code Mockery}
 * (context) from which they were created; presence of the former without the
 * latter implies a mistake.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 */
@SuppressWarnings("serial")
public class NoMockeryException extends Exception {
	public NoMockeryException() {
		super("This test requests mock objects, but not the Mockery from which" +
			  " they were created; declare a field of type Mockery, or perhaps" +
			  " consider using stubs instead of mocks.");
	}
}