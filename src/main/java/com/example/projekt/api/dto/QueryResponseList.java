package com.example.projekt.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryResponseList<T> {
    private List<QueryResponseSingle<T>> documents;
}
