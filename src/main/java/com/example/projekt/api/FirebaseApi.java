package com.example.projekt.api;

import com.example.projekt.api.dto.QueryRequest;
import com.example.projekt.api.dto.QueryResponse;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface FirebaseApi {
    @POST("databases/(default)/documents:runQuery")
    Call<List<QueryResponse>> getCoachByLocalId(
            @Body QueryRequest body,
            @Query("key") String apiKey,
            @Header("Authorization") String bearerToken
    );

    @PATCH("databases/(default)/documents/coaches/{id}")
    Call<QueryResponse> addCoach(
            @Path("id") String id,
            @Query("key") String apiKey,
            @Header("Content-Type") String contentType,
            @Header("Authorization") String bearerToken,
            @Body Map<String, Object> body
    );



}
