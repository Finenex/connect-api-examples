package com.ibexlab.samples.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class RpcBase {
    /**
     * 요청 id 생성.
     * 클라이언트마다 다르게 구현할 수 있음.
     *
     * @return id
     */
    static String createRequestId() {
        String time = String.valueOf(System.currentTimeMillis());
        return time.substring(time.length() - 5);
    }

    static String request(String spec, String request) {
        System.out.println(spec + " >>> " + request);
        try {
            URL url = new URL(spec);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(request.getBytes());
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(status > 299 ? con.getErrorStream() : con.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                content.append(line);
            }

            in.close();
            con.disconnect();

            if (status > 299) {
                System.out.println(spec + " <<< [ERROR " + status + "]" + content.toString());
                throw new ApiException(status, content.toString());
            }

            System.out.println(spec + "<<< " + content.toString());
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
