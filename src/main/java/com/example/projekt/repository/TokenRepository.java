package com.example.projekt.repository;

import com.example.projekt.model.entity.Token;
import com.example.projekt.util.JpaUtil;
import lombok.Getter;

public class TokenRepository {
    @Getter
    private static final TokenRepository instance = new TokenRepository();

    private TokenRepository() {}

    public void saveToken(Token token) {
        JpaUtil.doInTransactionVoid(session -> {
            Token oldToken = session.find(Token.class, token.getId());

            if (oldToken != null) {
                token.setAccessToken(token.getAccessToken());
                token.setRefreshToken(token.getRefreshToken());
                token.setExpires(token.getExpires());
                session.merge(token);
            } else {
                session.persist(token);
            }
        });
    }
}
