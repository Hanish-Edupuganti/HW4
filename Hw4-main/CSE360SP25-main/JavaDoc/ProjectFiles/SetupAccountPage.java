// File: SetupAccountPage.java

/**
 * <p>Title: SetupAccountPage</p>
 *
 * <p>Description: This class handles the account setup process for new users.
 * Users must enter a desired username, a secure password, and a valid invitation code to register.</p>
 * 
 * <p>It also provides real-time feedback on password strength using the PasswordEvaluator class.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @version 1.00 2025-04-01 Initial implementation
 */
public class SetupAccountPage {
    
    /**
     * DatabaseHelper instance for interacting with the database (e.g., user creation, validation).
     */
    private final DatabaseHelper databaseHelper;
    
    /**
     * Constructs a SetupAccountPage with the specified DatabaseHelper.
     *
     * @param databaseHelper The database helper used for account creation and verification.
     */
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the setup account form to the user.
     * <p>
     * This method would normally build the GUI using JavaFX components such as:
     * <ul>
     *   <li>TextField for the username</li>
     *   <li>PasswordField for password entry</li>
     *   <li>TextField for the invitation code</li>
     *   <li>Labels to display password strength criteria (color-coded)</li>
     *   <li>A 'Register' button to submit the data</li>
     *   <li>Validation and error feedback to the user</li>
     * </ul>
     * </p>
     * 
     * @param primaryStage The JavaFX primary stage used to display this UI page.
     */
    public void show(Stage primaryStage) {
        // Suggested implementation:
        // - VBox layout with padding and spacing
        // - Fields: usernameField, passwordField, inviteCodeField
        // - Password requirement feedback labels (green/red)
        // - Register button with event handler that:
        //   1) Validates invitation code
        //   2) Checks password rules via PasswordEvaluator
        //   3) Calls databaseHelper.createUser(...) if valid
        //   4) Displays success or error message
    }

    /**
     * Updates the feedback labels for the password requirements in real time.
     * <p>
     * This method is triggered as the user types their password, providing visual feedback
     * for whether each password rule (e.g., uppercase letter, numeric digit) has been met.
     * </p>
     *
     * @param password The password entered by the user.
     * @param labelUpperCase Label to indicate presence of an uppercase letter.
     * @param labelLowerCase Label to indicate presence of a lowercase letter.
     * @param labelNumeric Label to indicate presence of a numeric digit.
     * @param labelSpecial Label to indicate presence of a special character.
     * @param labelLength Label to indicate whether the password meets minimum length.
     */
    private void updatePasswordFeedback(
        String password,
        Label labelUpperCase,
        Label labelLowerCase,
        Label labelNumeric,
        Label labelSpecial,
        Label labelLength
    ) {
        // Evaluate password using the FSM-style evaluator
        PasswordEvaluator.evaluatePassword(password);
        
        // Update label color based on whether criteria are met
        labelUpperCase.setTextFill(PasswordEvaluator.foundUpperCase ? Color.GREEN : Color.RED);
        labelLowerCase.setTextFill(PasswordEvaluator.foundLowerCase ? Color.GREEN : Color.RED);
        labelNumeric.setTextFill(PasswordEvaluator.foundNumericDigit ? Color.GREEN : Color.RED);
        labelSpecial.setTextFill(PasswordEvaluator.foundSpecialChar ? Color.GREEN : Color.RED);
        labelLength.setTextFill(PasswordEvaluator.foundLongEnough ? Color.GREEN : Color.RED);
    }
}