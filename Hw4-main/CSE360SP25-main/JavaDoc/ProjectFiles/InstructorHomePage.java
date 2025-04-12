/**
 * The InstructorHomePage class represents the home page for instructors.
 * It provides options for instructors to review content, view scorecard parameters,
 * request admin actions, or log out of the system.
 */
package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InstructorHomePage {

    // Database helper instance for interacting with the database
    private DatabaseHelper dbHelper;

    // The currently logged-in instructor user
    private User currentUser;

    /**
     * Constructs an InstructorHomePage with the specified database helper and current user.
     *
     * @param dbHelper    The database helper used for database interactions.
     * @param currentUser The current user (instructor) logged in to the system.
     */
    public InstructorHomePage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the InstructorHomePage in the provided primary stage. 
     * This page provides buttons for various instructor actions.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Create a vertical layout with spacing
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // --- Buttons for Instructor Actions ---

        // Button to navigate to the content review page
        Button reviewContentBtn = new Button("Review All Content");
        reviewContentBtn.setOnAction(e -> {
            new ContentReviewPage(dbHelper, currentUser).show(primaryStage);
        });

        // Button to open the scorecard parameters page
        Button scorecardBtn = new Button("Reviewer Scorecard Parameters");
        scorecardBtn.setOnAction(e -> {
            new ScorecardParametersPage(dbHelper, currentUser).show(primaryStage);
        });

        // Button to open the request admin actions page
        Button requestAdminBtn = new Button("Request Admin Action");
        requestAdminBtn.setOnAction(e -> {
            new RequestsPage(dbHelper, currentUser).show(primaryStage);
        });

        // Button to log out and return to the login selection screen
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(dbHelper).show(primaryStage);
        });

        // Add all buttons to the layout
        layout.getChildren().addAll(reviewContentBtn, scorecardBtn, requestAdminBtn, logoutBtn);

        // Create and display the scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Instructor Home");
        primaryStage.show();
    }
}