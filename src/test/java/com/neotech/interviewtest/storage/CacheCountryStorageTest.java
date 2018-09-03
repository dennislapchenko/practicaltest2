package com.neotech.interviewtest.storage;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.neotech.interviewtest.storage.model.Country;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CacheCountryStorageTest
{
	private final int port = 8080;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(port);

	private final String wiremockPath = "/wiki/callingcodes";
	private final String wiremockServeFileProper = "callingcodes.html";
	private final String wiremockServeFileEmptyTables = "callingcodes_emptytable.html";

	private final String url = String.format("http://localhost:%s%s", port, wiremockPath);

	@Test
	public void parseWikiCallingCodes_success() throws IOException
	{
		stubResponse(getResourceAsString(wiremockServeFileProper));
		new CacheCountryStorage(url);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void parseWikiCallingCodes_badUrl() throws IOException
	{
		stubResponse("<html>html without tables</html>");
		new CacheCountryStorage(url);
	}

	@Test(expected = RuntimeException.class)
	public void parseWikiCallingCodes_emptyTable() throws IOException //empty table or failed to parse rows
	{
		stubResponse(getResourceAsString(wiremockServeFileEmptyTables));
		new CacheCountryStorage(url);
	}

	@Test
	public void parseCodes_andGet_success() throws IOException
	{
		stubResponse(getResourceAsString(wiremockServeFileProper));
		final CountryStorage cache = new CacheCountryStorage(url);

		final Optional<Country> country = cache.getCountryByCode(371);
		assertTrue(country.isPresent());
		assertThat(country.get().getName(), is("Latvia"));
		assertThat(country.get().getCode(), is("+371"));
	}

	@Test
	public void parseCodes_andGet_failure() throws IOException
	{
		stubResponse(getResourceAsString(wiremockServeFileProper));
		final CountryStorage cache = new CacheCountryStorage(url);

		final Optional<Country> country = cache.getCountryByCode(1337);
		assertTrue(!country.isPresent());
	}

	private String getResourceAsString(final String filePath) throws IOException
	{
		final ClassLoader classLoader = getClass().getClassLoader();
		return Files.toString(new File(classLoader.getResource(filePath).getFile()), Charsets.UTF_8);
	}

	private void stubResponse(final String response)
	{
		stubFor(get(wiremockPath).willReturn(aResponse().withStatus(200).withBody(response)));
	}
}
