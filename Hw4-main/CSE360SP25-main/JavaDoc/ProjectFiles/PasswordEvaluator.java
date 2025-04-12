/**
 * <p> Title: Directed Graph-translated Password Assessor. </p>
 * 
 * <p> Description: A demonstration of the mechanical translation of a Directed Graph 
 * diagram into an executable Java program using the Password Evaluator Directed Graph. 
 * The code's detailed design is based on a while loop with a cascade of if statements.</p>
 * 
 * <p> Copyright: Lynn Robert Carter ©️ 2022 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2025-01-22	Changes to the special characters and ERROR outputs.
 */
public class PasswordEvaluator {

    /** Stores an error message when validation fails */
    public static String passwordErrorMessage = "";

    /** The raw password input being processed */
    public static String passwordInput = "";

    /** Index of the character where an error occurred */
    public static int passwordIndexofError = -1;

    // Flags to indicate satisfaction of various password requirements
    public static boolean foundUpperCase = false;
    public static boolean foundLowerCase = false;
    public static boolean foundNumericDigit = false;
    public static boolean foundSpecialChar = false;
    public static boolean foundLongEnough = false;

    // Internal state variables used during evaluation
    private static String inputLine = "";           // Entire input line
    private static char currentChar;                // Current character being evaluated
    private static int currentCharNdx;              // Index of the current character
    private static boolean running;                 // Control flag for the FSM loop
    private static boolean otherChar;               // Set to true if an invalid character is found

    /**
     * Displays the current state of the input and highlights the character being evaluated.
     * Used when an error is encountered.
     */
    private static void displayInputState() {
        System.out.println(inputLine); // Display full input
        System.out.println(inputLine.substring(0, currentCharNdx) + "?"); // Mark the error position
        System.out.println("The password size: " + inputLine.length() +
                "  |  The currentCharNdx: " + currentCharNdx +
                "  |  The currentChar: \"" + currentChar + "\"");
    }

    /**
     * Evaluates the password using a finite state machine modeled from a directed graph.
     * Returns an empty string if the password is valid; otherwise, returns an error message.
     *
     * @param input The password string to validate
     * @return An empty string if valid; otherwise, a detailed error message
     */
    public static String evaluatePassword(String input) {
        // ---------- Initialization ----------
        passwordErrorMessage = "";
        passwordIndexofError = 0;
        inputLine = input;
        currentCharNdx = 0;

        if (input.length() <= 0) return "* ERROR * The password is empty!";

        // Grab the first character
        currentChar = input.charAt(0);

        // Reset all validation flags
        passwordInput = input;
        foundUpperCase = false;
        foundLowerCase = false;
        foundNumericDigit = false;
        foundSpecialChar = false;
        foundLongEnough = false;
        running = true;
        otherChar = false;

        // ---------- Main Loop ----------
        while (running) {
            displayInputState(); // Show current state for debugging

            // Check for upper case character
            if (currentChar >= 'A' && currentChar <= 'Z') {
                System.out.println("Upper case letter found");
                foundUpperCase = true;
            }
            // Check for lower case character
            else if (currentChar >= 'a' && currentChar <= 'z') {
                System.out.println("Lower case letter found");
                foundLowerCase = true;
            }
            // Check for numeric digit
            else if (currentChar >= '0' && currentChar <= '9') {
                System.out.println("Digit found");
                foundNumericDigit = true;
            }
            // Check for accepted special characters
            else if ("~`!@#$%^&*()_-+{}[]|:,.?/".indexOf(currentChar) >= 0) {
                System.out.println("Special character found");
                foundSpecialChar = true;
            }
            // If character is not valid, exit with error
            else {
                passwordIndexofError = currentCharNdx;
                otherChar = true;
                return "* ERROR * An invalid character has been found!";
            }

            // Check length condition (must be at least 8 characters)
            if (currentCharNdx >= 7) {
                System.out.println("At least 8 characters found");
                foundLongEnough = true;
            }

            // Advance to next character or exit loop
            currentCharNdx++;
            if (currentCharNdx >= inputLine.length()) {
                running = false; // End of input
            } else {
                currentChar = input.charAt(currentCharNdx);
            }

            System.out.println(); // Spacer for clarity
        }

        // ---------- Final Validation ----------

        // Build error message if any condition is not met
        String errMessage = "* ERROR * ";
        if (!foundUpperCase) errMessage += "Upper case; ";
        if (!foundLowerCase) errMessage += "Lower case; ";
        if (!foundNumericDigit) errMessage += "Numeric digits; ";
        if (!foundSpecialChar) errMessage += "Special character; ";
        if (!foundLongEnough) errMessage += "Long Enough; ";
        if (otherChar) errMessage += "Invalid Character; ";

        // Return empty string if all conditions passed
        if (errMessage.equals("* ERROR * ")) return "";

        // Otherwise, return detailed error message
        passwordIndexofError = currentCharNdx;
        return errMessage + "conditions were not satisfied";
    }
}