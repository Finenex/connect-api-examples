package com.ibexlab.samples;

import com.ibexlab.samples.constants.Address;
import com.ibexlab.samples.constants.Token;
import com.ibexlab.samples.utils.ApiException;
import com.ibexlab.samples.utils.Crypto;
import com.ibexlab.samples.utils.JsonRpc;

import java.util.HashMap;
import java.util.Map;

/**
 * 토큰 이체
 */
public class Transfer {
    private static final int MAX_WAIT_SEC = 10;

    public static void main(String[] args) {
        transfer(
                Token.NLP, // 사용할 토큰
                Address.ADDRESS1, // 송신 계좌 
                Address.SECRET1, // 송신 계좌 비밀키
                Address.ADDRESS2, // 수신 계좌
                "10", // 금액
                "Transfer (NLP)" // 적요
        );

        transfer(
                Token.NURI,
                Address.ADDRESS1,
                Address.SECRET1,
                Address.ADDRESS2,
                "5",
                "Transfer (NURI)"
        );
    }

    private static void transfer(String contract, String from, String secret, String to, String amount, String comment) {
        // 1. 임시 키 구함
        String tempKey = getTempKey(from);

        // 2. 해시 키 구함
        String hashKey = getHashKey(tempKey, secret);

        // 3. 서명 구함
        String message = contract + "|" + from + "|" + to + "|" + amount + "|" + tempKey + "|" + hashKey;
        String signature = getSignature(message, from, tempKey, hashKey);

        // 4. 이체 요청
        String transactionId = requestTransfer(contract, from, to, amount, comment, tempKey, hashKey, signature);

        // 5. 이체 결과를 기다림
        int status = waitForTransferComplete(transactionId);

        switch (status) {
            case -1:
                System.out.println("Transaction pending");
                break;
            case 0:
                System.out.println("Transaction failed");
                break;
            case 1:
                System.out.println("Transaction successful");
                break;
            case 2:
            default:
                System.out.println("Transaction invalid");
                break;
        }
    }

    private static String getTempKey(String from) {
        Map<String, Object> params = new HashMap<>();
        params.put("address", from);
        params.put("keyType", "transfer");

        String request = JsonRpc.createRequest("net_getTempKey", params);
        String response = JsonRpc.tokenApi(request);
        Map<String, Object> result = JsonRpc.getResultData(response);
        return (String) result.get("tempKey");
    }

    private static String getHashKey(String tempKey, String secret) {
        String message = tempKey + "|" + secret;
        return Crypto.sha256(message);
    }

    private static String getSignature(String message, String from, String tempKey, String hashKey) {
        Map<String, Object> params = new HashMap<>();
        params.put("address", from);
        params.put("data", Crypto.sha256(message));
        params.put("tempKey", tempKey);
        params.put("hashKey", hashKey);

        String request = JsonRpc.createRequest("signData", params);
        String response = JsonRpc.walletApi(request);
        Map<String, Object> result = JsonRpc.getResultData(response);
        String signature = (String) result.get("signedData");

        if (signature == null) {
            throw new ApiException(500, "Cannot get signature");
        }

        return signature;
    }

    private static String requestTransfer(String contractAddress, String from, String to, String amount, String comment, String tempKey, String hashKey, String signature) {
        Map<String, Object> params = new HashMap<>();
        params.put("contractAddress", contractAddress);
        params.put("sender", from);
        params.put("toAddress", to);
        params.put("amount", amount);
        params.put("comment", comment);
        params.put("tempKey", tempKey);
        params.put("hashKey", hashKey);
        params.put("signature", signature);

        String request = JsonRpc.createRequest("erc20_transfer", params);
        String response = JsonRpc.tokenApi(request);
        Map<String, Object> result = JsonRpc.getResultData(response);
        return (String) result.get("transactionId");
    }

    private static int waitForTransferComplete(String transactionId) {
        int status = -1;

        for (int i = 0; i < MAX_WAIT_SEC; i++) {
            status = getTransactionStatus(transactionId);

            if (status >= 0) {
                return status;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }

        return status;
    }

    private static int getTransactionStatus(String transactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transactionId);

        String request = JsonRpc.createRequest("net_getTransactionStatus", params);
        String response = JsonRpc.tokenApi(request);
        Map<String, Object> result = JsonRpc.getResultData(response);
        return (int) result.get("status");
    }
}
