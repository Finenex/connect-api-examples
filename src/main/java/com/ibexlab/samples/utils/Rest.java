package com.ibexlab.samples.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Rest extends RpcBase {
    private static final String URL_AUTH_API = "https://test-auth.finenex.net/v2/auth";
    private static final String URL_POINTS_API = "https://test-pg.finenex.net";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * RESP API 용 요청을 만들어 줌.
     * <pre>
     * {
     *   "common": {
     *     "tranNo": "1",
     *     "sourceAddr": "token-api-samples"
     *   },
     *   "content": {
     *     "id": "samples",
     *     "domain": "dsta.com",
     *     "password": "1111"
     *   }
     * }
     * </pre>
     *
     * @param params 인자들
     * @return JSON
     */
    public static String createRequest(Map<String, Object> params) {
        try {
            Map<String, Object> common = new HashMap<>();
            common.put("tranNo", createRequestId());
            common.put("sourceAddr", "token-api-samples");

            Map<String, Object> body = new HashMap<>();
            body.put("common", common);
            body.put("content", params);
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * REST API 응답 검증 및 응답 결과 추출
     * <pre>
     * {
     *   "common": {
     *     "tranNo" : "1",
     *     "sourceAddr": "token-api-samples",
     *     "resCode": "200",
     *     "resMessage": "success"
     *   },
     *   "content": {
     *     "token": "12341431434df3afadf2134ga34"
     *   }
     * }
     * </pre>
     *
     * @param response 응답 JSON
     * @return resCode, resMessage 포함한 결과 Map
     */
    public static Map<String, Object> getResult(String response) {
        if (response == null) {
            throw new ApiException(500, "No response");
        }

        try {
            JsonNode rsp = mapper.readTree(response);
            int resCode = rsp.path("common").path("resCode").asInt(500);
            String resMessage = rsp.path("common").path("resMessage").asText();

            if (resCode != 200) {
                throw new ApiException(resCode, resMessage);
            }

            Map<String, Object> result = mapper.convertValue(rsp.path("content"), new TypeReference<Map<String, Object>>() {
            });
            result.put("resCode", resCode);
            result.put("resMessage", resMessage);
            return result;
        } catch (IOException e) {
            throw new ApiException(500, "Response parsing error");
        }
    }

    /**
     * Authentication API 호출
     *
     * @param request 요청 JSON
     * @return 응답 JSON
     */
    public static String authApi(String request) {
        return request(URL_AUTH_API, request);
    }

    /**
     * Points API 호출
     *
     * @param method  명령
     * @param request 요청 JSON
     * @param jwt     인증 토큰
     * @return 응답 JSON
     */
    public static String pointsApi(String method, String request, String jwt) {
        return request(URL_POINTS_API + method, request, jwt);
    }
}
