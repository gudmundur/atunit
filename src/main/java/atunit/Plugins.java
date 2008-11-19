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

import atunit.spi.plugin.AtUnitPlugin;

/**
 * Specifies a list of {@link AtUnitPlugin} implementations to use.
 * 
 * Note that you specify multiple plugin classes by using an array.  This is uncommon, so here's an example of the syntax:
 * 
 * <code>
 * @Plugins( { @FirstPlugin.class, SecondPlugin.class } )
 * public class MyTest {
 *   // ...
 * }
 * </code>
 *
 * @author Logan Johnson &lt;logan.johnson@gmail.com&gt;
 * 
 * @see AtUnitPlugin
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Plugins {
	
	Class<? extends AtUnitPlugin>[] value();
	
}
