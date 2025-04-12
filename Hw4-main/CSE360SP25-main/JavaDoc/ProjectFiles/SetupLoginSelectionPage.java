// File: SetupLoginSelectionPage.java

/**
 * <p>Title: SetupLoginSelectionPage</p>
 * 
 * <p>Description: This class represents a user interface page that provides users 
 * with the option to either set up a new account or log into an existing one. 
 * It is typically the first screen shown to users after launching the application, 
 * unless they are an admin or returning user.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @version 1.00 2025-04-01 Initial implementation
 */
public class SetupLoginSelectionPage {

    /**
     * DatabaseHelper instance used to perform necessary database interactions
     * for both setup and login processes.
     */
    private final DatabaseHelper databaseHelper;

    /**
     * Constructs the SetupLoginSelectionPage with the specified DatabaseHelper.
     *
     * @param databaseHelper The database helper to be used for account setup or login processes.
     */
    public SetupLoginSelectionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the selection page for setup or login options.
     * <p>
     * This method creates a simple JavaFX layout containing two buttons:
     * <ul>
     *   <li>"SetUp" — Navigates to the SetupAccountPage</li>
     *   <li>"Login" — Navigates to the UserLoginPage</li>
     * </ul>
     * The layout is placed in the center of the window with padding.
     * </p>
     *
     * @param primaryStage The primary stage where the scene is to be displayed.
     */
    public void show(Stage primaryStage) {
        // Buttons to select either account setup or login
        Button setupButton = new Button("SetUp");
        Button loginButton = new Button("Login");

        // Define behavior for each button
        setupButton.setOnAction(a -> {
            new SetupAccountPage(databaseHelper).show(primaryStage);
        });

        loginButton.setOnAction(a -> {
            new UserLoginPage(databaseHelper).show(primaryStage);
        });

        // Layout setup
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(setupButton, loginButton);

        // Configure and display the scene
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}