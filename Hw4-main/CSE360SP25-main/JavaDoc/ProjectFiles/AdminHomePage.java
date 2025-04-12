/**
 * This class represents the Admin Home Page, providing navigation options for administrators.
 * 
 * It includes functionalities to manage questions, users, trusted reviewers, and messaging.
 */


/**
 * Represents the Admin Home Page UI and its associated actions.
 */
public class AdminHomePage {

    /**
     * Displays the Admin Home Page with various management options.
     *
     * @param primaryStage The main stage for the application UI.
     * @param user The current logged-in administrator user.
     */
    public void show(Stage primaryStage, User user) {
        // Create a vertical layout container with spacing between components
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label displaying a welcome message to the admin
        Label adminLabel = new Label("Hello, " + user.getUserName() + "!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to navigate to the Question Management page
        Button manageQuestionsBtn = new Button("Manage Questions");
        manageQuestionsBtn.setOnAction(e -> {
            Questions questions = StartCSE360.getQuestions();
            Answers answers = StartCSE360.getAnswers();
            new QuestionsPage(questions, answers, user).show(primaryStage);
        });

        // Button to navigate to the User Management page
        Button userMgmtBtn = new Button("Manage Users");
        userMgmtBtn.setOnAction(e -> {
            new UserManagementPage(StartCSE360.getDatabaseHelper()).show(primaryStage);
        });

        // Button to navigate to the Trusted Reviewers management page
        Button trustedReviewersBtn = new Button("Manage Trusted Reviewers");
        trustedReviewersBtn.setOnAction(e -> {
            new TrustedReviewersPage(StartCSE360.getDatabaseHelper(), user).show(primaryStage);
        });

        // Button to open the Messaging Center
        Button messagingBtn = new Button("Messaging Center");
        messagingBtn.setOnAction(e -> {
            new MessagingPage(StartCSE360.getDatabaseHelper(), user).show(primaryStage);
        });

        // Button to log out and return to the login selection screen
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(StartCSE360.getDatabaseHelper()).show(primaryStage);
        });

        // Add all components to the layout
        layout.getChildren().addAll(
            adminLabel,