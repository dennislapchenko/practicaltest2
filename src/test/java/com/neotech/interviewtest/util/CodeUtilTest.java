package com.neotech.interviewtest.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


@RunWith(BlockJUnit4ClassRunner.class)
public class CodeUtilTest
{
	@Test
	public void testValidCodes()
	{
		final List<TestPair<String, Integer>> testCodes = Arrays.asList(
				TestPair.of("123", 123),
				TestPair.of("+337", 337),
				TestPair.of("++337", 337),
				TestPair.of("1 337", 1337),
				TestPair.of("1 33  7", 1337),
				TestPair.of("+1 337", 1337));

		testCodes.forEach(code -> {
			final int rawCode = CodeUtil.parseToRawCode(code.getTestValue());
			assertThat(rawCode, is(code.getExpected()));
		});
	}

	@Test
	public void testInvalidCodes()
	{
		final List<String> testCodes = Arrays.asList("+1d31", "+ddasf", "dasfas");

		int exceptionCounter = 0;
		for (final String code : testCodes)
		{
			try
			{
				CodeUtil.parseToRawCode(code);
				fail("Should not have reached this fail statement");
			}
			catch (final NumberFormatException nfe)
			{
				exceptionCounter++;
			}
		}

		assertThat(exceptionCounter, is(testCodes.size()));
	}
}
