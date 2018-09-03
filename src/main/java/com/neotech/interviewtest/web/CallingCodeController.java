package com.neotech.interviewtest.web;

import com.neotech.interviewtest.util.CodeUtil;
import com.neotech.interviewtest.util.CodeValidator;
import com.neotech.interviewtest.web.model.BaseResponse;
import com.neotech.interviewtest.web.service.CallingCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CallingCodeController
{
	private final CallingCodeService callingCodeService;
	private final CodeValidator codeValidator;

	@Autowired
	public CallingCodeController(final CallingCodeService callingCodeService, final CodeValidator codeValidator)
	{
		this.callingCodeService = callingCodeService;
		this.codeValidator = codeValidator;
	}

	//Could use ControllerAdvice to handle exceptions like BadRequest or NotFound, but opted for not throwing exceptions at all
	@GetMapping("/callingcode/{code}")
	public BaseResponse callingCodeLocator(@PathVariable final String code)
	{
		if (!codeValidator.validate(code))
		{
			return new BaseResponse(false, "Invalid code format.");
		}

		final int rawCode = CodeUtil.parseToRawCode(code);
		return callingCodeService.getCountryByCode(rawCode);
	}
}
