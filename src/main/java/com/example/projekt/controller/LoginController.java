package com.example.projekt.controller;

import com.example.projekt.AppEventBus;
import com.example.projekt.event.bus.UserLoginEvent;
import com.example.projekt.model.entity.Client;
import com.example.projekt.repository.ClientRepository;
import com.example.projekt.util.GoogleCallbackServer;
import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;

import java.awt.*;
import java.net.URI;

public class LoginController {
    public void handleGoogleLogin() {
        try {
            new GoogleCallbackServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test(){
        ClientRepository.getInstance().add(new Client("asd"));
        AppEventBus.getAsyncBus().post(new UserLoginEvent());
    }


}
