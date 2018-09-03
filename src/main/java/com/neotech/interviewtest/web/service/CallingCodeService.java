package com.neotech.interviewtest.web.service;

import com.neotech.interviewtest.storage.CountryStorage;
import com.neotech.interviewtest.storage.model.Country;
import com.neotech.interviewtest.web.model.BaseResponse;
import com.neotech.interviewtest.web.model.CallingCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/*
	Usually there would be an interface for this (any) service, but sometimes i really fail to see the purpose.
	Like.. no other foreseeable implementation could be added for this functionality..
 */
@Component
@Slf4j
public class CallingCodeService
{
	private final CountryStorage countryStorage;

	@Autowired
	public CallingCodeService(final CountryStorage countryStorage)
	{
		this.countryStorage = countryStorage;
	}

	public BaseResponse getCountryByCode(final int rawCode)
	{
		final Optional<Country> country = countryStorage.getCountryByCode(rawCode);
		if (country.isPresent())
		{
			final Country c = country.get();
			log.debug("found country [{}] using code [{}]", c.getName(), rawCode);
			return new CallingCodeResponse(c.getName(), c.getTimezone());
		}

		log.debug("no country found for code [{}]", rawCode);
		return new BaseResponse(false, "Calling code did not match any country.");
	}
}
