package atunit.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;

import com.google.common.collect.Sets;

import atunit.Mock;
import atunit.Stub;
import atunit.Unit;
import atunit.easymock.EasyMockFramework;
import atunit.guice.GuiceContainer;
import atunit.jmock.JMockFramework;
import atunit.spring.SpringContainer;

final public class TestClassUtils {

	/**
	 * Gets the old-style MockFramework class for the given test class by checking for a MockFramework or MockFrameworkClass annotation.
	 * 
	 * @throws IncompatibleAnnotationException if both MockFramework and MockFrameworkClass are present.
	 */
	public static Class<? extends MockFramework> getMockFrameworkClass(Class<?> testClass) throws IncompatibleAnnotationException {
		Class<? extends MockFramework> mockFrameworkClass = NoMockFramework.class;
		
		atunit.MockFramework mockFrameworkAnno = testClass.getAnnotation(atunit.MockFramework.class);
		atunit.MockFrameworkClass mockFrameworkClassAnno = testClass.getAnnotation(atunit.MockFrameworkClass.class);
		
		if ( mockFrameworkAnno != null && mockFrameworkClassAnno != null )
			throw new IncompatibleAnnotationException(atunit.MockFramework.class, atunit.MockFrameworkClass.class);

		if ( mockFrameworkAnno != null ) {
			switch ( mockFrameworkAnno.value() ) {
				case EASYMOCK: return EasyMockFramework.class;
				case JMOCK: return JMockFramework.class;
			}
		}
		
		if ( mockFrameworkClassAnno != null ) {
			mockFrameworkClass = mockFrameworkClassAnno.value();
		}
		
		return mockFrameworkClass;
	}
	
	/**
	 * Gets all fields (declared and inherited) on the given class.
	 */
	public static Set<Field> getFields(Class<?> testClass) {
		Set<Field> fields = Sets.newHashSet(testClass.getDeclaredFields());
		Class<?> c = testClass;
		while ( (c = c.getSuperclass()) != null ) {
			for ( Field f : c.getDeclaredFields() ) {
				if ( !Modifier.isStatic(f.getModifiers())
					 && !Modifier.isPrivate(f.getModifiers())
				     ) {
					fields.add(f);
				}
			}
		}
		return Sets.immutableSet(fields);
	}
	
	/**
	 * Gets the one and only field in the set annotated with Unit.
	 * @throws NoUnitException if no field is annotated with Unit.
	 * @throws TooManyUnitsException if more than one field is annotated with Unit.
	 */
	public static Field getUnitField(Set<Field> fields)  throws NoUnitException, TooManyUnitsException {
		Field unitField = null;
		for ( Field field : fields ) {
			for ( Annotation anno : field.getAnnotations() ) {
				if ( Unit.class.isAssignableFrom(anno.annotationType())) {
					if ( unitField != null ) throw new TooManyUnitsException("Already had field " + unitField + " when I found field " + field);
					unitField = field;
				}
			}
		}
		if ( unitField == null ) throw new NoUnitException();
		return unitField;
	}
	
	/**
	 * Gets the old-style Container class for the given test class by looking for a Container or ContainerClass annotation.
	 * 
	 * @throws IncompatibleAnnotationException if both Container and ContainerClass annotations are present.
	 */
	public static Class<? extends Container> getContainerClass(Class<?> testClass) throws IncompatibleAnnotationException {
		
		atunit.Container containerAnno = testClass.getAnnotation(atunit.Container.class);
		atunit.ContainerClass containerClassAnno = testClass.getAnnotation(atunit.ContainerClass.class);
		
		if ( containerAnno != null && containerClassAnno != null )
			throw new IncompatibleAnnotationException(atunit.Container.class, atunit.ContainerClass.class);

		if ( containerAnno != null ) {
			switch ( containerAnno.value() ) {
				case GUICE: return GuiceContainer.class;
				case SPRING: return SpringContainer.class;
			}
		}
		
		if ( containerClassAnno != null ) {
			return containerClassAnno.value();
		}
		
		return NoContainer.class;
	}
	
	
	/**
	 * Gets all fields annotated with Mock.
	 */
	public static Set<Field> getMockFields(Set<Field> fields) {
		Set<Field> mocks = Sets.newHashSet();
		for ( Field field : fields ) {
			if ( field.getAnnotation(Mock.class) != null ) {
				mocks.add(field);
			}
		}
		return Sets.immutableSet(mocks);
	}
	
	/**
	 * Gets all fields annotated with Stub
	 */
	public static Set<Field> getStubFields(Set<Field> fields) {
		Set<Field> stubs = Sets.newHashSet();
		for ( Field field : fields ) {
			if ( field.getAnnotation(Stub.class) != null ) {
				stubs.add(field);
			}
		}
		return Sets.immutableSet(stubs);
	}
	
}
