// File: UserHomePage.java

/**
 * <p>Title: UserHomePage</p>
 * 
 * <p>Description: This class represents the home page for a standard user.
 * It provides navigation options for users to access the question page, request a reviewer role,
 * use the messaging center, or log out of the application.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @version 1.00	2025-04-01 Initial implementation
 */
public class UserHomePage {

    /**
     * Displays the home page for a standard user.
     * 
     * The page provides the following functionality:
     * <ul>
     *     <li>Navigate to the Questions Page to view, search, or manage questions.</li>
     *     <li>Request the "reviewer" role if the user wants to participate in peer review.</li>
     *     <li>Access the Messaging Center for private communication with other users.</li>
     *     <li>Logout and return to the login/setup selection screen.</li>
     * </ul>
     *
     * @param primaryStage The main stage used for rendering the scene.
     * @param user The current user that is logged into the system.
     */
    public void show(Stage primaryStage, User user) {
        VBox layout = new VBox(10);  // Layout with vertical spacing
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Greeting label that displays the current user's name
        Label userLabel = new Label("Hello, " + user.getUserName() + "!");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to open the Questions Page
        Button manageQuestionsBtn = new Button("Questions Page");
        manageQuestionsBtn.setOnAction(e -> {
            Questions questions = StartCSE360.getQuestions();    // DB-backed questions manager
            Answers answers = StartCSE360.getAnswers();          // DB-backed answers manager
            new QuestionsPage(questions, answers, user).show(primaryStage);  // Navigate to Questions Page
        });
        
        // Button to request reviewer role (adds "reviewer" to user's role list)
        Button requestReviewerBtn = new Button("Request Reviewer Role");
        requestReviewerBtn.setOnAction(e -> {
            new RoleRequestPage(StartCSE360.getDatabaseHelper(), user).show(primaryStage);
        });
        
        // NEW: Button to access the messaging center (in-app private messaging)
        Button messagingBtn = new Button("Messaging Center");
        messagingBtn.setOnAction(e -> {
            new MessagingPage(StartCSE360.getDatabaseHelper(), user).show(primaryStage);
        });

        // Button to log out and return to the login/setup selection screen
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(StartCSE360.getDatabaseHelper()).show(primaryStage);
        });

        // Add all components to the layout
        layout.getChildren().addAll(userLabel, manageQuestionsBtn, requestReviewerBtn, messagingBtn, logoutBtn);

        // Create and display the scene
        Scene userScene = new Scene(layout, 800, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("HomePage");
    }
}