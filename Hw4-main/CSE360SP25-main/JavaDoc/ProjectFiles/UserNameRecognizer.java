// File: UserNameRecognizer.java

/**
 * <p>Title: FSM-translated UserNameRecognizer</p>
 * 
 * <p>Description: This class demonstrates how a Finite State Machine (FSM) diagram can be translated 
 * into an executable Java program for username validation. It processes usernames character-by-character 
 * and checks for validity based on predefined formatting rules.</p>
 * 
 * <p>Copyright: Lynn Robert Carter ©️ 2024</p>
 * 
 * @version 1.00 2024-09-13 Initial baseline derived from the Even Recognizer.
 * @version 1.01 2024-09-17 Fixed UNChar bug, improved error reporting, enhanced comments.
 */

public class UserNameRecognizer {

	// --- Public output fields ---
	public static String userNameRecognizerErrorMessage = "";   // Error message generated if input is invalid
	public static String userNameRecognizerInput = "";          // The input string being evaluated
	public static int userNameRecognizerIndexofError = -1;      // Index at which an error occurred (if any)

	// --- Internal FSM state variables ---
	private static int state = 0;             // Current state of the FSM
	private static int nextState = 0;         // Next state to transition into
	private static boolean finalState = false;// True if the final state is acceptable
	private static String inputLine = "";     // Input string being processed
	private static char currentChar;          // Current character being evaluated
	private static int currentCharNdx;        // Index of the current character
	private static boolean running;           // Indicates whether the FSM is still running
	private static int userNameSize = 0;      // Total number of valid characters processed

	/**
	 * Prints the current FSM state and transition for debugging purposes.
	 */
	private static void displayDebuggingInfo() {
		if (currentCharNdx >= inputLine.length()) {
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "None");
		} else {
			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
				nextState + "     " + userNameSize);
		}
	}

	/**
	 * Advances the FSM to the next character in the input.
	 * Ends execution when the end of input is reached.
	 */
	private static void moveToNextCharacter() {
		currentCharNdx++;
		if (currentCharNdx < inputLine.length())
			currentChar = inputLine.charAt(currentCharNdx);
		else {
			currentChar = ' ';
			running = false;
		}
	}

	/**
	 * Runs the FSM to check if the input is a valid username.
	 * 
	 * Valid usernames:
	 * - Must start with a letter (A-Z or a-z).
	 * - May contain letters, digits, and the special characters '.', '_', or '-'.
	 * - Cannot end in a special character.
	 * - Must be between 4 and 16 characters in length.
	 *
	 * @param input The username string to validate.
	 * @return An empty string if valid, otherwise a descriptive error message.
	 */
	public static String checkForValidUserName(String input) {
		if (input.length() <= 0) {
			userNameRecognizerIndexofError = 0;
			return "\n** ERROR ** The input is empty";
		}

		// Initialize FSM variables
		state = 0;
		inputLine = input;
		currentCharNdx = 0;
		currentChar = input.charAt(0);
		userNameRecognizerInput = input;
		running = true;
		nextState = -1;
		userNameSize = 0;

		System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");

		// FSM Execution Loop
		while (running) {
			switch (state) {
				case 0: // Start: First character must be a letter
					if (Character.isLetter(currentChar)) {
						nextState = 1;
						userNameSize++;
					} else {
						running = false;
					}
					break;

				case 1: // Accepting State: Allow letters, digits, or special characters
					if (Character.isLetterOrDigit(currentChar)) {
						nextState = 1;
						userNameSize++;
					} else if (currentChar == '.' || currentChar == '_' || currentChar == '-') {
						nextState = 2;
						userNameSize++;
					} else {
						running = false;
					}

					if (userNameSize > 16)
						running = false;
					break;

				case 2: // After special character: Next must be a letter or digit
					if (Character.isLetterOrDigit(currentChar)) {
						nextState = 1;
						userNameSize++;
					} else {
						running = false;
					}

					if (userNameSize > 16)
						running = false;
					break;
			}

			if (running) {
				displayDebuggingInfo();
				moveToNextCharacter();
				state = nextState;
				if (state == 1) finalState = true;
				nextState = -1;
			}
		}

		// Show last transition info
		displayDebuggingInfo();
		System.out.println("The loop has ended.");

		// Determine result based on final state and position
		userNameRecognizerIndexofError = currentCharNdx;
		userNameRecognizerErrorMessage = "\n** ERROR ** ";

		switch (state) {
			case 0:
				userNameRecognizerErrorMessage += "A UserName must start with A-Z or a-z.\n";
				return userNameRecognizerErrorMessage;

			case 1:
				if (userNameSize < 4) {
					userNameRecognizerErrorMessage += "A UserName must have at least 4 characters.\n";
					return userNameRecognizerErrorMessage;
				} else if (userNameSize > 16) {
					userNameRecognizerErrorMessage += "A UserName must have no more than 16 characters.\n";
					return userNameRecognizerErrorMessage;
				} else if (currentCharNdx < input.length()) {
					userNameRecognizerErrorMessage += "A UserName may only contain A-Z, a-z, 0-9, '.', '_', or '-'.\n";
					return userNameRecognizerErrorMessage;
				} else {
					// Valid username
					userNameRecognizerIndexofError = -1;
					userNameRecognizerErrorMessage = "";
					return "";
				}

			case 2:
				userNameRecognizerErrorMessage += "A UserName character after '.', '-', or '_' must be A-Z, a-z, or 0-9.\n";
				return userNameRecognizerErrorMessage;

			default:
				return "";
		}
	}
}