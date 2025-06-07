package com.example.projekt.service;

import com.example.projekt.AuthSession;
import com.example.projekt.api.FirebaseApi;
import com.example.projekt.api.FirebaseApiService;
import com.example.projekt.api.dto.*;
import com.example.projekt.util.AppConfig;
import org.checkerframework.checker.units.qual.A;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientsService {

    private final static FirebaseApi firebaseApi = FirebaseApiService.getInstance().getFirebaseApi();

    public static List<Client> getClients() {
        try {
            Response<QueryResponseList<ClientFields>> response = firebaseApi.getCoachClients(
                    AuthSession.getFirebaseLocalId(),
                    "Bearer " + AuthSession.getFirebaseIdToken()
            ).execute();

            if(response.body() == null) return new ArrayList<>();
            var body = response.body();

            return body.getDocuments().stream()
                    .map(client -> new Client(client.getName(), client.getFields().getName().getStringValue(), client.getFields().getLastMessage().getTimestampValue()))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addNewCoach(String id, String idToken) throws IOException {
        System.out.println("dodawanie");

        Map<String, Object> fields = new HashMap<>();
        fields.put("email", Map.of("stringValue", AuthSession.getFirebaseEmail()));
        Map<String, Object> body = new HashMap<>();
        body.put("fields", fields);

        Response<QueryResponseSingle<CoachFields>> addResponse = firebaseApi.addCoach(
                id,
                "Bearer " + idToken,
                body
        ).execute();

        if(!addResponse.isSuccessful()) throw new RuntimeException();
        if(addResponse.body() == null) throw new RuntimeException();
    }

    public static void loginUser(){
        String apiKey = AppConfig.getProperty("firebase.apiKey");
        String idToken = AuthSession.getFirebaseIdToken();
        String uid = AuthSession.getFirebaseLocalId();

        try {
            Response<QueryResponseSingle<CoachFields>> response = firebaseApi.getCoachByLocalId(
                    uid,
                    "Bearer " + idToken
            ).execute();

            if(!response.isSuccessful()){
                addNewCoach(uid, idToken);
            }

            System.out.println("logowanie");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
