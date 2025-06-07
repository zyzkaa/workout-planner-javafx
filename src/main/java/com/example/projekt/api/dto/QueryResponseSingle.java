package com.example.projekt.api.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class QueryResponseSingle<T> {
    private String name;
    private T fields;

    @Data
    public static class FirestoreStringField {
        private String stringValue;
    }

    @Data
    public static class FirestoreTimestampField {
        private Timestamp timestampValue;
    }
}
