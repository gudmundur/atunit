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

package atunit;

import java.lang.annotation.*;

/**
 * Fields with this annotation are populated with mock objects created using the configured framework.
 * 
 * A framework is chosen via the {@link MockFramework} or {@link MockFrameworkClass} annotation on the test class.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 *
 * @see MockFramework
 * @see MockFrameworkClass
 * @see <a href="example/ExampleJMockTest.java.xhtml">ExampleJMockTest.java</a>
 * @see <a href="example/ExampleJMockTest.java.xhtml">ExampleEasyMockTest.java</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Mock {
}
