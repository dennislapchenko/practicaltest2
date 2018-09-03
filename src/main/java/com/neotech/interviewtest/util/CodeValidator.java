package com.neotech.interviewtest.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

//No need for an overly complex or interfaced validator, the validation method is too simple
@Component
public class CodeValidator
{
	//optional '+' and then any number of digits with possible space after a digit.
	private final Pattern CODE_REGEX = Pattern.compile("^(\\+?)([0-9](\\s?))+$");

	public boolean validate(final String code)
	{
		return CODE_REGEX.matcher(code).matches();
	}
}
