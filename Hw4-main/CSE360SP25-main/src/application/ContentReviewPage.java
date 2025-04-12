package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ContentReviewPage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    public ContentReviewPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        Label title = new Label("Content Review");

        Button viewQuestionsBtn = new Button("View All Questions");
        viewQuestionsBtn.setOnAction(e -> {
            // Implementation to view all questions
        });

        Button viewAnswersBtn = new Button("View All Answers");
        viewAnswersBtn.setOnAction(e -> {
            // Implementation to view all answers
        });

        Button viewMessagesBtn = new Button("View All Private Messages");
        viewMessagesBtn.setOnAction(e -> {
            // Implementation to view all messages
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            if (currentUser.getRoles().contains("admin"))
                new AdminHomePage().show(primaryStage, currentUser);
            else if (currentUser.getRoles().contains("instructor"))
                new InstructorHomePage(dbHelper, currentUser).show(primaryStage);
            else if (currentUser.getRoles().contains("staff"))
                new StaffHomePage(dbHelper, currentUser).show(primaryStage);
        });

        layout.getChildren().addAll(title, viewQuestionsBtn, viewAnswersBtn, viewMessagesBtn, backBtn);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Content Review");
        primaryStage.show();
    }
}
