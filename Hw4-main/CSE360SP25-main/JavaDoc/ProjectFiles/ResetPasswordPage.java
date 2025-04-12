// File: ResetPasswordPage.java

/**
 * A UI page that allows the administrator to reset a user's password.
 * This page provides functionality for entering a username and generating a temporary password.
 * The generated temporary password is then sent to the user.
 */
public class ResetPasswordPage {

    private DatabaseHelper dbHelper;

    /**
     * Constructor to initialize the ResetPasswordPage with the given database helper.
     *
     * @param dbHelper The database helper for handling database operations like resetting passwords.
     */
    public ResetPasswordPage(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Displays the reset password page in the provided stage.
     * Allows the administrator to enter a username and reset the password.
     *
     * @param primaryStage The stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Title label
        Label titleLabel = new Label("Reset User Password");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Input field for username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        // Label to display the new temporary password
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-font-style: italic;");

        // Button to trigger password reset
        Button resetBtn = new Button("Reset Password");
        resetBtn.setOnAction(e -> {
            String username = usernameField.getText();
            if (username.isBlank()) {
                resultLabel.setText("Please enter a valid username.");
                return;
            }

            try {
                // Generate and set a temporary password via dbHelper
                String tempPassword = dbHelper.resetPassword(username);
                resultLabel.setText("Temporary password sent to user: " + tempPassword);
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Back button to return to admin home page
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            // Example fallback behavior; may be redirected to AdminHomePage
            new AdminHomePage().show(primaryStage, null);
        });

        layout.getChildren().addAll(titleLabel, usernameField, resetBtn, resultLabel, backBtn);
        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reset Password");
        primaryStage.show();
    }
}