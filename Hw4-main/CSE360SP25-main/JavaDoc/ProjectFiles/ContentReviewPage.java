/**
 * The ContentReviewPage class represents the UI for reviewing content within the application.
 * 
 * It allows users with appropriate roles (admin, instructor, or staff) to view questions,
 * answers, and private messages.
 */

/**
 * UI for content review functionality, accessible by users with review permissions.
 */
public class ContentReviewPage {

    private DatabaseHelper dbHelper;  // Reference to the database helper
    private User currentUser;         // Currently logged-in user

    /**
     * Constructs a ContentReviewPage with a database helper and the current user.
     *
     * @param dbHelper    The database helper instance.
     * @param currentUser The user currently logged in.
     */
    public ContentReviewPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the content review page on the given stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Create the layout and spacing between elements
        VBox layout = new VBox(10);

        // Title label
        Label title = new Label("Content Review");

        // Button to view all questions (functionality to be implemented)
        Button viewQuestionsBtn = new Button("View All Questions");
        viewQuestionsBtn.setOnAction(e -> {
            // TODO: Implement logic to fetch and display all questions
        });

        // Button to view all answers (functionality to be implemented)
        Button viewAnswersBtn = new Button("View All Answers");
        viewAnswersBtn.setOnAction(e -> {
            // TODO: Implement logic to fetch and display all answers
        });

        // Button to view all private messages (functionality to be implemented)
        Button viewMessagesBtn = new Button("View All Private Messages");
        viewMessagesBtn.setOnAction(e -> {
            // TODO: Implement logic to fetch and display all messages
        });

        // Back button: navigates the user to their respective home page based on role
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            if (currentUser.getRoles().contains("admin")) {
                new AdminHomePage().show(primaryStage, currentUser);
            } else if (currentUser.getRoles().contains("instructor")) {
                new InstructorHomePage(dbHelper, currentUser).show(primaryStage);
            } else if (currentUser.getRoles().contains("staff")) {
                new StaffHomePage(dbHelper, currentUser).show(primaryStage);
            }
        });

        // Add all UI components to the layout
        layout.getChildren().addAll(title, viewQuestionsBtn, viewAnswersBtn, viewMessagesBtn, backBtn);

        // Create and set the scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Content Review");
        primaryStage.show();
    }
}