package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;

public class ReviewManagementPage {

    private DatabaseHelper dbHelper;
    private Answer answer;
    private User currentUser;

    public ReviewManagementPage(DatabaseHelper dbHelper, Answer answer, User currentUser) {
        this.dbHelper = dbHelper;
        this.answer = answer;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("Reviews for Answer ID: " + answer.getAnswerID());

        ListView<Review> reviewListView = new ListView<>();
        ObservableList<Review> reviewsObs = FXCollections.observableArrayList(
                dbHelper.getReviewsByAnswerId(answer.getAnswerID()));
        reviewListView.setItems(reviewsObs);

        reviewListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Review r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) {
                    setText(null);
                } else {
                    setText("Reviewer: " + r.getReviewerUserName() + " | Rating: " + r.getRating()
                            + "\n" + r.getReviewText());
                }
            }
        });

        // Allow adding a review if the current user is a reviewer.
        TextArea reviewArea = new TextArea();
        reviewArea.setPromptText("Write your review...");
        Spinner<Integer> ratingSpinner = new Spinner<>(1, 5, 3);
        Button addReviewBtn = new Button("Add Review");
        addReviewBtn.setOnAction(e -> {
            String text = reviewArea.getText();
            if (!text.isBlank()) {
                Review newRev = new Review();
                newRev.setAnswerId(answer.getAnswerID());
                newRev.setReviewerUserName(currentUser.getUserName());
                newRev.setReviewText(text);
                newRev.setRating(ratingSpinner.getValue());
                dbHelper.addReview(newRev);
                reviewsObs.setAll(dbHelper.getReviewsByAnswerId(answer.getAnswerID()));
                reviewArea.clear();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            Question q = StartCSE360.getQuestions().getQuestionByID(answer.getQuestionID());
            new QuestionDetailPage(q, StartCSE360.getQuestions(), StartCSE360.getAnswers(), currentUser)
                    .show(primaryStage);
        });

        layout.getChildren().addAll(title, reviewListView);
        if (currentUser.getRoles().contains("reviewer")) {
            layout.getChildren().addAll(reviewArea, ratingSpinner, addReviewBtn);
        }
        layout.getChildren().add(backBtn);

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Review Management");
        primaryStage.show();
    }
}
