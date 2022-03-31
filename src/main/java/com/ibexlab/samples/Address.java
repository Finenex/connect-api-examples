package com.ibexlab.samples;

import com.ibexlab.samples.utils.JsonRpc;

import java.util.HashMap;
import java.util.Map;

/**
 * 계좌 생성
 */
public class Address {
    public static void main(String[] args) {
        // "Sample Address" 이란 이름의 계좌를 생성함
        createAddress("Sample Address");
    }

    private static void createAddress(String addressInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("addInfo", addressInfo);

        String request = JsonRpc.createRequest("createAddress", params);
        String response = JsonRpc.walletApi(request);
        Map<String, Object> result = JsonRpc.getResultData(response);

        System.out.println("Created address: " + result.get("address"));
        System.out.println("Secret: " + result.get("secretKey"));
        System.out.println("Mnemonic: " + result.get("mnemonic"));
    }
}
