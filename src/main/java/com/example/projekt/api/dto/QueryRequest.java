package com.example.projekt.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryRequest {
    private StructuredQuery structuredQuery;

    public QueryRequest(String uid) {
        this.structuredQuery = new StructuredQuery(uid);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StructuredQuery {
        private From[] from = { new From("coaches") };
        private Where where;
        private int limit = 1;

        public StructuredQuery(String uid) {
            this.where = new Where(new FieldFilter(new Field("uid"), "EQUAL", new Value(uid)));
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class From {
            private String collectionId;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Where {
            private FieldFilter fieldFilter;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class FieldFilter {
            private Field field;
            private String op;
            private Value value;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Field {
            private String fieldPath;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Value {
            private String stringValue;
        }
    }
}
