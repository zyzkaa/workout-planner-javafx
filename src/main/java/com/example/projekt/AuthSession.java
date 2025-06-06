package com.example.projekt;

import com.example.projekt.api.dto.FirebaseSignInResponse;
import com.example.projekt.api.dto.GoogleTokenResponse;
import lombok.Getter;

import java.sql.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AuthSession {
    private static Token firebaseToken;
    public static String google;
    private static final ReentrantReadWriteLock firebaseLock = new ReentrantReadWriteLock();

    public static void setFirebaseToken(FirebaseSignInResponse firebaseResponse){
        firebaseLock.writeLock().lock();
        try {
            AuthSession.firebaseToken = new Token(firebaseResponse);
        } finally {
            firebaseLock.writeLock().unlock();
        }
    }

    private static Token getFirebaseToken(){
        firebaseLock.readLock().lock();
        try {
            return firebaseToken;
        } finally {
            firebaseLock.readLock().unlock();
        }
    }

    public static String getFirebaseIdToken(){
        return getFirebaseToken().idToken;
    }

    public static String getFirebaseLocalId(){
        return getFirebaseToken().localId;
    }

    public static String getFirebaseEmail(){
        return getFirebaseToken().email;

    }

    public static void clear(){
        firebaseLock.writeLock().lock();
        try {
            AuthSession.firebaseToken = null;
        } finally {
            firebaseLock.writeLock().unlock();
        }
    }

    @Getter
    private static class Token {
        private final String idToken;
        private final String refreshToken;
        private final Date expires;
        private final String localId;
        private final String email;

        public Token(FirebaseSignInResponse firebaseToken) {
            this.idToken = firebaseToken.getIdToken();
            this.refreshToken = firebaseToken.getRefreshToken();
            this.expires = new Date(System.currentTimeMillis() + firebaseToken.getExpiresIn() * 1000);
            this.localId = firebaseToken.getLocalId();
            this.email = firebaseToken.getEmail();
        }
    }

}
