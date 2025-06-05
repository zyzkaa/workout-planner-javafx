package com.example.projekt.api.dto;

public class GoogleSignInRequest {
    private String postBody;
    private String requestUri;
    private boolean returnSecureToken;
    private boolean returnIdpCredential;

    public GoogleSignInRequest(String idToken) {
        this.postBody = "id_token=" + idToken + "&providerId=google.com";
        this.requestUri = "http://localhost";
        this.returnSecureToken = true;
        this.returnIdpCredential = true;
    }
}
