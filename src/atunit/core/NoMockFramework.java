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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NoMockFramework implements MockFramework {

	public Map<Field, Object> getValues(Field[] fields) throws Exception {
		for ( Field field : fields ) {
			if ( field.getAnnotation(Mock.class) != null ) {
				throw new IllegalAnnotationException(Mock.class, "No mock framework specified with @MockFramework or @MockFrameworkClass");
			}
		}

		return new HashMap<Field,Object>();
	}

}
