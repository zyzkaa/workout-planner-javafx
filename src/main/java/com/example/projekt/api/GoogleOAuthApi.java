package com.example.projekt.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GoogleOAuthApi {
    @FormUrlEncoded
    @POST("token")
    Call<GoogleTokenResponse> exchangeCodeForToken(
            @Field("code") String code,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("redirect_uri") String redirect_uri,
            @Field("grant_type") String grant_type
    );
}
