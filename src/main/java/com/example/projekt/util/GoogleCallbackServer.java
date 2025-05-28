package com.example.projekt.util;

import com.example.projekt.AppEventBus;
import com.example.projekt.api.GoogleTokenResponse;
import com.example.projekt.event.bus.UserLoginEvent;
import com.example.projekt.model.entity.Token;
import com.example.projekt.repository.TokenRepository;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class GoogleCallbackServer {
    private boolean tokenAlreadyExchanged = false;
    private HttpServer server = null;

    public void start() throws IOException, URISyntaxException {
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/callback", this::handleCallback);
        server.start();
        System.out.println("Listening on port 8080");

        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + AppConfig.getProperty("google.clientId")
                + "&redirect_uri=" + AppConfig.getProperty("google.redirect")
                + "&response_type=code"
                + "&access_type=offline"
                + "&scope=openid%20email"
                + "&prompt=consent";

        Desktop.getDesktop().browse(new URI(url));
    }

    private void handleCallback(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String code = extractParam(query, "code");

        if (code == null || tokenAlreadyExchanged) {
            exchange.sendResponseHeaders(204, -1); // No Content
            return;
        }

        tokenAlreadyExchanged = true;

//        System.out.println("Authorization code: " + code);

        GoogleTokenResponse tokenResponse = exchangeCodeForToken(code);
//        System.out.println("Token response:\n" + tokenResponse);

        if(tokenResponse == null){
            exchange.sendResponseHeaders(401, -1);
            return;
        }

        TokenRepository.getInstance().saveToken(new Token(
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getExpiresIn()
        ));

        AppEventBus.getAsyncBus().post(new UserLoginEvent());
        String response = "Log in successfull, you can close this window";
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
        server.stop(10000);

    }

    private String extractParam(String query, String key) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return null;
    }

    private GoogleTokenResponse exchangeCodeForToken(String code) {
        try {
            String params = "code=" + code
                    + "&client_id=" + AppConfig.getProperty("google.clientId")
                    + "&client_secret=" + AppConfig.getProperty("google.clientSecret")
                    + "&redirect_uri=http://localhost:8080/callback"
                    + "&grant_type=authorization_code";

            HttpURLConnection conn = (HttpURLConnection) new URL("https://oauth2.googleapis.com/token").openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(params.getBytes());
            }

            String json = new String(conn.getInputStream().readAllBytes());
            Gson gson = new Gson();
            return gson.fromJson(json, GoogleTokenResponse.class);

//            int responseCode = conn.getResponseCode();
//            InputStream is = (responseCode >= 200 && responseCode < 300)
//                    ? conn.getInputStream()
//                    : conn.getErrorStream();
//
//            String json = new String(is.readAllBytes());
//            System.out.println("Raw response:\n" + json);
//
//            if (responseCode >= 200 && responseCode < 300) {
//                Gson gson = new Gson();
//                GoogleTokenResponse token = gson.fromJson(json, GoogleTokenResponse.class);
//
//                System.out.println("Access token: " + token.accessToken());
//                System.out.println("ID token: " + token.idToken());
//
//                return token.accessToken();
//            } else {
//                System.err.println("Error: HTTP " + responseCode);
//                return null;
//            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}