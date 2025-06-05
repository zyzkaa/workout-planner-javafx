package com.example.projekt.api;

import com.example.projekt.api.dto.FirebaseSignInResponse;
import com.example.projekt.api.dto.GoogleSignInRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoogleApi {
    @POST("v1/accounts:signInWithIdp")
    Call<FirebaseSignInResponse> signInToFirebase(
            @Query("key") String apiKey,
            @Body GoogleSignInRequest request);
}
