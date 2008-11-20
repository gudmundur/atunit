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
 * Thrown when a test has more than one field of type {@link Mockery}.
 * 
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 */
@SuppressWarnings("serial")
public class TooManyMockeriesException extends Exception {
	public TooManyMockeriesException(String msg) {
		super(msg);
	}
}