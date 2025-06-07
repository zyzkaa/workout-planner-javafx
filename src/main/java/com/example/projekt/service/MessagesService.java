package com.example.projekt.service;

import com.example.projekt.api.FirebaseApi;
import com.example.projekt.api.FirebaseApiService;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MessagesService {
    @Getter
    private static MessagesService instance = new MessagesService();

    private final FirebaseApi firebaseApi = FirebaseApiService.getInstance().getFirebaseApi();

}
