package com.example.projekt.api.dto;

import lombok.Data;

@Data
public class QueryResponse {
    private Document document;

    @Data
    public static class Document {
        private String name;
        private Fields fields;

        @Data
        public static class Fields {
            private Value uid;

            @Data
            public static class Value {
                private String stringValue;
            }
        }
    }
}
