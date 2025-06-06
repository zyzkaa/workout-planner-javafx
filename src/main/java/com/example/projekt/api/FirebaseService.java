package com.example.projekt.api;

import com.example.projekt.util.AppConfig;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseService {
    private static FirebaseService instance;

    @Getter
    private final FirebaseApi firebaseApi;

    public FirebaseService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://firestore.googleapis.com/v1/projects/" + AppConfig.getProperty("firebase.projectId") + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        firebaseApi = retrofit.create(FirebaseApi.class);
    }

    public static FirebaseService getInstance() {
        if (instance == null) {
            instance = new FirebaseService();
        }
        return instance;
    }
}
