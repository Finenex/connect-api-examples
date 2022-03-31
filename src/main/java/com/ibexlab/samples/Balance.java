package com.ibexlab.samples;

import com.ibexlab.samples.constants.Address;
import com.ibexlab.samples.constants.Token;
import com.ibexlab.samples.utils.JsonRpc;

import java.util.HashMap;
import java.util.Map;

/**
 * 계좌의 토큰 잔액 구하기
 */
public class Balance {
    public static void main(String[] args) {
        // ADDRESS1 계좌에 저장된 NLP, NURI 토큰 잔액을 구함
        getERC20Balance(Token.NLP, Address.ADDRESS1);
        getERC20Balance(Token.NURI, Address.ADDRESS1);

        // ADDRESS2 계좌에 저장된 NLP, NURI 토큰 잔액을 구함
        getERC20Balance(Token.NLP, Address.ADDRESS2);
        getERC20Balance(Token.NURI, Address.ADDRESS2);
    }

    private static void getERC20Balance(String contract, String address) {
        Map<String, Object> params = new HashMap<>();
        params.put("contractAddress", contract);
        params.put("address", address);

        String request = JsonRpc.createRequest("erc20_getBalance", params);
        String response = JsonRpc.tokenApi(request);
        Map<String, Object> result = JsonRpc.getResultData(response);

        String tokenName = Token.NLP.equals(contract) ? "NLP" : "NURI";
        System.out.println("Address: " + address);
        System.out.println(tokenName + " balance=" + result.get("balance"));
    }
}
