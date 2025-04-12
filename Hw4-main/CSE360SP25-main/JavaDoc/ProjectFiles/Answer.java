/**
 * The Answer class represents a response to a question in the database.
 * It includes details such as the answer text, author, timestamps,
 * and vote counts (upvotes/downvotes), along with whether the answer is accepted.
 */
package application;

import java.time.LocalDateTime;

/**
 * Represents an Answer entity in the database.
 */
public class Answer {
    private int answerID;             // Unique identifier for the answer
    private int questionID;           // ID of the question this answer is related to
    private String author;            // Author/username of the person who wrote the answer
    private String answerText;        // The actual content of the answer
    private LocalDateTime creationTime; // Timestamp when the answer was created
    private int upvotes;              // Number of upvotes the answer has received
    private int downvotes;            // Number of downvotes the answer has received
    private boolean accepted = false; // Indicates whether the answer is accepted

    /**
     * Constructs an Answer with the current timestamp.
     * Initializes upvotes and downvotes to 0.
     */
    public Answer() {
        this.creationTime = LocalDateTime.now();
    }

    /**
     * Constructs an Answer with specified details.
     *
     * @param answerID    The unique identifier for the answer.
     * @param questionID  The ID of the question this answer belongs to.
     * @param author      The author of the answer.
     * @param answerText  The content of the answer.
     */
    public Answer(int answerID, int questionID, String author, String answerText) {
        this(); // Call default constructor to set timestamp
        setAnswerID(answerID);
        setQuestionID(questionID);
        setAuthor(author);
        setAnswerText(answerText);
    }

    // --- Getters and Setters ---

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the answer.
     * 
     * @param author The name of the author. Cannot be null or blank.
     * @throws IllegalArgumentException if author is null or blank
     */
    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or blank.");
        }
        this.author = author;
    }

    public String getAnswerText() {
        return answerText;
    }

    /**
     * Sets the content of the answer.
     * 
     * @param answerText The answer text. Cannot be null or blank.
     * @throws IllegalArgumentException if answerText is null or blank
     */
    public void setAnswerText(String answerText) {
        if (answerText == null || answerText.isBlank()) {
            throw new IllegalArgumentException("Answer text cannot be null or blank.");
        }
        this.answerText = answerText;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Increments the upvote count by 1.
     */
    public void upvote() {
        this.upvotes++;
    }

    /**
     * Increments the downvote count by 1.
     */
    public void downvote() {
        this.downvotes++;
    }

    /**
     * Sets the upvote count to a specific value.
     *
     * @param upvotes The number of upvotes to set.
     */
    public void setUpvotesCount(int upvotes) {
        this.upvotes = upvotes;
    }

    /**
     * Sets the downvote count to a specific value.
     *
     * @param downvotes The number of downvotes to set.
     */
    public void setDownvotesCount(int downvotes) {
        this.downvotes = downvotes;
    }

    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Sets whether this answer is marked as accepted.
     *
     * @param accepted true if the answer is accepted; false otherwise.
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Returns a string representation of the answer.
     * Includes the author name, "Accepted" tag if applicable, and the answer content.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(author);
        if (accepted) {
            sb.append(" (Accepted)");
        }
        sb.append(": ").append(answerText);
        return sb.toString();
    }
}