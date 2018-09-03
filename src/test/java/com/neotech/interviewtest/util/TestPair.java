package com.neotech.interviewtest.util;

import org.apache.commons.lang3.tuple.Pair;

/*
	For fun decided to write own Pair implementation to make these golang-inspired tests i came up with more readable.
 */
public class TestPair<L, R> extends Pair<L, R>
{
	/**
	 * Test value object
	 */
	private final L testValue;
	/**
	 * Expected result object
	 */
	private final R expected;


	public TestPair(final L testValue, final R expected)
	{
		super();
		this.testValue = testValue;
		this.expected = expected;
	}

	public static <L, R> TestPair<L, R> of(final L testValue, final R expected)
	{
		return new TestPair<>(testValue, expected);
	}

	public L getTestValue()
	{
		return testValue;
	}

	public R getExpected()
	{
		return expected;
	}

	@Override
	public L getLeft()
	{
		return testValue;
	}

	@Override
	public R getRight()
	{
		return expected;
	}

	@Override
	public R setValue(final R value)
	{
		throw new UnsupportedOperationException();
	}
}
