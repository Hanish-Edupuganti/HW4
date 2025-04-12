// File: WelcomeLoginPage.java

/**
 * <p>Title: WelcomeLoginPage</p>
 * 
 * <p>Description: This class displays a welcome screen to the user after a successful login. 
 * Based on the roles associated with the user, it routes them to the appropriate home page 
 * (admin, instructor, staff, or general user). If the user has multiple roles, they are prompted 
 * to choose one. Admin users are also given an additional "Invite" button to invite new users.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @author 
 * 
 * @version 1.0
 */

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WelcomeLoginPage {

    /**
     * The database helper instance used for performing database operations.
     */
    private final DatabaseHelper databaseHelper;

    /**
     * Constructs the WelcomeLoginPage with a reference to the DatabaseHelper.
     * 
     * @param databaseHelper The helper object for DB connectivity and logic.
     */
    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the welcome screen with buttons to continue to the appropriate home page
     * based on the user's roles, or to quit the application.
     * 
     * @param primaryStage The JavaFX stage to display the scene.
     * @param user The user who has just logged in.
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Greeting message
        Label welcomeLabel = new Label("Welcome!!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to proceed to user's home page
        Button continueButton = new Button("Continue to your Page");
        continueButton.setOnAction(a -> {
            if (user.getRoles().size() > 1) {
                // Redirect to role selection page for multi-role users
                new RoleSelectionPage(user, databaseHelper).show(primaryStage);
            } else {
                // Direct to role-based home page
                String role = user.getRoles().get(0);
                switch (role.toLowerCase()) {
                    case "admin" -> new AdminHomePage().show(primaryStage, user);
                    case "instructor" -> new InstructorHomePage(databaseHelper, user).show(primaryStage);
                    case "staff" -> new StaffHomePage(databaseHelper, user).show(primaryStage);
                    default -> new UserHomePage().show(primaryStage, user);
                }
            }
        });

        // Button to close the application and database connection
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
            Platform.exit();
        });

        // Optional admin-only invitation functionality
        if (user.getRoles().contains("admin")) {
            Button inviteButton = new Button("Invite");
            inviteButton.setOnAction(a -> {
                new InvitationPage().show(databaseHelper, primaryStage);
            });
            layout.getChildren().add(inviteButton);
        }

        // Add UI components to layout
        layout.getChildren().addAll(welcomeLabel, continueButton, quitButton);

        // Show scene
        Scene welcomeScene = new Scene(layout, 800, 400);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
    }
}