package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminHomePage {

    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label adminLabel = new Label("Hello, " + user.getUserName() + "!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button manageQuestionsBtn = new Button("Manage Questions");
        manageQuestionsBtn.setOnAction(e -> {
            Questions questions = StartCSE360.getQuestions();
            Answers answers = StartCSE360.getAnswers();
            new QuestionsPage(questions, answers, user).show(primaryStage);
        });
        
        Button userMgmtBtn = new Button("Manage Users");
        userMgmtBtn.setOnAction(e -> {
            new UserManagementPage(StartCSE360.getDatabaseHelper()).show(primaryStage);
        });
        
        Button trustedReviewersBtn = new Button("Manage Trusted Reviewers");
        trustedReviewersBtn.setOnAction(e -> {
            new TrustedReviewersPage(StartCSE360.getDatabaseHelper(), user).show(primaryStage);
        });
        
        // NEW: Messaging Center button.
        Button messagingBtn = new Button("Messaging Center");
        messagingBtn.setOnAction(e -> {
            new MessagingPage(StartCSE360.getDatabaseHelper(), user).show(primaryStage);
        });
        
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(StartCSE360.getDatabaseHelper()).show(primaryStage);
        });

        layout.getChildren().addAll(adminLabel, manageQuestionsBtn, userMgmtBtn, trustedReviewersBtn, messagingBtn, logoutBtn);
        Scene adminScene = new Scene(layout, 800, 400);
        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin HomePage");
    }
}
