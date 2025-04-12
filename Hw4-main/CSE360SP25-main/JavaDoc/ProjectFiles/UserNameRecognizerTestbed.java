// File: UserNameRecognizerTestbed.java



/**

 * <p>Title: UserNameRecognizerTestbed</p>

 * 

 * <p>Description: This is a console-based testbed application that demonstrates the use of 

 * the UserNameRecognizer class. Users are prompted to enter a username. The FSM logic is 

 * applied to validate the input and appropriate feedback is provided in case of errors.</p>

 * 

 * <p>Copyright: Lynn Robert Carter ©️ 2024</p>

 * 

 * @version 1.00 2024-09-13 Initial console implementation of the UserName Recognizer testbed.

 */



import java.util.Scanner;



public class UserNameRecognizerTestbed {

	

	/** 

	 * The current input line being processed.

	 */

	static String inputLine;



	/**

	 * The main method acts as the driver for the UserNameRecognizer testbed.

	 * It accepts input from the console, runs the FSM, and displays results or error messages.

	 */

	public static void main(String[] args) {



		System.out.println("Welcome to the UserName Recognizer Testbed\n");

        System.out.println("Please enter a UserName or an empty line to stop.");



		// Scanner to read input from the console

		Scanner keyboard = new Scanner(System.in);



		// Process each line of input

		while (keyboard.hasNextLine()) {

			inputLine = keyboard.nextLine();



			// Exit on empty input

			if (inputLine.length() == 0) {

				System.out.println("\n*** Empty input line detected, the loop stops.");

				keyboard.close();

				System.exit(0);

			}



			// Validate input using FSM

			String errMessage = UserNameRecognizer.checkForValidUserName(inputLine);



			// If there's an error, display it with location

			if (!errMessage.isEmpty()) {

				System.out.println(errMessage);



				// Defensive check (should never fail)

				if (UserNameRecognizer.userNameRecognizerIndexofError <= -1)

					return;



				// Show the input with a pointer (arrow) at the point of error

				System.out.println(inputLine);

				System.out.println(inputLine.substring(0, 

					UserNameRecognizer.userNameRecognizerIndexofError) + "\u21EB");

			}

			else {

				System.out.println("Success! The UserName is valid.");

			}



			// Prompt for next input

	        System.out.println("\nPlease enter a UserName or an empty line to stop.");

		}

	}

}