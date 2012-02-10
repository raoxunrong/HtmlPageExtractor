package catfish.utils;

import org.junit.Test;

import static junit.framework.Assert.*;

public class StringUtilsTest {

	@Test
	public void should_return_true_when_invoke_isEmptyString_and_string_is_null(){
		assertEquals(true, StringUtils.isEmptyString(null));
	}
	
	@Test
	public void should_return_true_when_invoke_isEmptyString_and_string_is_empty(){
		assertEquals(true, StringUtils.isEmptyString(""));
	}
	
	@Test
	public void should_return_true_when_invoke_isEmptyString_and_string_contains_only_blank_char(){
		assertEquals(true, StringUtils.isEmptyString("   "));
	}
	
	@Test
	public void should_return_false_when_invoke_isEmptyString_and_string_is_not_empty(){
		assertEquals(false, StringUtils.isEmptyString("test"));
	}
}
