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

package atunit.spring;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.UrlResource;

import atunit.core.Container;

public class SpringContainer implements Container {

	public Object createTest(Class<?> testClass, Map<Field, Object> fieldValues) throws Exception {
		
		GenericApplicationContext ctx = new GenericApplicationContext();
		
		for ( Field field : fieldValues.keySet() ) {
			
			Bean beanAnno = field.getAnnotation(Bean.class);
			
			AbstractBeanDefinition beandef = defineInstanceHolderFactoryBean(field.getType(), fieldValues.get(field));
			
			if ((beanAnno != null) && !beanAnno.value().equals("")) {
				ctx.registerBeanDefinition(beanAnno.value(), beandef);
			} else {
				BeanDefinitionReaderUtils.registerWithGeneratedName(beandef, ctx);
			}
		}
		
		loadBeanDefinitions(testClass, ctx);
		
		fillInMissingFieldBeans(testClass, ctx);

		
		ctx.refresh();
		
		Object test = testClass.newInstance();
		for ( Field field : testClass.getDeclaredFields() ) {
			field.setAccessible(true);
			Bean beanAnno = field.getAnnotation(Bean.class);
			if ( beanAnno == null ) {
				if ( fieldValues.containsKey(field) ) {
					field.set(test, fieldValues.get(field));
				}
			} else {
				if ( ! beanAnno.value().equals("") ) {
					field.set(test, ctx.getBean(beanAnno.value()));
				} else {
					String[] beanNames = ctx.getBeanNamesForType(field.getType());
					if ( beanNames.length < 1 ) {
						throw new BeanCreationException("There are no beans defined with type " + field.getType());
					}
					if ( beanNames.length > 1 ) {
						throw new BeanCreationException("There are " + beanNames.length + " beans defined with type " + field.getType() 
								                                 + "; consider wiring by name instead");
					}
					field.set(test, ctx.getBean(beanNames[0]));
				}
			}
		}
		
		return test;
	}
	
	protected void loadBeanDefinitions(Class<?> testClass, BeanDefinitionRegistry registry) {
		XmlBeanDefinitionReader xml = new XmlBeanDefinitionReader(registry);
		
		String resourceName = testClass.getSimpleName() + ".xml";
		Context ctxAnno = testClass.getAnnotation(Context.class);
		if ( ctxAnno != null ) {
			resourceName = ctxAnno.value();
		}
		URL xmlUrl = testClass.getResource(resourceName);
		if ( xmlUrl != null ) {
			xml.loadBeanDefinitions(new UrlResource(xmlUrl));
		} else if ( ctxAnno != null) {
			// is this the appropriate exception here?
			throw new ApplicationContextException("Could not find context file named " + resourceName);
		}
	}
	
	protected void fillInMissingFieldBeans(Class<?> testClass, GenericApplicationContext ctx) throws Exception {
		for ( Field field : testClass.getDeclaredFields() ) {
			Bean beanAnno = field.getAnnotation(Bean.class);
			if ( beanAnno == null ) continue;
			String name = beanAnno.value();
			if ( !name.equals("") && !ctx.containsBean(name) ) {
				ctx.registerBeanDefinition(name, defineAutowireBean(field.getType()));
			} else if ( ctx.getBeansOfType(field.getType()).isEmpty() ) {
				BeanDefinitionReaderUtils.registerWithGeneratedName(defineAutowireBean(field.getType()), ctx);
			}
		}
	}
	
	protected AbstractBeanDefinition defineAutowireBean(Class<?> type) throws Exception {
		AbstractBeanDefinition beandef = BeanDefinitionReaderUtils.createBeanDefinition(null, type.getName(), type.getClassLoader());
		beandef.setAutowireCandidate(true);
		beandef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
		return beandef;
	}
	
	protected AbstractBeanDefinition defineInstanceHolderFactoryBean(Class<?> type, Object instance) throws Exception {
		ConstructorArgumentValues args = new ConstructorArgumentValues();
		args.addIndexedArgumentValue(0, type);
		args.addIndexedArgumentValue(1, instance);
		
		AbstractBeanDefinition beandef = BeanDefinitionReaderUtils.createBeanDefinition(null, InstanceHolderFactoryBean.class.getName(), getClass().getClassLoader());
		beandef.setConstructorArgumentValues(args);
		beandef.setAutowireCandidate(true);
		return beandef;
	}
	
	protected static class InstanceHolderFactoryBean implements FactoryBean {

		final Class<?> type;
		final Object instance;
		
		public InstanceHolderFactoryBean(Class<?> type, Object instance) {
			this.type = type;
			this.instance = instance;
		}
		
		public Object getObject() throws Exception {
			return instance;
		}

		public Class<?> getObjectType() {
			return type;
		}

		public boolean isSingleton() {
			return true;
		}
		
	}
}
