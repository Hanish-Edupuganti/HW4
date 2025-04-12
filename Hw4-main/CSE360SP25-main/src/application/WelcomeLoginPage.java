package application;

import databasePart1.DatabaseHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeLoginPage {
    
    private final DatabaseHelper databaseHelper;

    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        Label welcomeLabel = new Label("Welcome!!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Button continueButton = new Button("Continue to your Page");
        continueButton.setOnAction(a -> {
            if (user.getRoles().size() > 1) {
                new RoleSelectionPage(user, databaseHelper).show(primaryStage);
            } else {
                String r = user.getRoles().get(0);
                if (r.equalsIgnoreCase("admin")) {
                    new AdminHomePage().show(primaryStage, user);
                } else if (r.equalsIgnoreCase("instructor")) {
                    new InstructorHomePage(databaseHelper, user).show(primaryStage);
                } else if (r.equalsIgnoreCase("staff")) {
                    new StaffHomePage(databaseHelper, user).show(primaryStage);
                } else {
                    new UserHomePage().show(primaryStage, user);
                }
            }
        });
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
            Platform.exit();
        });
        if (user.getRoles().contains("admin")) {
            Button inviteButton = new Button("Invite");
            inviteButton.setOnAction(a -> {
                new InvitationPage().show(databaseHelper, primaryStage);
            });
            layout.getChildren().add(inviteButton);
        }
        layout.getChildren().addAll(welcomeLabel, continueButton, quitButton);
        Scene welcomeScene = new Scene(layout, 800, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
    }
}
