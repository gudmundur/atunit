package atunit.guice;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import atunit.AtUnit;
import atunit.Container;
import atunit.example.ExampleGuiceTest;

public class GuiceContainerTests {

	JUnitCore junit;
	
	@Before
	public void setUp() {
		junit = new JUnitCore();
		
	}
	
	@Test
	public void tExample() {
		Result result = junit.run(ExampleGuiceTest.class);
		assertTrue(result.wasSuccessful());
		assertEquals(2, result.getRunCount());
	}
	
	
	@Test
	public void tDuplicateFields() throws Exception {
		GuiceContainer container = new GuiceContainer();
		Map<Field, Object> fieldValues = Maps.newHashMap();
		fieldValues.put( DuplicateFields.class.getDeclaredField("field1"), "field 1" );
		fieldValues.put( DuplicateFields.class.getDeclaredField("field2"), "field 2" );
		fieldValues.put( DuplicateFields.class.getDeclaredField("field3"), new Integer(3));
		
		DuplicateFields df = (DuplicateFields)container.createTest(DuplicateFields.class, fieldValues);
		

		// Guice should have had to fill these in all by itself, because
		// GuiceContainer should have ignored the undifferentiated fields
		assertFalse("field 1".equals(df.field1));
		assertFalse("field 2".equals(df.field2));
		
		// this should have our value
		assertEquals(3, df.field3.intValue());
	}
	protected static class DuplicateFields {
		@Inject public String field1;
		@Inject public String field2;
		@Inject public Integer field3;
	}
}
