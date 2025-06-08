package com.example.projekt.api;

import com.example.projekt.api.dto.ClientFields;
import com.example.projekt.api.dto.CoachFields;
import com.example.projekt.api.dto.QueryResponseList;
import com.example.projekt.api.dto.QueryResponseSingle;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface FirebaseApi {
    @GET("databases/(default)/documents/coaches/{id}")
    Call<QueryResponseSingle<CoachFields>> getCoachByLocalId(
            @Path("id") String id,
            @Header("Authorization") String bearerToken
    );

    @PATCH("databases/(default)/documents/coaches/{id}")
    Call<QueryResponseSingle<CoachFields>> addCoach(
            @Path("id") String id,
            @Header("Authorization") String bearerToken,
            @Body Map<String, Object> body
    );

    @GET("databases/(default)/documents/coaches/{id}/users")
    Call<QueryResponseList<ClientFields>> getCoachClients(
            @Path("id") String id,
            @Header("Authorization") String bearerToken
    );

}
