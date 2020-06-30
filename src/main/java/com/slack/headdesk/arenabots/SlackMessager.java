package com.slack.headdesk.arenabots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackMessager {
    private String token;

    public SlackMessager() {
        this.token = System.getenv("BOT_TOKEN");
    }

    public void sendMessage(String message, String user) {
        try {
            URL url = new URL("https://slack.com/api/chat.postMessage");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization","Bearer "+ this.token);
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String payload = "{" +
                    "\"channel\": \"" + user + "\", " +
                    "\"text\": \"" + message + "\", " +
                    "\"as_user\": \"true\"" +
                    "}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;

            StringBuilder response = new StringBuilder();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
        }
        catch (IOException ignored) {
            System.out.println("Error with sending slack message");
        }
    }
}
