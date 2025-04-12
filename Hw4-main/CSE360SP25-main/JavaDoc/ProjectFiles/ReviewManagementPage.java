// File: ReviewManagementPage.java

/**
 * <p>Title: ReviewManagementPage</p>
 * 
 * <p>Description: This class is responsible for managing the review system for an answer, 
 * allowing reviewers to add reviews with ratings for an answer and view existing reviews.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00 2025-04-01 Initial implementation
 */
public class ReviewManagementPage {

    /** Database helper for database operations */
    private DatabaseHelper dbHelper;

    /** The answer being reviewed */
    private Answer answer;

    /** The currently logged-in user */
    private User currentUser;

    /**
     * Constructs a ReviewManagementPage with the given dependencies.
     *
     * @param dbHelper    The DatabaseHelper instance for DB interactions.
     * @param answer      The answer that is being reviewed.
     * @param currentUser The user currently logged in.
     */
    public ReviewManagementPage(DatabaseHelper dbHelper, Answer answer, User currentUser) {
        this.dbHelper = dbHelper;
        this.answer = answer;
        this.currentUser = currentUser;
    }

    /**
     * Displays the review management interface for a specific answer.
     * This page allows users to see all existing reviews and, if they are reviewers,
     * to add a new review with a rating.
     *
     * @param primaryStage The primary stage where the interface will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Page title
        Label title = new Label("Reviews for Answer ID: " + answer.getAnswerID());

        // --- Review List Section ---
        ListView<Review> reviewListView = new ListView<>();

        // Load reviews for this answer
        ObservableList<Review> reviewsObs = FXCollections.observableArrayList(
                dbHelper.getReviewsByAnswerId(answer.getAnswerID()));
        reviewListView.setItems(reviewsObs);

        // Display reviews with reviewer name, rating, and review text
        reviewListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Review r, boolean empty) {
                super.updateItem(r, empty);
                if (empty || r == null) {
                    setText(null);
                } else {
                    setText("Reviewer: " + r.getReviewerUserName() + " | Rating: " + r.getRating()
                            + "\n" + r.getReviewText());
                }
            }
        });

        // --- Review Input Section (if user is a reviewer) ---
        TextArea reviewArea = new TextArea();
        reviewArea.setPromptText("Write your review...");

        Spinner<Integer> ratingSpinner = new Spinner<>(1, 5, 3);

        Button addReviewBtn = new Button("Add Review");

        // Handle review submission
        addReviewBtn.setOnAction(e -> {
            String text = reviewArea.getText();
            if (!text.isBlank()) {
                Review newRev = new Review();
                newRev.setAnswerId(answer.getAnswerID());
                newRev.setReviewerUserName(currentUser.getUserName());
                newRev.setReviewText(text);
                newRev.setRating(ratingSpinner.getValue());
                
                dbHelper.addReview(newRev); // Store in DB
                reviewsObs.setAll(dbHelper.getReviewsByAnswerId(answer.getAnswerID())); // Refresh view
                reviewArea.clear();
            }
        });

        // --- Back Button Section ---
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            Question q = StartCSE360.getQuestions().getQuestionByID(answer.getQuestionID());
            new QuestionDetailPage(q, StartCSE360.getQuestions(), StartCSE360.getAnswers(), currentUser)
                    .show(primaryStage);
        });

        // --- Build Layout ---
        layout.getChildren().addAll(title, reviewListView);

        // Only show the input controls if the user is a reviewer
        if (currentUser.getRoles().contains("reviewer")) {
            layout.getChildren().addAll(reviewArea, ratingSpinner, addReviewBtn);
        }

        layout.getChildren().add(backBtn);

        // Show the scene
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Review Management");
        primaryStage.show();
    }
}