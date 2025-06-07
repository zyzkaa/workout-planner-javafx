package com.example.projekt.api.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class FirebaseRefreshResponse {
    @SerializedName("id_token")
    private String idToken;

    @SerializedName("refresh_token")
    private String refreshToken;
}
