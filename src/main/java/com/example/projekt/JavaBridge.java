package com.example.projekt;

import com.example.projekt.api.dto.Message;
import com.example.projekt.api.dto.MessageResponse;
import com.example.projekt.controller.ChatController;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class JavaBridge {
    private final ChatController chatController;

    public void setMessages(String message) {
        System.out.println("[FROM JS] " + message);

        try {
            System.out.println("try on message");

            Gson gson = new Gson();
            MessageResponse response = gson.fromJson(message, MessageResponse.class);
            System.out.println("Parsed type: " + response.getType());
            System.out.println("Message count: " + response.getData().size());

            if (response.getType().equals("messages")) {
                List<Message> messages = response.getData();
                messages.removeIf(m -> m.getDate() == null);
                messages.forEach(m -> {
                    System.out.println(m.getContent());
                    m.setDateFormated(new Date(m.getDate().getSeconds() * 1000));
                });
                messages.sort(Comparator.comparing(Message::getDateFormated));
                chatController.setMessages(messages);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setMessages(String message) {
//        System.out.println("[FROM JS] " + message);
//
//        try {
//            System.out.println("try on message");
//
//            Gson gson = new Gson();
//            Map<String, Object> map = gson.fromJson(message, Map.class);
//            String type = (String) map.get("type");
//
//            if ("messages".equals(type)) {
//                System.out.println("mesates equals type");
//
//                Type listType = new TypeToken<List<Message>>() {}.getType();
//                List<Message> messages = gson.fromJson(gson.toJson(map.get("data")), listType);
//
//                messages.forEach(m -> {
//                    if(m.getDate() == null) messages.remove(m);
//                    System.out.println(m.getContent());
//                    m.setDateFormated(new Date(m.getDate().getSeconds() * 1000));
//                });
//                messages.sort(Comparator.comparing(Message::getDateFormated));
//                chatController.setMessages(messages);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public JavaBridge(ChatController chatController) {
        System.out.println("create java bridge");
        this.chatController = chatController;
    }

}
