/**
 * The {@code Message} class represents a message sent from one user to another.
 * It contains the details of the message including sender, recipient, content,
 * creation time, and whether the message has been read.
 * 
 * <p>The message can optionally be linked to a specific question through the {@code questionId} field.
 * It provides getters and setters to allow manipulation of these fields.
 * </p>
 * 
 * <p>Usage Example:
 * <pre>
 * Message message = new Message();
 * message.setFromUser("Alice");
 * message.setToUser("Bob");
 * message.setContent("Hello, Bob!");
 * message.setQuestionId(42);  // Link to a question (optional)
 * </pre>
 * </p>
 */
public class Message {
    
    /** The unique identifier for the message */
    private int messageId;

    /** The user who sent the message */
    private String fromUser;

    /** The user who will receive the message */
    private String toUser;

    /** The ID of the question this message is linked to (optional) */
    private int questionId;

    /** The content of the message */
    private String content;

    /** The time when the message was created */
    private LocalDateTime creationTime;

    /** The read status of the message */
    private boolean isRead;

    /**
     * Constructs a new {@code Message} instance with the current time set as the creation time.
     */
    public Message() {
        creationTime = LocalDateTime.now(); // Automatically timestamp message when it's created
    }

    /**
     * Gets the unique identifier for the message.
     * 
     * @return the message ID
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Sets the unique identifier for the message.
     * 
     * @param messageId the new message ID
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId; // Assign message ID
    }

    /**
     * Gets the user who sent the message.
     * 
     * @return the sender's username
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * Sets the user who sent the message.
     * 
     * @param fromUser the sender's username
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser; // Set sender
    }

    /**
     * Gets the user who will receive the message.
     * 
     * @return the recipient's username
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * Sets the user who will receive the message.
     * 
     * @param toUser the recipient's username
     */
    public void setToUser(String toUser) {
        this.toUser = toUser; // Set recipient
    }

    /**
     * Gets the ID of the question this message is linked to.
     * 
     * @return the question ID
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Sets the ID of the question this message is linked to.
     * 
     * @param questionId the question ID
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId; // Link message to a specific question
    }

    /**
     * Gets the content of the message.
     * 
     * @return the message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     * 
     * @param content the new content of the message
     */
    public void setContent(String content) {
        this.content = content; // Set message text
    }

    /**
     * Gets the creation time of the message.
     * 
     * @return the creation time of the message
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the creation time of the message.
     * 
     * @param creationTime the new creation time of the message
     */
    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime; // Manually set timestamp if needed (e.g., from database)
    }

    /**
     * Checks if the message has been read.
     * 
     * @return {@code true} if the message has been read, {@code false} otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the read status of the message.
     * 
     * @param read {@code true} if the message is marked as read, {@code false} otherwise
     */
    public void setRead(boolean read) {
        isRead = read; // Mark message as read/unread
    }
}