package com.neotech.interviewtest.storage;

import com.neotech.interviewtest.storage.model.Country;

import java.util.Optional;

public interface CountryStorage
{
	Optional<Country> getCountryByCode(int code);
}
