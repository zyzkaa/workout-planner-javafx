package com.example.projekt.service;

import com.example.projekt.api.FirebaseService;
import com.example.projekt.api.dto.QueryRequest;
import com.example.projekt.api.dto.QueryResponse;
import com.example.projekt.model.entity.Token;
import com.example.projekt.repository.TokenRepository;
import com.example.projekt.util.AppConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;

@NoArgsConstructor
public class MessagesService {
    @Getter
    private static MessagesService instance = new MessagesService();

    public void loginUser(){
        Token firebaseToken = TokenRepository.getInstance().getFirebaseToken();
        String apiKey = AppConfig.getProperty("firebase.apiKey");
        String idToken = firebaseToken.getAccessToken();
        String uid = firebaseToken.getLocalId();

        QueryRequest request = new QueryRequest(uid);

        try {
            Response<List<QueryResponse>> response = FirebaseService.getInstance().getFirebaseApi().getCoachByLocalId(
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
                System.out.println("nie ma w bazie");
            }

            System.out.println("jest w bazie");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }

}
