/**
 * The FirstPage class represents the initial screen displayed to the first user.
 * It prompts the user to set up administrator access and navigate to the setup screen.
 */


public class FirstPage {
    
    // Reference to the DatabaseHelper for database interactions
    private final DatabaseHelper databaseHelper;
    
    /**
     * Constructs a FirstPage instance with the specified database helper.
     *
     * @param databaseHelper The database helper used for database interactions.
     */
    public FirstPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Static variable that allows the setup of a custom behavior for the "Continue" button press.
     * This is especially useful for unit testing or mocking UI navigation.
     */
    public static Runnable onContinuePressed = null;

    /**
     * Displays the FirstPage in the provided primary stage. 
     * This page prompts the user to set up administrator access.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Create a vertical box layout with spacing
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label with welcome and instructions for the first user
        Label userLabel = new Label("Hello..You are the first person here.\nPlease select continue to setup administrator access");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // "Continue" button for proceeding to admin setup
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(a -> {
            // If a custom test handler is set, execute it; otherwise, launch setup page
            if (onContinuePressed != null) {
                onContinuePressed.run();  // Useful for testing without launching full UI
            } else {
                new AdminSetupPage(databaseHelper).show(primaryStage); // Default behavior
            }
        });

        // Add UI elements to the layout
        layout.getChildren().addAll(userLabel, continueButton);

        // Create and assign the scene to the stage
        Scene firstPageScene = new Scene(layout, 800, 400);
        primaryStage.setScene(firstPageScene);
        primaryStage.setTitle("First Page");
        primaryStage.show();
    }
}