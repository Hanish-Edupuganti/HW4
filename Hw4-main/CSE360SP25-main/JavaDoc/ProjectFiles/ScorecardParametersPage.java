// File: ScorecardParametersPage.java

/**
 * <p>Title: ScorecardParametersPage</p>
 * 
 * <p>Description: This class allows an instructor to set the scorecard parameters for the reviewer system, 
 * such as upvote weight, downvote weight, and rating weight. These parameters are saved to the database.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2025-04-01 Initial implementation
 */
public class ScorecardParametersPage {

    /**
     * The DatabaseHelper instance used for interacting with the database.
     */
    private DatabaseHelper dbHelper;

    /**
     * The current user who is logged into the application.
     */
    private User currentUser;

    /**
     * Constructs a ScorecardParametersPage with the specified DatabaseHelper and currentUser.
     *
     * @param dbHelper The DatabaseHelper instance used for database operations.
     * @param currentUser The current user who is logged into the system.
     */
    public ScorecardParametersPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the scorecard parameters page where the instructor can set the weight for upvotes, 
     * downvotes, and ratings for the scorecard system.
     * 
     * The page includes text fields to enter the weights, a save button to store the parameters in the database,
     * and a back button to return to the instructor's home page.
     *
     * @param primaryStage The primary stage where the scene will be set.
     */
    public void show(Stage primaryStage) {
        // Main layout container
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Title label
        Label title = new Label("Set Reviewer Scorecard Parameters");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Input field for upvote weight
        TextField upvoteField = new TextField();
        upvoteField.setPromptText("Upvote Weight");

        // Input field for downvote weight
        TextField downvoteField = new TextField();
        downvoteField.setPromptText("Downvote Weight");

        // Input field for rating weight
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating Weight");

        // Button to save entered parameters
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> {
            try {
                // Parse and validate input
                int upvoteWeight = Integer.parseInt(upvoteField.getText());
                int downvoteWeight = Integer.parseInt(downvoteField.getText());
                int ratingWeight = Integer.parseInt(ratingField.getText());

                // Save parameters to the database
                dbHelper.setScorecardParam("upvoteWeight", upvoteWeight);
                dbHelper.setScorecardParam("downvoteWeight", downvoteWeight);
                dbHelper.setScorecardParam("ratingWeight", ratingWeight);

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Parameters saved successfully.");
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                // Handle non-integer input
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid integer values for all fields.");
                alert.showAndWait();
            }
        });

        // Back button to return to instructor's home
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new InstructorHomePage(dbHelper, currentUser).show(primaryStage));

        // Add all UI elements to the layout
        layout.getChildren().addAll(title, upvoteField, downvoteField, ratingField, saveBtn, backBtn);

        // Setup scene and stage
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scorecard Parameters");
        primaryStage.show();
    }
}