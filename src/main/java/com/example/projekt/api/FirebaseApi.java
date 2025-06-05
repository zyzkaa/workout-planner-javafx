package com.example.projekt.api;

import com.example.projekt.api.dto.QueryRequest;
import com.example.projekt.api.dto.QueryResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FirebaseApi {
    @POST("databases/(default)/documents:runQuery")
    Call<List<QueryResponse>> getCoachByLocalId(
            @Body QueryRequest body,
            @Query("key") String apiKey,
            @Header("Authorization") String bearerToken
    );
}
