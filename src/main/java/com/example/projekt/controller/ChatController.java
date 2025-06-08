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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.StringUtils;

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
    @FXML
    private ScrollPane scrollPane;

    private final SimpleStringProperty clientId = new SimpleStringProperty();
    private final ObservableList<Message> messages = FXCollections.observableArrayList();
    private JSObject window;
    private String coachId = AuthSession.getFirebaseLocalId();

    @FXML
    public void initialize() {
        setupWebView();
        setupButtonAndInput();
        setupMessages();
        setupClientId();
        setupScrollPane();
    }

    private void loadMessages() {
        window.call("getMessages");
    }

    private void setupScrollPane(){
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 0.0 && oldVal.doubleValue() != 0.0) {
                System.out.println("Reached top of ScrollPane!");
            }
        });
    }

    private void setupClientId(){
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
            loadMessages();
        });
    }

    private void setupWebView(){
        System.out.println("setupWebView ---------------------------------------------------------------------------------------");
        System.out.println(getClass().getResource("/web/chat.html"));
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
    }

    private void setupMessages(){
        messages.addListener((ListChangeListener<Message>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Platform.runLater(() -> {
                        change.getAddedSubList().forEach(this::showMessage);
                    });
                }
            }
        });

        messagesBox.heightProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setVvalue(1.0);
        });


    }

    private void setupButtonAndInput(){
        sendButton.setDisable(true);
        chatInput.setDisable(true);
        sendButton.setOnAction(event -> handleSendMessage());
        chatInput.setOnAction(event -> handleSendMessage());
    }

    private void handleSendMessage(){
        String text = chatInput.getText();
        if (clientId.get().isEmpty() || clientId.get() == null) return;
        if (StringUtils.isBlank(text)) return;

        chatInput.setText("");
        window.call("sendMessage", text, coachId);
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
//        messages.forEach(m -> System.out.println("Sender: " + m.getSender() + " | Content: " + m.getContent()));
    }

    private void showMessage(Message message) {
        Label messageLabel = new Label(message.getContent());
        messageLabel.getStyleClass().add("chat-message");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(Region.USE_PREF_SIZE);

        HBox wrapper = new HBox(messageLabel);

        if(message.getSender().equals(coachId)){
            wrapper.setAlignment(Pos.CENTER_RIGHT);
        } else {
            wrapper.setAlignment(Pos.CENTER_LEFT);
        }
        wrapper.setPadding(new Insets(5));

        messagesBox.getChildren().add(wrapper);
    }
}
