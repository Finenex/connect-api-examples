package com.ibexlab.api.examples.utils;

import java.io.*;
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
        return request(spec, request, null);
    }

    static String request(String spec, String request, String jwt) {
        System.out.println(spec + " >>> " + request);

        try {
            URL url = new URL(spec);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            if (jwt != null) {
                con.setRequestProperty("Authorization", "Bearer " + jwt);
            }

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(request.getBytes());
            out.flush();
            out.close();

            int status = con.getResponseCode();
            InputStream is = status > 299 ? con.getErrorStream() : con.getInputStream();
            StringBuilder content = new StringBuilder();

            if (is != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line;

                while ((line = in.readLine()) != null) {
                    content.append(line);
                }

                in.close();
            }

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
