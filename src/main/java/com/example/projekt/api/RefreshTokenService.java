package com.example.projekt.api;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RefreshTokenService {
    @Getter
    private final RefreshTokenApi refreshTokenApi;
    private static RefreshTokenService instance;

    public RefreshTokenService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://securetoken.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        refreshTokenApi = retrofit.create(RefreshTokenApi.class);
    }

    public static RefreshTokenService getInstance() {
        if (instance == null) instance = new RefreshTokenService();
        return instance;
    }
}
