/**
 * The {@code MessagingPage} class represents the messaging center for a user, where they can
 * view their messages, send new messages, and delete messages they have sent or received.
 * 
 * <p>
 * This page allows the user to:
 * <ul>
 *   <li>View a list of messages</li>
 *   <li>Compose and send new messages</li>
 *   <li>Delete messages they have sent or received</li>
 *   <li>Navigate back to the home page</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The messages are retrieved from the database and displayed in a {@link ListView}. The user can also
 * interact with the messages by selecting, sending, and deleting them.
 * </p>
 * 
 * <p>Usage Example:
 * <pre>
 * MessagingPage messagingPage = new MessagingPage(dbHelper, currentUser);
 * messagingPage.show(primaryStage);
 * </pre>
 * </p>
 */
public class MessagingPage {
    
    /** The database helper used to interact with the database */
    private DatabaseHelper dbHelper;

    /** The current user viewing the messaging page */
    private User currentUser;

    /**
     * Constructs a new {@code MessagingPage} for the specified user.
     * 
     * @param dbHelper the {@link DatabaseHelper} instance for database operations
     * @param currentUser the {@link User} instance representing the current user
     */
    public MessagingPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the messaging page on the given {@code Stage}.
     * This method sets up the UI components, retrieves the user's messages,
     * and handles user interactions such as composing and deleting messages.
     *
     * @param primaryStage the {@link Stage} to display the messaging page on
     */
    public void show(Stage primaryStage) {
        // Layout container with spacing between elements
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Page title label
        Label title = new Label("Messaging Center");

        // ------------------- Message List -------------------

        // Retrieve all messages for the current user from the database
        List<Message> messages = dbHelper.getMessagesForUser(currentUser.getUserName());

        // Wrap the messages in an observable list for dynamic updates
        ObservableList<Message> observableMessages = FXCollections.observableArrayList(messages);

        // Create a ListView for displaying messages
        ListView<Message> messageListView = new ListView<>(observableMessages);

        // Customize how each message appears in the list
        messageListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);
                if (empty || msg == null) {
                    setText(null);
                } else {
                    // Determine whether the message is incoming or outgoing
                    String direction = msg.getFromUser().equals(currentUser.getUserName())
                            ? "Sent to: " : "Received from: ";
                    String other = msg.getFromUser().equals(currentUser.getUserName())
                            ? msg.getToUser() : msg.getFromUser();
                    // Display basic message info
                    setText(direction + other + "\n" + msg.getContent() + "\n(" + msg.getCreationTime() + ")");
                }
            }
        });

        // ------------------- Message Composer -------------------

        // TextField to enter recipient username
        TextField recipientField = new TextField();
        recipientField.setPromptText("Recipient username");

        // TextArea to write a new message
        TextArea newMessageArea = new TextArea();
        newMessageArea.setPromptText("Type your message here...");

        // Button to send a message
        Button sendBtn = new Button("Send Message");
        sendBtn.setOnAction(e -> {
            String recipient = recipientField.getText();
            String content = newMessageArea.getText();
            
            // Validate input
            if (recipient.isBlank() || content.isBlank()) {
                return; // Skip if fields are empty
            }

            // Create and populate a new Message object
            Message newMsg = new Message();
            newMsg.setFromUser(currentUser.getUserName());
            newMsg.setToUser(recipient);
            newMsg.setContent(content);

            // Save message to database
            dbHelper.addMessage(newMsg);

            // Refresh message list
            observableMessages.setAll(dbHelper.getMessagesForUser(currentUser.getUserName()));

            // Clear form
            recipientField.clear();
            newMessageArea.clear();
        });

        // ------------------- Message Deletion -------------------

        // Button to delete the selected message
        Button deleteBtn = new Button("Delete Selected Message");
        deleteBtn.setOnAction(e -> {
            Message selected = messageListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            // Only allow deletion if the user is sender or receiver
            if (selected.getFromUser().equals(currentUser.getUserName())
                    || selected.getToUser().equals(currentUser.getUserName())) {

                boolean success = dbHelper.deleteMessage(selected.getMessageId());
                if (success) {
                    // Refresh messages after deletion
                    observableMessages.setAll(dbHelper.getMessagesForUser(currentUser.getUserName()));
                }
            } else {
                // Show error if user tries to delete a message they’re not part of
                Alert alert = new Alert(Alert.AlertType.ERROR, "You can only delete messages you sent or received.");
                alert.showAndWait();
            }
        });

        // ------------------- Navigation Button -------------------

        // Button to go back to user's appropriate home page
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            if (currentUser.getRoles().contains("admin")) {
                new AdminHomePage().show(primaryStage, currentUser);
            } else {
                new UserHomePage().show(primaryStage, currentUser);
            }
        });

        // Add all UI components to the layout
        layout.getChildren().addAll(
            title,
            messageListView,
            new Label("Compose New Message:"),
            recipientField,
            newMessageArea,
            sendBtn,
            deleteBtn,
            backBtn
        );

        // Create and display the scene
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Messaging Center");
        primaryStage.show();
    }
}