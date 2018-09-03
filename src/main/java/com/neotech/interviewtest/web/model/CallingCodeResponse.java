package com.neotech.interviewtest.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CallingCodeResponse extends BaseResponse
{
	private String country;
	private String timezone;

	public CallingCodeResponse(final String country, final String timezone)
	{
		super.ok = true;
		this.country = country;
		this.timezone = timezone;
	}
}
