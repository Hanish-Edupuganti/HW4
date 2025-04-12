// File: UserInterface.java

/**
 * <p>Title: UserInterface</p>
 * 
 * <p>Description: A JavaFX-based user interface for a password evaluation testbed.
 * This class creates a UI to test and evaluate password strength based on predefined criteria
 * like having uppercase letters, lowercase letters, numeric digits, special characters, and length.</p>
 * 
 * <p>Copyright: ©️ 2022 Lynn Robert Carter</p>
 * 
 * @version 1.00 2022-02-21 The JavaFX-based GUI for the implementation of a testbed.
 * @author Lynn Robert Carter
 */
public class UserInterface {

	/********************************
	 * Attributes
	 ********************************/

	// Title label for the application
	private Label label_ApplicationTitle = new Label("Password Evaluation Testbed");

	// Label and text field for user input
	private Label label_Password = new Label("Enter the password here");
	private TextField text_Password = new TextField();

	// Feedback labels for user input validation
	private Label label_errPassword = new Label("");	
	private Label noInputFound = new Label("");

	// TextFlow and segments for showing password errors visually
	private TextFlow errPassword;
	private Text errPasswordPart1 = new Text();
	private Text errPasswordPart2 = new Text(); // The red arrow (↑)
	private Label errPasswordPart3 = new Label(""); // Extra description under the arrow

	// Message for password validation status
	private Label validPassword = new Label("");

	// Labels for password rules
	private Label label_Requirements = new Label("A valid password must satisfy the following requirements:");
	private Label label_UpperCase = new Label("At least one upper case letter");
	private Label label_LowerCase = new Label("At least one lower case letter");
	private Label label_NumericDigit = new Label("At least one numeric digit");
	private Label label_SpecialChar = new Label("At least one special character");
	private Label label_LongEnough = new Label("At least eight characters");

	/********************************
	 * Constructor
	 ********************************/

	/**
	 * Initializes the graphical user interface (GUI) components and sets up event listeners.
	 *
	 * @param theRoot The root container to which UI elements are added.
	 */
	public UserInterface(Pane theRoot) {

		// --- Set up title and password input section
		setupLabelUI(label_ApplicationTitle, "Arial", 24, PasswordEvaluationGUITestbed.WINDOW_WIDTH, Pos.CENTER, 0, 10);
		setupLabelUI(label_Password, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 50);
		setupTextUI(text_Password, "Arial", 18, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 20, Pos.BASELINE_LEFT, 10, 70, true);
		text_Password.textProperty().addListener((obs, oldVal, newVal) -> setPassword());

		// --- Feedback for no input
		noInputFound.setTextFill(Color.RED);
		setupLabelUI(noInputFound, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 110);		

		// --- General error label
		label_errPassword.setTextFill(Color.RED);
		setupLabelUI(label_errPassword, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 160, Pos.BASELINE_LEFT, 22, 126);		

		// --- Error arrow indicators
		errPasswordPart1.setFill(Color.BLACK);
		errPasswordPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
		errPasswordPart2.setFill(Color.RED);
		errPasswordPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		errPassword = new TextFlow(errPasswordPart1, errPasswordPart2);
		errPassword.setMinWidth(PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10); 
		errPassword.setLayoutX(22);  
		errPassword.setLayoutY(100);

		setupLabelUI(errPasswordPart3, "Arial", 14, 200, Pos.BASELINE_LEFT, 20, 125);	

		// --- Password rules display
		setupLabelUI(label_Requirements, "Arial", 16, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 200);
		setupLabelUI(label_UpperCase, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 230);
		setupLabelUI(label_LowerCase, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 260);
		setupLabelUI(label_NumericDigit, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 290);
		setupLabelUI(label_SpecialChar, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 320);
		setupLabelUI(label_LongEnough, "Arial", 14, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 30, 350);

		// Initially set all rule labels to red
		resetAssessments();

		// --- Final result label
		validPassword.setTextFill(Color.GREEN);
		setupLabelUI(validPassword, "Arial", 18, PasswordEvaluationGUITestbed.WINDOW_WIDTH - 160, Pos.BASELINE_LEFT, 10, 380);				

		// --- Add all UI elements to the layout
		theRoot.getChildren().addAll(label_ApplicationTitle, label_Password, text_Password, 
			noInputFound, label_errPassword, errPassword, errPasswordPart3, validPassword,
			label_Requirements, label_UpperCase, label_LowerCase, label_NumericDigit,
			label_SpecialChar, label_LongEnough);
	}

	/********************************
	 * UI Setup Helpers
	 ********************************/

	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}

	/********************************
	 * Core Evaluation Methods
	 ********************************/

	/**
	 * Clears the UI and evaluates the password whenever the user types.
	 */
	private void setPassword() {
		label_errPassword.setText("");
		noInputFound.setText("");
		errPasswordPart1.setText("");
		errPasswordPart2.setText("");
		validPassword.setText("");
		resetAssessments();
		performEvaluation();			
	}

	/**
	 * Performs the actual password evaluation and updates the UI accordingly.
	 */
	private void performEvaluation() {
		String inputText = text_Password.getText();
		
		if (inputText.isEmpty()) {
		    noInputFound.setText("No input text found!");
		} else {
			String errMessage = PasswordEvaluator.evaluatePassword(inputText);
			updateFlags();

			if (!errMessage.isEmpty()) {
				label_errPassword.setText(PasswordEvaluator.passwordErrorMessage);

				if (PasswordEvaluator.passwordIndexofError <= -1) return;

				String input = PasswordEvaluator.passwordInput;
				errPasswordPart1.setText(input.substring(0, PasswordEvaluator.passwordIndexofError));
				errPasswordPart2.setText("\u21EB"); // Upward arrow
				errPasswordPart3.setText("The red arrow points at the character causing the error!");

				validPassword.setTextFill(Color.RED);
				validPassword.setText("Failure! The password is not valid.");
			} else {
				validPassword.setTextFill(Color.GREEN);
				validPassword.setText("Success! The password is valid.");
			}
		}
	}

	/********************************
	 * UI State Reset / Flag Update
	 ********************************/

	/**
	 * Resets the rule labels to red (default state).
	 */
	public void resetAssessments() {
		label_UpperCase.setTextFill(Color.RED);
		label_LowerCase.setTextFill(Color.RED);
		label_NumericDigit.setTextFill(Color.RED);
		label_SpecialChar.setTextFill(Color.RED);
		label_LongEnough.setTextFill(Color.RED);
	}

	/**
	 * Turns rule labels green if the corresponding password check passes.
	 */
	public void updateFlags() {
		if (PasswordEvaluator.passwordFlagUpperCase) label_UpperCase.setTextFill(Color.GREEN);
		if (PasswordEvaluator.passwordFlagLowerCase) label_LowerCase.setTextFill(Color.GREEN);
		if (PasswordEvaluator.passwordFlagNumericDigit) label_NumericDigit.setTextFill(Color.GREEN);
		if (PasswordEvaluator.passwordFlagSpecialChar) label_SpecialChar.setTextFill(Color.GREEN);
		if (PasswordEvaluator.passwordFlagLongEnough) label_LongEnough.setTextFill(Color.GREEN);
	}
}