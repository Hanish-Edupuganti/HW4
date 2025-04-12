// File: RoleRequestPage.java

/**
 * A UI page that allows a user to request a reviewer role.
 * This page provides a button for users to request the "reviewer" role,
 * which is immediately granted upon request.
 */
public class RoleRequestPage {

    /** Database helper used to interact with the database */
    private DatabaseHelper dbHelper;

    /** The user who is currently logged in and requesting the role */
    private User currentUser;

    /**
     * Constructor to initialize the RoleRequestPage with the given database helper and current user.
     *
     * @param dbHelper The database helper for handling database operations like adding roles.
     * @param currentUser The currently logged-in user requesting the role.
     */
    public RoleRequestPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the role request page in the provided stage.
     * Allows the user to request the "reviewer" role.
     *
     * @param primaryStage The stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Create layout container
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Title label
        Label title = new Label("Request Reviewer Role");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Description label
        Label description = new Label("Click the button below to request access to the reviewer role.");
        description.setWrapText(true);

        // Button to request the reviewer role
        Button requestRoleButton = new Button("Request Reviewer Role");
        requestRoleButton.setOnAction(e -> {
            // Add the reviewer role to the user
            dbHelper.addRoleToUser(currentUser.getUserName(), "reviewer");

            // Show confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                "You have been granted the reviewer role!", ButtonType.OK);
            alert.showAndWait();

            // Optionally redirect the user to another page (e.g., user home)
            new UserHomePage().show(primaryStage, currentUser);
        });

        // Back button to return to user home
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new UserHomePage().show(primaryStage, currentUser);
        });

        // Add all components to layout
        layout.getChildren().addAll(title, description, requestRoleButton, backButton);

        // Set scene and display it
        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Role Request");
        primaryStage.show();
    }
}