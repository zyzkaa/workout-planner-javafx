package com.example.projekt.model.entity;

import com.example.projekt.api.dto.FirebaseSignInResponse;
import com.example.projekt.api.dto.GoogleTokenResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;

import javax.annotation.Nullable;
import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    private String id;

    //id token for firebase token
    private String accessToken;
    private String refreshToken;
    private Date expires;

    @Nullable
    private String localId;

    @Nullable
    private String email;

    public Token(GoogleTokenResponse googleToken) {
        this.id = "google";
        this.accessToken = googleToken.getAccessToken();
        this.refreshToken = googleToken.getRefreshToken();
        this.expires = new Date(System.currentTimeMillis() + googleToken.getExpiresIn() * 1000);
    }

    public Token(FirebaseSignInResponse firebaseToken) {
        this.id = "firebase";
        this.accessToken = firebaseToken.getIdToken();
        this.refreshToken = firebaseToken.getRefreshToken();
        this.expires = new Date(System.currentTimeMillis() + firebaseToken.getExpiresIn() * 1000);
        this.localId = firebaseToken.getLocalId();
        this.email = firebaseToken.getEmail();
    }

}
