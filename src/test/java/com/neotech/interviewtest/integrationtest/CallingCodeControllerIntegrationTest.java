package com.neotech.interviewtest.integrationtest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.neotech.interviewtest.util.TestPair;
import com.neotech.interviewtest.web.model.BaseResponse;
import com.neotech.interviewtest.web.model.CallingCodeResponse;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("integrationtest")
@WebAppConfiguration
public class CallingCodeControllerIntegrationTest
{
	private static final int port = 8080;
	private static final WireMockServer wiremock = new WireMockServer(port);
	private static final String wiremockPath = "/wiki/callingcodes";
	private static final String wiremockServeFile = "callingcodes.html";

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@BeforeClass
	public static void setupClass() throws Exception
	{
		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		final String body = Files.toString(new File(classLoader.getResource(wiremockServeFile).getFile()), Charsets.UTF_8);
		wiremock.stubFor(WireMock.get(wiremockPath).willReturn(aResponse().withBody(body).withStatus(200)));
		wiremock.start();
	}

	@Before
	public void setup() throws Exception
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testCallingCodes() throws Exception
	{
		final List<TestPair<String, String>> cases = Arrays.asList(
				TestPair.of("+371", json(new CallingCodeResponse("Latvia", "UTC+02:00"))),
				TestPair.of("63", json(new CallingCodeResponse("Philippines", "UTC+08:00"))),
				TestPair.of("+1 767", json(new CallingCodeResponse("Dominica", "UTC-04:00"))),
				TestPair.of("+1d3", json(new BaseResponse(false, "Invalid code format."))),
				TestPair.of("fa@11~213", json(new BaseResponse(false, "Invalid code format."))),
				TestPair.of("1337", json(new BaseResponse(false, "Calling code did not match any country."))));

		for (final TestPair<String, String> c : cases) //oldstyle loop because of exception handling
		{
			this.mockMvc.perform(get("/api/callingcode/" + c.getTestValue()))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(content().json(c.getExpected()));
		}
	}

	private String json(final Object obj) throws JsonProcessingException
	{
		return objectMapper.writeValueAsString(obj);
	}

	@AfterClass
	public static void teardown() throws Exception
	{
		wiremock.stop();
	}
}
