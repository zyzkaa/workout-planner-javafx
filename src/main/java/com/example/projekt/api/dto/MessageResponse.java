package com.example.projekt.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageResponse {
    private String type;
    private List<Message> data;
}
