package com.example.projekt.api.dto;

import lombok.Data;

@Data
public class ClientFields {
    private QueryResponseSingle.FirestoreStringField name;
    private QueryResponseSingle.FirestoreTimestampField lastMessage;
}
