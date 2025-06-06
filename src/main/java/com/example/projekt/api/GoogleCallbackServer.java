package com.example.projekt.api;

import com.example.projekt.AppEventBus;
import com.example.projekt.AuthSession;
import com.example.projekt.api.dto.FirebaseSignInResponse;
import com.example.projekt.api.dto.GoogleSignInRequest;
import com.example.projekt.api.dto.GoogleTokenResponse;
import com.example.projekt.event.bus.UserLoginEvent;
import com.example.projekt.util.AppConfig;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import retrofit2.Response;

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

//        for windows
//        Desktop.getDesktop().browse(new URI(url));

        Runtime.getRuntime().exec(new String[] { "xdg-open", url });
    }

    private void handleCallback(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String code = extractParam(query, "code");

        if (code == null || tokenAlreadyExchanged) {
            exchange.sendResponseHeaders(204, -1); // No Content
            return;
        }

        tokenAlreadyExchanged = true;

        GoogleTokenResponse googleTokenResponse = exchangeCodeForToken(code);

        if(googleTokenResponse == null){
            exchange.sendResponseHeaders(401, -1);
            return;
        }

        AuthSession.google = googleTokenResponse.getAccessToken();

        FirebaseSignInResponse firebaseTokenResponse = loginToFirebase(googleTokenResponse);

        if(firebaseTokenResponse == null){
            exchange.sendResponseHeaders(401, -1);
            return;
        }

        AuthSession.setFirebaseToken(firebaseTokenResponse);
        AppEventBus.getAsyncBus().post(new UserLoginEvent());

        String response = "Log in successfull, you can close this window";
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }

        server.stop(10000);
    }

    private FirebaseSignInResponse loginToFirebase(GoogleTokenResponse googleToken){
        try {
            Response<FirebaseSignInResponse> response = GoogleService.getInstance().getGoogleApi().signInToFirebase(
                    AppConfig.getProperty("firebase.apiKey"),
                    new GoogleSignInRequest(googleToken.getIdToken())
            ).execute();

            if (response.isSuccessful()) {
                System.out.println(response.body());
                return response.body();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}