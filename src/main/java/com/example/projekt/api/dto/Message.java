package com.example.projekt.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private String id;
    private String content;
    private String sender;
    private MyDate date;
    private Date dateFormated = null;

    @Data
    public static class MyDate{
        private long seconds;
        private long nanoseconds;
    }
}
