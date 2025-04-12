/**
 * The AdminSetupPage class manages the setup process for creating an administrator account.
 * 
 * This is intended for initializing the system with admin credentials for the first user.
 */


/**
 * Handles the administrator setup process, allowing a user to create an admin account.
 */
public class AdminSetupPage {

    // Reference to the database helper for user registration and DB operations
    private final DatabaseHelper databaseHelper;

    /**
     * Constructs an AdminSetupPage with the provided database helper.
     *
     * @param databaseHelper The database helper to manage user registration.
     */
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the administrator setup page.
     *
     * @param primaryStage The primary stage where the setup form is displayed.
     */
    public void show(Stage primaryStage) {
        // --- UI Elements ---

        // Text field for entering admin username
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");  // Placeholder text
        userNameField.setMaxWidth(250);                       // Limit field width

        // Password field for entering admin password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");        // Placeholder text
        passwordField.setMaxWidth(250);                       // Limit field width

        // Button to initiate the admin setup process
        Button setupButton = new Button("Setup");

        // --- Event Handling ---

        // Define what happens when the setup button is clicked
        setupButton.setOnAction(a -> {
            // Retrieve input values from text fields
            String userName = userNameField.getText();
            String password = passwordField.getText();

            try {
                // Create a new admin user and register in the database
                User user = new User(userName, password, "admin");
                databaseHelper.register(user);

                System.out.println("Administrator setup completed.");

                // After successful setup, navigate to the welcome login page
                new WelcomeLoginPage(databaseHelper).show(primaryStage, user);

            } catch (SQLException e) {
                // If there's a DB error, print it to the error stream and stack trace
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // --- Layout & Scene Setup ---

        // VBox layout to vertically arrange form fields and the button
        VBox layout = new VBox(10, userNameField, passwordField, setupButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");  // Styling the layout

        // Create and apply the scene to the primary stage
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();  // Display the window
    }
}