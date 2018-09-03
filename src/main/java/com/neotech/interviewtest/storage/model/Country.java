package com.neotech.interviewtest.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Country
{
	private String name;
	private String code;
	private String timezone;
}
