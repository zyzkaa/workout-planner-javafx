package com.example.projekt.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    private int id =  1;

    private String accessToken;
    private String refreshToken;
    private Date expires;

    public Token(String accessToken, String refreshToken, int expires) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires = new Date(System.currentTimeMillis() + expires * 1000L);
    }

}
