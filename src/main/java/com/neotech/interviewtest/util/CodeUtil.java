package com.neotech.interviewtest.util;

public class CodeUtil
{
	/*
		Remove all '+' characters and all whitespaces from a string and parse into int
	 */
	public static int parseToRawCode(final String code) throws NumberFormatException
	{
		return Integer.parseInt(code.replaceAll("(\\+|\\s)", "")); //remove first '+' or whitespaces
	}
}
