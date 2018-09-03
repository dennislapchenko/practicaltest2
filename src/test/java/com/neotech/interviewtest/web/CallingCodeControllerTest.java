package com.neotech.interviewtest.web;

import com.neotech.interviewtest.util.CodeValidator;
import com.neotech.interviewtest.util.TestPair;
import com.neotech.interviewtest.web.model.BaseResponse;
import com.neotech.interviewtest.web.model.CallingCodeResponse;
import com.neotech.interviewtest.web.service.CallingCodeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CallingCodeControllerTest
{
	@Mock
	private CallingCodeService service;
	@Mock
	private CodeValidator validator;

	private CallingCodeController controller;

	private final String validMatchingCode = "+371";
	private final int validMatchingRawCode = 371;
	private final String validNotMatchingCode = "+1337";
	private final int validNotMatchingRawCode = 1337;
	private final String invalidCode = "+runescape";

	@Before
	public void setup()
	{
		when(validator.validate(validMatchingCode)).thenReturn(true);
		when(validator.validate(validNotMatchingCode)).thenReturn(true);
		when(validator.validate(invalidCode)).thenReturn(false);

		when(service.getCountryByCode(validMatchingRawCode))
				.thenReturn(new CallingCodeResponse("Latvia", "GMT+2"));
		when(service.getCountryByCode(validNotMatchingRawCode))
				.thenReturn(new BaseResponse(false, "Not found"));

		controller = new CallingCodeController(service, validator);
	}

	@Test
	public void testCodeLocator()
	{
		final List<TestPair<String, BaseResponse>> cases = Arrays.asList(
				TestPair.of(validMatchingCode, new CallingCodeResponse("Latvia", "GMT+2")),
				TestPair.of(validNotMatchingCode, new BaseResponse(false, "Not found")),
				TestPair.of(invalidCode, new BaseResponse(false, "Invalid code format.")));

		cases.forEach(c -> {
			final BaseResponse result = controller.callingCodeLocator(c.getTestValue());
			assertThat(result, is(c.getExpected()));
		});
	}
}
