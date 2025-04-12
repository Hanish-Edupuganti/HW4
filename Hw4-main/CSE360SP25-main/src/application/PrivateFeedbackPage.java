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

public class PrivateFeedbackPage {

    private DatabaseHelper dbHelper;
    private Question question;
    private User currentUser;

    public PrivateFeedbackPage(DatabaseHelper dbHelper, Question question, User currentUser) {
        this.dbHelper = dbHelper;
        this.question = question;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("Private Messages for Question: " + question.getQuestionTitle());

        ListView<Message> messagesListView = new ListView<>();
        ObservableList<Message> messagesObservable = FXCollections.observableArrayList(
                dbHelper.getMessagesForQuestion(question.getQuestionID(), currentUser.getUserName()));
        messagesListView.setItems(messagesObservable);

        // Customize how messages are displayed.
        messagesListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                } else {
                    setText("From: " + msg.getFromUser() + "\n" + msg.getContent());
                }
            }
        });

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your private feedback here...");

        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> {
            String content = messageArea.getText();
            if (!content.isBlank()) {
                Message newMsg = new Message();
                newMsg.setFromUser(currentUser.getUserName());
                newMsg.setToUser(question.getAuthor()); // or another recipient as needed
                newMsg.setQuestionId(question.getQuestionID());
                newMsg.setContent(content);
                dbHelper.addMessage(newMsg);
                messagesObservable.setAll(dbHelper.getMessagesForQuestion(question.getQuestionID(), currentUser.getUserName()));
                messageArea.clear();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            new QuestionDetailPage(question, StartCSE360.getQuestions(), StartCSE360.getAnswers(), currentUser)
                    .show(primaryStage);
        });

        layout.getChildren().addAll(title, messagesListView, messageArea, sendBtn, backBtn);
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Private Feedback");
        primaryStage.show();
    }
}