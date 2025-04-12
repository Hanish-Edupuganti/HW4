package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class MessagingPage {
    private DatabaseHelper dbHelper;
    private User currentUser;

    public MessagingPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label title = new Label("Messaging Center");

        // Retrieve messages for the current user.
        List<Message> messages = dbHelper.getMessagesForUser(currentUser.getUserName());
        ObservableList<Message> observableMessages = FXCollections.observableArrayList(messages);
        ListView<Message> messageListView = new ListView<>(observableMessages);
        messageListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                } else {
                    String direction = msg.getFromUser().equals(currentUser.getUserName())
                            ? "Sent to: " : "Received from: ";
                    String other = msg.getFromUser().equals(currentUser.getUserName())
                            ? msg.getToUser() : msg.getFromUser();
                    setText(direction + other + "\n" + msg.getContent() + "\n(" + msg.getCreationTime() + ")");
                }
            }
        });

        // Compose new message section.
        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient username");
        TextArea newMessageArea = new TextArea();
        newMessageArea.setPromptText("Type your message here...");

        Button sendBtn = new Button("Send Message");
        sendBtn.setOnAction(e -> {
            String recipient = recipientField.getText();
            String content = newMessageArea.getText();
            if (recipient.isBlank() || content.isBlank()) {
                return;
            }
            Message newMsg = new Message();
            newMsg.setFromUser(currentUser.getUserName());
            newMsg.setToUser(recipient);
            newMsg.setContent(content);
            dbHelper.addMessage(newMsg);
            observableMessages.setAll(dbHelper.getMessagesForUser(currentUser.getUserName()));
            recipientField.clear();
            newMessageArea.clear();
        });

        // New: Delete Selected Message button.
        Button deleteBtn = new Button("Delete Selected Message");
        deleteBtn.setOnAction(e -> {
            Message selected = messageListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            // Allow deletion if the current user is sender or recipient.
            if (selected.getFromUser().equals(currentUser.getUserName())
                    || selected.getToUser().equals(currentUser.getUserName())) {
                boolean success = dbHelper.deleteMessage(selected.getMessageId());
                if (success) {
                    observableMessages.setAll(dbHelper.getMessagesForUser(currentUser.getUserName()));
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You can only delete messages you sent or received.");
                alert.showAndWait();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            if (currentUser.getRoles().contains("admin"))
                new AdminHomePage().show(primaryStage, currentUser);
            else
                new UserHomePage().show(primaryStage, currentUser);
        });

        layout.getChildren().addAll(title, messageListView,
                new Label("Compose New Message:"), recipientField, newMessageArea, sendBtn, deleteBtn, backBtn);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Messaging Center");
        primaryStage.show();
    }
}
