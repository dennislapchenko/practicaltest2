package com.neotech.interviewtest.storage;

import com.neotech.interviewtest.storage.model.Country;
import com.neotech.interviewtest.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class CacheCountryStorage implements CountryStorage
{
	private Map<Integer, Country> cachedCodes;
	private final String codesUrl;

	@Autowired
	public CacheCountryStorage(@Value("${app.codesUrl}") final String url) throws IOException
	{
		this.codesUrl = url;
		init();
	}

	/*
		Load html from wiki url, get second table, get its body, parse children.
	 */
	//@PostConstruct
	private void init() throws IOException
	{
		cachedCodes = new HashMap<>();

		final Document doc = Jsoup.connect(codesUrl).get();
		final Element table = doc.select("table").get(1); //if html has no table, index out of bounds is expected behaviour
		final Element tableBody = table.selectFirst("tbody");

		tableBody.children().forEach(row -> {
			//some entries have this [notes 1|2] artifact, some have multiple codes
			final String cleanCallingCode = row.child(1).text().replaceAll("\\[notes (1|2)\\]", "");
			final List<String> multipleCodes = Arrays.asList(cleanCallingCode.split(","));

			multipleCodes.forEach(code -> {
				try
				{
					final int rawCode = CodeUtil.parseToRawCode(code);
					//child 0 - country, child 1 - code, child 2 - timezone
					cachedCodes.put(rawCode, new Country(row.child(0).text(), code, row.child(2).text()));
				}
				catch (final NumberFormatException nfe)
				{ //consume exception
					log.warn("failed to parse country code [{}]", code);
				}
			});
		});

		if (cachedCodes.isEmpty()) //fail fast on startup
		{
			throw new RuntimeException("Country calling code storage is empty."); //ideally would make new exception class
		}
		log.info("Successfully loaded {} country codes", cachedCodes.size());
	}

	@Override
	public Optional<Country> getCountryByCode(final int code)
	{
		return Optional.ofNullable(cachedCodes.get(code));
	}
}
