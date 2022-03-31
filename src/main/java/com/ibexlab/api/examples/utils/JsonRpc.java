package com.ibexlab.api.examples.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonRpc extends RpcBase {
    private static final String URL_TOKEN_API = "http://15.164.18.184:6001/token";
    private static final String URL_WALLET_API = "http://15.164.18.184:6001/wallet";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * JSON RPC 요청을 만들어 줌.
     * <pre>
     * {
     *   "jsonrpc" : "2.0",
     *   "id": "1",
     *   "method": "getBethBalance",
     *   "params": {
     *     "address" : "0x0eef659d2924679b90566befab17c6b6f1523664"
     *   }
     * }
     * </pre>
     *
     * @param method 명령
     * @param params 인자들
     * @return JSON
     */
    public static String createRequest(String method, Map<String, Object> params) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("jsonrpc", "2.0");
            body.put("id", createRequestId());
            body.put("method", method);
            body.put("params", params);
            return mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * JSON RPC 응답 검증 및 응답 결과 추출
     * <pre>
     * {
     *   "jsonrcp" : "2.0",
     *   "id": "1",
     *   "result": {
     *     "resultCode": "200",
     *     "resultMessage": "Success",
     *     "resultData": {
     *       "balance": "0.99999999738177"
     *     }
     *   }
     * }
     * </pre>
     *
     * @param response 응답 JSON
     * @return 결과 Map
     */
    public static Map<String, Object> getResultData(String response) {
        if (response == null) {
            throw new ApiException(500, "No response");
        }

        try {
            JsonNode rsp = mapper.readTree(response);
            JsonNode result = rsp.path("result");
            int code = result.path("resultCode").asInt(500);

            if (code != 200) {
                String message = result.path("resultMessage").asText();
                throw new ApiException(code, message);
            }

            return mapper.convertValue(result.path("resultData"), new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new ApiException(500, "Response parsing error");
        }
    }

    /**
     * 토큰 API 호출
     *
     * @param request 요청 JSON
     * @return 응답 JSON
     */
    public static String tokenApi(String request) {
        return request(URL_TOKEN_API, request);
    }

    /**
     * 서버 Wallet API 호출
     *
     * @param request 요청 JSON
     * @return 응답 JSON
     */
    public static String walletApi(String request) {
        return request(URL_WALLET_API, request);
    }
}
