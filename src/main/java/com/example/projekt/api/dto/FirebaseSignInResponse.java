package com.example.projekt.api.dto;

import lombok.Getter;

@Getter
public class FirebaseSignInResponse {
    public String idToken;
    public String refreshToken;
    public String expiresIn;
    public String localId;
    public String email;
}
