package com.example.projekt;

import com.example.projekt.api.RefreshTokenService;
import com.example.projekt.api.dto.FirebaseRefreshResponse;
import com.example.projekt.api.dto.FirebaseSignInResponse;
import com.example.projekt.util.AppConfig;
import retrofit2.Response;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuthSession {
    private static Token firebaseToken;
    public static String google;
    private static final ReentrantReadWriteLock firebaseLock = new ReentrantReadWriteLock();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> refreshTask;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (refreshTask != null && !refreshTask.isDone()) {
                refreshTask.cancel(false);
            }
            scheduler.shutdown();
        }));
    }

    public static void setFirebaseToken(FirebaseSignInResponse firebaseResponse){
        if (refreshTask != null && !refreshTask.isDone()) {
            boolean cancelled = refreshTask.cancel(false);
        }

        firebaseLock.writeLock().lock();
        try {
            AuthSession.firebaseToken = new Token(firebaseResponse);
        } finally {
            firebaseLock.writeLock().unlock();
        }

        long expiresSeconds = Long.parseLong(firebaseResponse.expiresIn);
        long refreshTime = expiresSeconds - 10;
        refreshTask = scheduler.scheduleWithFixedDelay(
                () ->  refreshFirebaseToken(),
                refreshTime,
                refreshTime,
                TimeUnit.SECONDS
        );
}

    private static void refreshFirebaseToken(){
        try {
            Response<FirebaseRefreshResponse> response = RefreshTokenService.getInstance()
                    .getRefreshTokenApi()
                    .refreshFirebaseToken(
                            AppConfig.getProperty("firebase.apiKey"),
                            "refresh_token",
                            firebaseToken.refreshToken
                    ).execute();

            if (response.isSuccessful() && response.body() != null) {
                FirebaseRefreshResponse refreshResponse = response.body();
                firebaseLock.writeLock().lock();
                try {
                    firebaseToken.idToken = refreshResponse.getIdToken();
                    firebaseToken.refreshToken = refreshResponse.getRefreshToken();
                } finally {
                    firebaseLock.writeLock().unlock();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }}

    private static Token getFirebaseToken(){
        firebaseLock.readLock().lock();
        try {
            return firebaseToken;
        } finally {
            firebaseLock.readLock().unlock();
        }
    }

    public static String getFirebaseIdToken(){
        return firebaseToken.idToken;
    }

    public static String getFirebaseLocalId(){
        return firebaseToken.localId;
    }

    public static String getFirebaseEmail(){
        return firebaseToken.email;
    }

    public static void clear(){
        if (refreshTask != null && !refreshTask.isDone()) {
            boolean cancelled = refreshTask.cancel(false);
        }

        firebaseLock.writeLock().lock();
        try {
            AuthSession.firebaseToken = null;
        } finally {
            firebaseLock.writeLock().unlock();
        }
    }

    private static class Token {
        private String idToken;
        private String refreshToken;
        private final Long expires;
        private final String localId;
        private final String email;

        public Token(FirebaseSignInResponse firebaseToken) {
            this.idToken = firebaseToken.getIdToken();
            this.refreshToken = firebaseToken.getRefreshToken();
            this.expires = Long.parseLong(firebaseToken.getExpiresIn());
            this.localId = firebaseToken.getLocalId();
            this.email = firebaseToken.getEmail();
        }
    }
}