package com.ibexlab.samples;

import com.ibexlab.samples.utils.Rest;

import java.util.HashMap;
import java.util.Map;

public class Authentication {
    public static void main(String[] args) {
        authenticate(
                "samples", // 아이디
                "dsta.com", // 도메인
                "1111" // 패스워드
        );
    }

    private static void authenticate(String id, String domain, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("domain", domain);
        params.put("password", password);

        String request = Rest.createRequest(params);
        String response = Rest.authApi(request);
        Map<String, Object> result = Rest.getResult(response);
        System.out.println("token=" + result.get("token"));
    }
}
