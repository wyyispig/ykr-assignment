package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApiService {

    @Autowired
    private AppConfig appConfig;

    private static final Gson gson = new Gson();
    private static List<RoleContent> historyList = new ArrayList<>(); // 对话历史存储集合
    private static String totalAnswer = ""; // 大模型的答案汇总
    private static Boolean totalFlag = true; // 控制提示用户是否输入
    private static String newQuestion = "";

    public String askQuestion(String question) throws Exception {
        System.out.println("askQuestion called with question: " + question);
        newQuestion = question;
        String authUrl = getAuthUrl(appConfig.getHostUrl(), appConfig.getApiKey(), appConfig.getApiSecret());
        System.out.println("Generated authUrl: " + authUrl);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        System.out.println("Connecting to URL: " + url);
        Request request = new Request.Builder().url(url).build();

        totalAnswer = "";
        WebSocket webSocket = client.newWebSocket(request, new BigModelNew("0", false));
        // 等待WebSocket连接关闭
        while (!totalFlag) {
            Thread.sleep(200);
        }

        return totalAnswer;
    }

    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        System.out.println("getAuthUrl called");
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());

        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        System.out.println("PreStr: " + preStr);

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        System.out.println("SHA: " + sha);

        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha);
        System.out.println("Authorization: " + authorization);

        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        System.out.println("Auth URL: " + httpUrl.toString());
        return httpUrl.toString();
    }

    private static boolean canAddHistory() {
        int historyLength = 0;
        for (RoleContent temp : historyList) {
            historyLength += temp.content.length();
        }
        if (historyLength > 12000) {
            historyList.subList(0, 5).clear();
            return false;
        } else {
            return true;
        }
    }

    class BigModelNew extends WebSocketListener {
        private String userId;
        private Boolean wsCloseFlag;

        public BigModelNew(String userId, Boolean wsCloseFlag) {
            this.userId = userId;
            this.wsCloseFlag = wsCloseFlag;

            RoleContent roleContent=new RoleContent();
            roleContent.role="assistant";
            roleContent.content="我是您的专属英语作文辅导老师，请发给我您的英语作文，我会为您进行打分、分析与提供详细改进建议";
            historyList.add(roleContent);
        }


        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.print("大模型：");
            MyThread myThread = new MyThread(webSocket);
            myThread.start();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
            if (myJsonParse.header.code != 0) {
                System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
                System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
                webSocket.close(1000, "");
                System.out.println("webSocket已关闭" );
                return;
            }
            List<Text> textList = myJsonParse.payload.choices.text;
            for (Text temp : textList) {
                System.out.print(temp.content);
                totalAnswer += temp.content;
            }
            if (myJsonParse.header.status == 2) {
                System.out.println();
                System.out.println("*************************************************************************************");
                if (canAddHistory()) {
                    RoleContent roleContent = new RoleContent("assistant", totalAnswer);
                    historyList.add(roleContent);
                } else {
                    historyList.remove(0);
                    RoleContent roleContent = new RoleContent("assistant", totalAnswer);
                    historyList.add(roleContent);
                }
                wsCloseFlag = true;
                totalFlag = true;
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("WebSocket onFailure");
            t.printStackTrace();
            try {
                if (response != null) {
                    int code = response.code();
                    System.out.println("onFailure code:" + code);
                    System.out.println("onFailure body:" + response.body().string());
                    if (code != 101) {
                        System.out.println("connection failed");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        class MyThread extends Thread {
            private WebSocket webSocket;

            public MyThread(WebSocket webSocket) {
                this.webSocket = webSocket;
            }

            public void run() {
                try {
                    JSONObject requestJson = new JSONObject();
                    JSONObject header = new JSONObject();
                    header.put("app_id", appConfig.getAppid());
                    header.put("uid", UUID.randomUUID().toString().substring(0, 10));

                    JSONObject parameter = new JSONObject();
                    JSONObject chat = new JSONObject();
                    chat.put("domain", "generalv3.5");
                    chat.put("temperature", 0.5);
                    chat.put("max_tokens", 4096);
                    parameter.put("chat", chat);

                    JSONObject payload = new JSONObject();
                    JSONObject message = new JSONObject();
                    JSONArray text = new JSONArray();

                    if (historyList.size()>0) {
                        for (RoleContent tempRoleContent : historyList) {
                            text.add(JSON.toJSON(tempRoleContent));
                        }
                    }

//                    RoleContent roleContent = new RoleContent("user", newQuestion);

                    RoleContent roleContent=new RoleContent();
                    roleContent.role="user";
                    roleContent.content=newQuestion;
                    text.add(JSON.toJSON(roleContent));
                    historyList.add(roleContent);

                    message.put("text", text);
                    payload.put("message", message);

                    requestJson.put("header", header);
                    requestJson.put("parameter", parameter);
                    requestJson.put("payload", payload);

                    System.out.println("Request JSON: " + requestJson.toString());

                    webSocket.send(requestJson.toString());

                    while (true) {
                        Thread.sleep(200);
                        if (wsCloseFlag) {
                            break;
                        }
                    }
                    webSocket.close(1000, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class JsonParse {
        Header header;
        Payload payload;
    }

    class Header {
        int code;
        int status;
        String sid;
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String role;
        String content;
    }

    class RoleContent {
        String role;
        String content;

        public RoleContent(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public RoleContent() {}

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
