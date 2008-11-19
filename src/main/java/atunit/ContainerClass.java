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

import atunit.core.Container;

/**
 * Specifies a {@link Container} implementation to use as the dependency
 * injection container for the test.
 * <p>
 * This annotation is used to plug in an unsupported or custom container. To use
 * a supported container, use the {@link atunit.Container} annotation instead.
 * 
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 * @see Container
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface ContainerClass {
	
	Class<? extends Container> value();
	
}
