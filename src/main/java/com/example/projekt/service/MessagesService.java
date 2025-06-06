package com.example.projekt.service;

import com.example.projekt.AuthSession;
import com.example.projekt.api.FirebaseApi;
import com.example.projekt.api.FirebaseService;
import com.example.projekt.api.dto.QueryRequest;
import com.example.projekt.api.dto.QueryResponse;
import com.example.projekt.util.AppConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class MessagesService {
    @Getter
    private static MessagesService instance = new MessagesService();

    private final FirebaseApi firebaseApi = FirebaseService.getInstance().getFirebaseApi();

    private Map<String, Object> getRequestBody(){
        Map<String, Object> body = new HashMap<>();
        return body;
    }

    public void loginUser(){
        String apiKey = AppConfig.getProperty("firebase.apiKey");
        String idToken = AuthSession.getFirebaseIdToken();
        String uid = AuthSession.getFirebaseLocalId();

        QueryRequest request = new QueryRequest(uid);

        try {
            Response<List<QueryResponse>> response = firebaseApi.getCoachByLocalId(
                    request,
                    apiKey,
                    "Bearer " + idToken
            ).execute();

            if(!response.isSuccessful()) throw new RuntimeException();
            if(response.body() == null) throw new RuntimeException();

            List<QueryResponse> responses = response.body();
            if(responses.size() > 1) throw new RuntimeException();

            QueryResponse user = responses.getFirst();

            if(user.getDocument() == null) {
                Map<String, Object> fields = new HashMap<>();
                fields.put("email", Map.of("stringValue", AuthSession.getFirebaseEmail()));
                Map<String, Object> body = new HashMap<>();
                body.put("fields", fields);

                Response<QueryResponse> addResponse = firebaseApi.addCoach(
                        uid,
                        apiKey,
                        "application/json",
                        "Bearer " + idToken,
                        body
                ).execute();

                if(!addResponse.isSuccessful()) throw new RuntimeException();
                if(addResponse.body() == null) throw new RuntimeException();
            } else {
                System.out.println("jest w bazie");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
