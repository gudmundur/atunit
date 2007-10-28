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

package atunit.easymock;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;

import atunit.core.Mock;
import atunit.core.MockFramework;

public class EasyMockFramework implements MockFramework {

	public Map<Field, Object> getValues(Field[] fields) throws Exception {
		Map<Field,Object> mocks = new HashMap<Field,Object>();
	
		for ( Field field : fields ) {
			Mock anno = field.getAnnotation(Mock.class);
			if ( anno != null ) {
				if ( anno.ignored() == true ) {
					mocks.put(field, EasyMock.createNiceMock(field.getType()));
				} else {
					mocks.put(field, EasyMock.createStrictMock(field.getType()));
				}
			}
		}
		
		return mocks;
	}

}
