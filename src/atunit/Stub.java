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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is like {@link Mock}, except that the object provided by
 * AtUnit is a dummy and is ignored by the mock objects framework.
 * <p>
 * The specific meaning of this annotation depends on the selected mock objects
 * framework, but in general it should be used to fulfill dependencies which are
 * irrelevant to the test.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 * @see Mock
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Stub {}
