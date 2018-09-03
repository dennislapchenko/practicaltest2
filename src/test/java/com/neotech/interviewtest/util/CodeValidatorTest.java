package com.neotech.interviewtest.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(BlockJUnit4ClassRunner.class)
public class CodeValidatorTest
{
	private final CodeValidator validator = new CodeValidator();

	//testing like this is influenced by golang unit testing, which was my language as of late.
	//never before have i written java unit tests like this,
	//however i am very happy how it looks and performs.
	@Test
	public void testCodeRegex()
	{
		final List<TestPair<String, Boolean>> testCodes = Arrays.asList(
				TestPair.of("123", true),
				TestPair.of("+337", true),
				TestPair.of("1 337", true),
				TestPair.of("+1 337", true),
				TestPair.of("++337", false),
				TestPair.of("1 33  7", false),
				TestPair.of("+1da", false),
				TestPair.of("fsaga", false),
				TestPair.of(" ", false),
				TestPair.of("+gag", false));

		testCodes.forEach(code -> {
			final boolean valid = validator.validate(code.getTestValue());
			assertThat(valid, is(code.getExpected()));
		});
	}
}
