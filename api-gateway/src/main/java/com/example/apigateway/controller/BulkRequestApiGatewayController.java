package com.example.apigateway.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이 api-gateway의 목적은 실 작업 플랫폼을 모르는 사용자가 작업을 요청하는 과정을 추상화하는 것입니다.
 *
 * - 대용량의 요청이기에 기초 정보는 사전에 업로드된 s3 path가 전달됩니다.
 * - api-gateway로 들어온 요청을 history 기록 후 실 작업 플랫폼으로 전달됩니다.
 */
@Slf4j
@RestController
public class BulkRequestApiGatewayController {

	private final Gson gson = new GsonBuilder().create();

	/**
	 * 초기 모델로 identifier 외의 requestParam이 없습니다.
	 * request.identifier는 플랫폼에서 약속된 직렬화 방식으로 front에서 전달해줍니다.
	 */
	@PostMapping("/v1")
	public String v1(@RequestBody final V1Request request) {
		// 테스트를 위해 identifier 반환
		return request.getIdentifier();
	}

	/**
	 * identifier와 requestParam 모두 json 그대로 받아 플랫폼으로 by-pass 합니다.
	 */
	@PostMapping("/v2")
	public void v2(@RequestBody final V2Request request) {
		// 테스트를 위해 json 출력
		log.debug("### identifierJson={}", gson.toJson(request.getIdentifierJson()));
		log.debug("### requestParamJson={}", gson.toJson(request.getRequestParamJson()));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class V1Request {

		private JobType jobType;
		private String identifier;
		private String s3Path;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class V2Request {

		private JobType jobType;
		private String identifierJson;
		private String requestParamJson;
		private String s3Path;
	}
}
