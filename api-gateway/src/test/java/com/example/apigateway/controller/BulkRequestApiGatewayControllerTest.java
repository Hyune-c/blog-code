package com.example.apigateway.controller;

import static com.example.apigateway.controller.JobType.AAA_ADD_ITEM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.apigateway.controller.BulkRequestApiGatewayController.V1Request;
import com.example.apigateway.controller.BulkRequestApiGatewayController.V2Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@SpringBootTest
class BulkRequestApiGatewayControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void beforeEach() {
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(new BulkRequestApiGatewayController())
				.build();
	}

	@DisplayName("[v1] AAA 플랫폼은 identifier 직렬화 방식이 {bizGroupNo}__{bizCode} 입니다.")
	@ParameterizedTest
	@CsvSource(value = "1000, A2000")
	void v1_AAA(final String bizGroupNo, final String bizCode) throws Exception {
		// given
		final String identifier = bizGroupNo + "__" + bizCode;
		final V1Request request = new V1Request(AAA_ADD_ITEM, identifier, null);

		// when, then
		mockMvc.perform(
				post("/v1")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@DisplayName("[v1] BBB 플랫폼은 identifier 직렬화 방식이 {shopCode} 입니다.")
	@ParameterizedTest
	@CsvSource(value = "1000")
	void v1_BBB(final String shopCode) throws Exception {
		// given
		final String identifier = shopCode + "";
		final V1Request request = new V1Request(AAA_ADD_ITEM, identifier, null);

		// when, then
		mockMvc.perform(
				post("/v1")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@DisplayName("[v2] identifier와 requestParam을 json으로 관리합니다.")
	@ParameterizedTest
	@CsvSource(value = "1000, A2000, Y")
	void v1_AAA(final String bizGroupNo, final String bizCode, final String specialPriceYN) throws Exception {
		// given
		final Gson gson = new GsonBuilder().create();
		final Map<String, String> identifiers = new HashMap<>();
		identifiers.put("bizGroupNo", bizGroupNo);
		identifiers.put("bizCode", bizCode);
		final Map<String, String> requestParams = new HashMap<>();
		requestParams.put("specialPriceYN", specialPriceYN);

		final V2Request request = new V2Request(AAA_ADD_ITEM, gson.toJson(identifiers), gson.toJson(requestParams), null);

		// when, then
		mockMvc.perform(
				post("/v2")
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}
}
