package com.ibexlab.samples;

import com.ibexlab.samples.constants.Address;
import com.ibexlab.samples.utils.Rest;

import java.util.HashMap;
import java.util.Map;

public class Points {
    private static String authToken;

    public static void main(String[] args) {
        // 1. API를 사용하기 위해서는 인증을 받아야 함. 인증을 받은 후에야 아래 API 들이 호출 가능함.
        authenticate(
                "samples", // 아이디
                "dsta.com", // 도메인
                "1111" // 패스워드
        );
        System.out.println("------------------------------------------------");

        // 2. NLP 포인트 발행
        String rewardId = issuePoints(
                "1", // 금액
                "TID001" // TID
        );
        System.out.println("------------------------------------------------");

        // 3. 적립 상태 확인 (미적립)
        status(
                rewardId // 발행 식별자
        );
        System.out.println("------------------------------------------------");

        // 4. 적립
        accumulate(
                rewardId, // 발행 식별자
                "1", // 금액
                Address.ADDRESS2 // 사용자 계좌
        );
        System.out.println("------------------------------------------------");

        // 5. 적립 상태 확인 (적립)
        status(
                rewardId // 발행 식별자
        );
        System.out.println("------------------------------------------------");

        // 5. 적립 취소
        cancel(
                rewardId, // 발행 식별자
                "1" // 금액
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
        authToken = (String) result.get("token");
    }

    private static String issuePoints(String amount, String tid) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("tid", tid);

        String request = Rest.createRequest(params);
        String response = Rest.pointsApi("/points/nlp/issue", request, authToken);
        Map<String, Object> result = Rest.getResult(response);

        System.out.println("rewardId=" + result.get("rewardId"));
        System.out.println("rewarDate=" + result.get("rewardDate"));
        return (String) result.get("rewardId");
    }

    private static void status(String rewardId) {
        Map<String, Object> params = new HashMap<>();
        params.put("rewardId", rewardId);

        String request = Rest.createRequest(params);
        String response = Rest.pointsApi("/points/nlp/status", request, authToken);
        Map<String, Object> result = Rest.getResult(response);
        System.out.println("isAccumulated? " + result.get("isAccumulated"));

        if ((boolean) result.get("isAccumulated")) {
            System.out.println("userAddress=" + result.get("userAddress"));
        }
    }

    private static void accumulate(String rewardId, String amount, String address) {
        Map<String, Object> params = new HashMap<>();
        params.put("rewardId", rewardId);
        params.put("amount", amount);
        params.put("address", address);

        String request = Rest.createRequest(params);
        String response = Rest.pointsApi("/points/nlp/accumulate", request, authToken);
        Map<String, Object> result = Rest.getResult(response);
        System.out.println("Points " + result.get("tokenAmount") + " is accumulated to " + result.get("userAddress"));
    }

    private static void cancel(String rewardId, String amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("rewardId", rewardId);
        params.put("amount", amount);

        String request = Rest.createRequest(params);
        String response = Rest.pointsApi("/points/nlp/cancel", request, authToken);
        Map<String, Object> result = Rest.getResult(response);
        System.out.println("Points " + result.get("tokenAmount") + " is canceled");
    }
}
