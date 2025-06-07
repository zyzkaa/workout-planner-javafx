package com.example.projekt.api.dto;

import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
public class Client {
    private String id;
    private String name;
    private Timestamp lastMessage;

    public Client(String id, String name, Timestamp lastMessage) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
    }
}
