package com.github.sylordis.csvreorganiser.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author sylordis
 *
 */
class TypeConverterTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * Test for {@link TypeConverter} to check if the class cannot be instantiated.
	 *
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Test
	@Tag("Constructor")
	void testConstructor() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
	InvocationTargetException, NoSuchMethodException, SecurityException {
		assertThrows(IllegalAccessException.class, () -> TypeConverter.class.getDeclaredConstructor().newInstance());
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.TypeConverter#to(java.lang.Object, java.lang.Class)}
	 * when asked type is same as provided object.
	 */
	@Test
	void testTo_Same() {
		final String o = "Metamorph";
		final String test = TypeConverter.to(o, String.class);
		assertEquals(o, test);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.TypeConverter#to(java.lang.Object, java.lang.Class)}
	 * when provided object is null.
	 */
	@Test
	@Tag("Null")
	void testTo_NullInput() {
		final String o = null;
		final Integer test = TypeConverter.to(o, Integer.class);
		assertNull(test);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.TypeConverter#to(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testTo_IntToString() {
		final Integer o = 969;
		final String test = TypeConverter.to(o, String.class);
		assertEquals("969", test);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.TypeConverter#to(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testTo_StringToInt() {
		final String o = "5";
		final Integer test = TypeConverter.to(o, Integer.class);
		assertEquals(5, test);
	}

	/**
	 * Test method for
	 * {@link com.github.sylordis.csvreorganiser.utils.TypeConverter#to(java.lang.Object, java.lang.Class)}.
	 */
	@Test
	void testTo_OtherCasesNotImplemented() {
		// Boolean to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(true, String.class));
		// Char to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to('b', Boolean.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to('i', Integer.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to('l', Long.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to('s', String.class));
		// Double to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(1.0, Boolean.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(2.0, Character.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(4.0, Float.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(5.0, Integer.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(6.0, Long.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(7.0, String.class));
		// Float to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(1f, Boolean.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(2f, Character.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(3f, Double.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(5f, Integer.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(6f, Long.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(7f, String.class));
		// Int to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(1, Boolean.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(2, Character.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(3, Double.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(4, Float.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(6, Long.class));
		// Long to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(1L, Boolean.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(2L, Character.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(3L, Double.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(4L, Float.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(5L, Integer.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to(7L, String.class));
		// Str to X
		assertThrows(NotImplementedException.class, () -> TypeConverter.to("true", Boolean.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to("b", Character.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to("3.0", Double.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to("4.", Float.class));
		assertThrows(NotImplementedException.class, () -> TypeConverter.to("6", Long.class));
	}

}
