package com.example.projekt.api;

import com.example.projekt.util.AppConfig;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleService {
    private static GoogleService instance;

    @Getter
    private final GoogleApi googleApi;

    public GoogleService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://identitytoolkit.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        googleApi = retrofit.create(GoogleApi.class);
    }

    public static GoogleService getInstance() {
        if (instance == null) {
            instance = new GoogleService();
        }
        return instance;
    }
}
