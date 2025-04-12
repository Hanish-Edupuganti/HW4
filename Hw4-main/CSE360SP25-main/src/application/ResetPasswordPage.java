package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResetPasswordPage {

    private databasePart1.DatabaseHelper dbHelper;

    public ResetPasswordPage(databasePart1.DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username to reset password");

        Button resetBtn = new Button("Reset Password");
        Label infoLabel = new Label();

        resetBtn.setOnAction(e -> {
            String username = usernameField.getText();
            // Generate a temporary password (this is a simple example; use a secure method in production)
            String tempPassword = "TEMP" + System.currentTimeMillis();
            boolean success = dbHelper.resetPassword(username, tempPassword);
            if (success) {
                infoLabel.setText("Temporary password for " + username + ": " + tempPassword);
            } else {
                infoLabel.setText("Failed to reset password for " + username);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new AdminHomePage().show(primaryStage, new User("admin", "", "admin")));

        layout.getChildren().addAll(new Label("Reset Password"), usernameField, resetBtn, infoLabel, backBtn);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset Password");
        primaryStage.show();
    }
}
