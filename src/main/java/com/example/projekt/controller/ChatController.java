package com.example.projekt.controller;

import com.example.projekt.AuthSession;
import com.example.projekt.JavaBridge;
import com.example.projekt.api.dto.Message;
import com.example.projekt.model.dto.ExerciseDto;
import com.example.projekt.util.AppConfig;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.util.List;

public class ChatController {
    @FXML
    VBox root;

    @FXML
    private WebView webView;

    @FXML
    private VBox messagesBox;

    @FXML
    private Button sendButton;

    @FXML
    private TextField chatInput;

    private final SimpleStringProperty clientId = new SimpleStringProperty();
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    JSObject window;

    @FXML
    public void initialize() {
        sendButton.setDisable(true);
        chatInput.setDisable(true);

        messages.addListener((ListChangeListener<Message>) change -> {
            System.out.println("add mesate listerer");

            while (change.next()) {
                if (change.wasAdded()) {
                    Platform.runLater(() -> {
                        change.getAddedSubList().forEach(message -> messagesBox.getChildren().add(new Text(message.getContent())));
                    });
                }
            }
        });

        webView.getEngine().load(getClass().getResource("/web/chat.html").toExternalForm());

        webView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                window = (JSObject) webView.getEngine().executeScript("window");
                window.setMember("java", new JavaBridge(this));

                JSObject firebaseConfig = (JSObject) webView.getEngine().executeScript("new Object()");
                firebaseConfig.setMember("apiKey", AppConfig.getProperty("firebase.apiKey"));
                firebaseConfig.setMember("projectId", AppConfig.getProperty("firebase.projectId"));
                window.setMember("firebaseConfig", firebaseConfig);
            }
        });

        ChangeListener<String> clientIdListener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    sendButton.setDisable(false);
                    chatInput.setDisable(false);

                    clientId.removeListener(this);
                }
            }
        };
        clientId.addListener(clientIdListener);

        clientId.addListener((observable, oldValue, newValue) -> {
            if (window == null || newValue == null) return;
            System.out.println("client id listener");
            JSObject userData = (JSObject) webView.getEngine().executeScript("new Object()");
            userData.setMember("coachId", AuthSession.getFirebaseLocalId());
            userData.setMember("clientId", newValue);
            window.setMember("userData", userData);
            System.out.println("client id listener 2 " + newValue + AuthSession.getFirebaseLocalId());
//            System.out.println(newValue);
            window.call("getMessages");
        });

        sendButton.setOnAction(event -> handleSendMessage());
        chatInput.setOnAction(event -> handleSendMessage());
    }

    public ChatController(){
    }

    private void handleSendMessage(){
        String text = chatInput.getText();
        if (clientId.get().isEmpty() || clientId.get() == null) return;
        if (text == null || text.trim().isEmpty()) return;
        chatInput.setText("");
        System.out.println(text);

//        JSObject newMessage = (JSObject) webView.getEngine().executeScript("new Object()");
//        newMessage.setMember("content", text);
//        window.setMember("newMessage", newMessage);
        window.call("sendMessage", text);
    }

    public void setClientId(String clientId) {
        System.out.println("set client id");
        this.clientId.set(clientId);
    }

    public void setMessages(List<Message> messages) {
        System.out.println("set messages");
        messagesBox.getChildren().clear();
        this.messages.clear();
        this.messages.addAll(messages);
        messages.forEach(m -> System.out.println(m.getContent()));
    }

//    public void addMessage(List<Message> messages) {
//        this.messages.addAll(messages);
//    }
}
