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

import atunit.core.MockFramework;

/**
 * Specifies a {@link MockFramework} implementation to use as the mock objects framework for the test.
 * <p>
 * This annotation is used to plug in an unsupported or custom mock objects framework. To use
 * a supported framework, use the {@link atunit.MockFramework} annotation instead.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 * @see MockFramework
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface MockFrameworkClass {
	
	Class<? extends MockFramework> value();
	
}
