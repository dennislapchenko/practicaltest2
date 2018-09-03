package com.neotech.interviewtest.web.service;

import com.neotech.interviewtest.storage.CountryStorage;
import com.neotech.interviewtest.storage.model.Country;
import com.neotech.interviewtest.util.TestPair;
import com.neotech.interviewtest.web.model.BaseResponse;
import com.neotech.interviewtest.web.model.CallingCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CallingCodeServiceTest
{
	@Mock
	private CountryStorage storage;
	private CallingCodeService service;

	@Before
	public void setup()
	{
		when(storage.getCountryByCode(37))
				.thenReturn(Optional.ofNullable(new Country("Zimbabwe", "+37", "GMT+2")));
		when(storage.getCountryByCode(1337))
				.thenReturn(Optional.ofNullable(null));

		service = new CallingCodeService(storage);
	}

	@Test
	public void testGetCountry_success()
	{
		final BaseResponse country = service.getCountryByCode(37);

		assertTrue(country instanceof CallingCodeResponse);
		assertTrue(country.isOk());
		assertTrue(StringUtils.isEmpty(country.getError()));
		final CallingCodeResponse resp = (CallingCodeResponse) country;
		assertThat(resp.getCountry(), is("Zimbabwe"));
		assertThat(resp.getTimezone(), is("GMT+2"));
	}

	@Test
	public void testGetCountry_notPresent()
	{
		final BaseResponse country = service.getCountryByCode(1337);

		assertFalse(country instanceof CallingCodeResponse);
		assertFalse(country.isOk());
		assertTrue(StringUtils.isNotEmpty(country.getError()));
	}

	//for fun's sake both above tests could be rewritten into one pretty little test.
	//(one shortcoming is that error message has to be matched directly and will break the test if real class' message is changed)
	@Test
	public void testGetCountry()
	{
		final List<TestPair<Integer, BaseResponse>> cases = Arrays.asList(
				TestPair.of(37, new CallingCodeResponse("Zimbabwe", "GMT+2")),
				TestPair.of(1337, new BaseResponse(false, "Calling code did not match any country."))
		);

		cases.forEach(c -> {
			final BaseResponse result = service.getCountryByCode(c.getTestValue());
			assertThat(result, is(c.getExpected()));
		});

	}
}
