package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RoleRequestPage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    public RoleRequestPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label label = new Label("Request to become a Reviewer");
        Button requestBtn = new Button("Request Reviewer Role");
        Label statusLabel = new Label();

        requestBtn.setOnAction(e -> {
            // For simplicity, immediately add the reviewer role.
            dbHelper.addRoleToUser(currentUser.getUserName(), "reviewer");
            statusLabel.setText("You are now a reviewer!");
            // Update currentUser object.
            currentUser.setRole(currentUser.getRole() + ",reviewer");
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new UserHomePage().show(primaryStage, currentUser));

        layout.getChildren().addAll(label, requestBtn, statusLabel, backBtn);
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Role Request");
        primaryStage.show();
    }
}
