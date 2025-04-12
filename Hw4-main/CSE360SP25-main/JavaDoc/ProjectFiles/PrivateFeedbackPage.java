/**
 * <p>Title: PrivateFeedbackPage</p>
 * 
 * <p>Description: This class represents a page that displays private feedback messages 
 * related to a specific question. It allows the current user to view and send private 
 * messages to the author of a question.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2025-04-01 Initial implementation
 */
public class PrivateFeedbackPage {

    /** The DatabaseHelper instance used to interact with the database */
    private DatabaseHelper dbHelper;

    /** The question for which the private feedback is being displayed */
    private Question question;

    /** The current user who is viewing or sending private feedback */
    private User currentUser;

    /**
     * Constructs a PrivateFeedbackPage with the specified DatabaseHelper, 
     * Question, and User.
     *
     * @param dbHelper   The DatabaseHelper instance used for database operations.
     * @param question   The question for which the private feedback is being displayed.
     * @param currentUser The current user viewing or sending private feedback.
     */
    public PrivateFeedbackPage(DatabaseHelper dbHelper, Question question, User currentUser) {
        this.dbHelper = dbHelper;
        this.question = question;
        this.currentUser = currentUser;
    }

    /**
     * Displays the private feedback page with a list of messages and an option 
     * to send new private feedback.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    public void show(Stage primaryStage) {
        // Create the main layout with spacing and padding
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Header label showing which question the feedback is related to
        Label title = new Label("Private Messages for Question: " + question.getQuestionTitle());

        // Initialize a ListView to display private messages
        ListView<Message> messagesListView = new ListView<>();

        // Fetch messages associated with the current question and current user
        ObservableList<Message> messagesObservable = FXCollections.observableArrayList(
                dbHelper.getMessagesForQuestion(question.getQuestionID(), currentUser.getUserName()));
        messagesListView.setItems(messagesObservable);

        // Define how each message appears in the ListView
        messagesListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null); // Clear text if there's no message
                } else {
                    // Display sender and message content
                    setText("From: " + msg.getFromUser() + "\n" + msg.getContent());
                }
            }
        });

        // Text area for composing new feedback messages
        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Write your private feedback here...");

        // Button to send the feedback message
        Button sendBtn = new Button("Send");
        sendBtn.setOnAction(e -> {
            String content = messageArea.getText();

            // Ensure the message is not blank before sending
            if (!content.isBlank()) {
                // Create and populate a new Message object
                Message newMsg = new Message();
                newMsg.setFromUser(currentUser.getUserName());
                newMsg.setToUser(question.getAuthor()); // Send to the question's author
                newMsg.setQuestionId(question.getQuestionID());
                newMsg.setContent(content);

                // Store the message in the database
                dbHelper.addMessage(newMsg);

                // Refresh the message list after sending
                messagesObservable.setAll(
                        dbHelper.getMessagesForQuestion(question.getQuestionID(), currentUser.getUserName()));

                // Clear the input area
                messageArea.clear();
            }
        });

        // Button to go back to the question detail page
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            // Navigate back to the question detail view
            new QuestionDetailPage(
                question, 
                StartCSE360.getQuestions(), 
                StartCSE360.getAnswers(), 
                currentUser
            ).show(primaryStage);
        });

        // Add all components to the layout
        layout.getChildren().addAll(title, messagesListView, messageArea, sendBtn, backBtn);

        // Set up and display the scene
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Private Feedback");
        primaryStage.show();
    }
}