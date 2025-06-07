package com.example.projekt.api;

import lombok.Getter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;


public class ExerciseApiService {
    private static ExerciseApiService instance;

    @Getter
    private final ExerciseApi exerciseApi;

    private ExerciseApiService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://exercisedb-api.vercel.app/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        exerciseApi = retrofit.create(ExerciseApi.class);
    }

    public static synchronized ExerciseApiService getInstance() {
        if (instance == null) instance = new ExerciseApiService();
        return instance;
    }

}
