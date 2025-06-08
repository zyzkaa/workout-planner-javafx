package com.example.projekt.api;

import com.example.projekt.api.dto.FirebaseRefreshResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RefreshTokenApi {
    @FormUrlEncoded
    @POST("v1/token")
    Call<FirebaseRefreshResponse> refreshFirebaseToken(
            @Query("key") String apiKey,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken
    );
}
