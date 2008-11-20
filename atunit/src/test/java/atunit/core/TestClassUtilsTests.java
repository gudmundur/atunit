package atunit.core;

import static atunit.core.TestClassUtils.getContainerClass;
import static atunit.core.TestClassUtils.getFields;
import static atunit.core.TestClassUtils.getMockFields;
import static atunit.core.TestClassUtils.getMockFrameworkClass;
import static atunit.core.TestClassUtils.getStubFields;
import static atunit.core.TestClassUtils.getUnitField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import atunit.Container;
import atunit.Mock;
import atunit.MockFramework;
import atunit.Stub;
import atunit.Unit;
import atunit.core.TestClassUtilsTests.TestClasses.Child;

import com.google.common.collect.Sets;

public class TestClassUtilsTests {

	@Test
	public void tGetContainerClass() throws Exception {
		assertEquals(NoContainer.class, getContainerClass(TestClasses.Unannotated.class));
		assertEquals(DummyContainer.class, getContainerClass(TestClasses.ClassAnnotations.class));
		// assertEquals(GuiceContainer.class,
		// getContainerClass(TestClasses.OptionAnnotations.class));
	}

	@Test(expected = IncompatibleAnnotationException.class)
	@Ignore
	public void tGetContainerIncompatibleAnnotations() throws Exception {
		getContainerClass(TestClasses.IncompatibleContainerAnnotations.class);
	}

	@Test
	public void tGetMockFrameworkClass() throws Exception {
		assertEquals(NoMockFramework.class, getMockFrameworkClass(TestClasses.Unannotated.class));
		assertEquals(DummyMockFramework.class, getMockFrameworkClass(TestClasses.ClassAnnotations.class));
		// assertEquals(EasyMockFramework.class,
		// getMockFrameworkClass(TestClasses.OptionAnnotations.class));
	}

	@Test(expected = IncompatibleAnnotationException.class)
	@Ignore
	public void tGetMockFrameworkIncompatibleAnnotations() throws Exception {
		getMockFrameworkClass(TestClasses.IncompatibleMockFrameworkAnnotations.class);
	}

	@Test
	public void tGetFields() throws Exception {
		Set<Field> fields = getFields(Child.class);

		Set<Field> expectedFields = Sets.newHashSet();
		expectedFields.add(TestClasses.Parent.class.getDeclaredField("parentField1"));
		expectedFields.add(TestClasses.Parent.class.getDeclaredField("parentField2"));
		expectedFields.addAll(Arrays.asList(TestClasses.Child.class.getDeclaredFields()));

		assertEquals(expectedFields.size(), fields.size());
		assertTrue(fields.containsAll(expectedFields));

	}

	@Test
	public void tGetUnitField() throws Exception {
		Set<Field> fields = Sets.newHashSet(TestClasses.Child.class.getDeclaredFields());
		assertEquals(TestClasses.Child.class.getDeclaredField("unitField"), getUnitField(fields));
	}

	@Test(expected = NoUnitException.class)
	public void tGetUnitFieldNoUnits() throws Exception {
		Set<Field> fields = Sets.newHashSet(TestClasses.Child.class.getDeclaredFields());
		fields.remove(TestClasses.Child.class.getDeclaredField("unitField"));
		getUnitField(fields);
	}

	@Test(expected = TooManyUnitsException.class)
	public void tGetUnitFieldTooManyUnits() throws Exception {
		getUnitField(getFields(TestClasses.TooManyUnits.class));
	}

	@Test
	public void tGetMockFields() throws Exception {
		Set<Field> expectedMocks = Sets.newHashSet();
		expectedMocks.add(TestClasses.TestWithMocks.class.getDeclaredField("mockField1"));
		expectedMocks.add(TestClasses.TestWithMocks.class.getDeclaredField("mockField2"));

		Set<Field> mocks = getMockFields(Sets.newHashSet(TestClasses.TestWithMocks.class.getDeclaredFields()));
		assertEquals(expectedMocks.size(), mocks.size());
		assertTrue(mocks.containsAll(expectedMocks));
	}

	@Test
	public void tGetStubFields() throws Exception {
		Set<Field> expectedStubs = Sets.newHashSet();
		expectedStubs.add(TestClasses.TestWithMocks.class.getDeclaredField("stubField1"));
		expectedStubs.add(TestClasses.TestWithMocks.class.getDeclaredField("stubField2"));

		Set<Field> mocks = getStubFields(Sets.newHashSet(TestClasses.TestWithMocks.class.getDeclaredFields()));
		assertEquals(expectedStubs.size(), mocks.size());
		assertTrue(mocks.containsAll(expectedStubs));
	}

	protected static class DummyContainer implements atunit.core.IContainer {
		public Object createTest(Class<?> testClass, Map<Field, Object> fieldValues) throws Exception {
			return null;
		}
	}

	protected static class DummyMockFramework implements atunit.core.IMockFramework {
		public Map<Field, Object> getValues(Field[] fields) throws Exception {
			return null;
		}
	}

	protected static class TestClasses {

		public static class Unannotated {
		}

		@Container(DummyContainer.class)
		@MockFramework(DummyMockFramework.class)
		public static class ClassAnnotations {
		}

		// @Container(Container.Option.GUICE)
		// @MockFramework(MockFramework.Option.EASYMOCK)
		// public static class OptionAnnotations {
		// }

		@Container(DummyContainer.class)
		public static class IncompatibleContainerAnnotations {
		}

		@MockFramework(DummyMockFramework.class)
		public static class IncompatibleMockFrameworkAnnotations {
		}

		@SuppressWarnings("unused")
		public static class Parent {
			protected String parentField1;
			public Integer parentField2;
			private String parentField3;
		}

		@SuppressWarnings("unused")
		public static class Child extends Parent {
			private String childField1;
			protected Integer childField2;
			@Unit
			private String unitField;
		}

		@SuppressWarnings("unused")
		public static class TooManyUnits {
			@Unit
			private String unit1;
			@Unit
			private String unit2;
			private Integer nonUnit;
		}

		@SuppressWarnings("unused")
		public static class TestWithMocks {
			@Unit
			private String unitField;
			@Mock
			private String mockField1;
			@Mock
			private String mockField2;
			@Stub
			private String stubField1;
			@Stub
			private String stubField2;
			private String someOtherField;
		}

	}

}
