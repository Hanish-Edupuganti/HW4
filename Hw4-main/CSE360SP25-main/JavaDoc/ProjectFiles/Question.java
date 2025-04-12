/**
 * Represents a single Question stored in the database.
 * This class contains the details of a question including its title, text, 
 * author, creation time, solved status, and accepted answer ID.
 * It provides methods for setting and getting these properties, as well as 
 * validating inputs where necessary.
 */
public class Question {

    // Unique ID of the question (typically set by the database)
    private int questionID;

    // Username of the user who asked the question
    private String author;

    // The title of the question (short summary)
    private String questionTitle;

    // The main body or content of the question
    private String questionText;

    // The timestamp when the question was created
    private LocalDateTime creationTime;

    // Boolean flag indicating whether the question has been marked as solved
    private boolean solved = false;

    // ID of the answer that was marked as accepted (if any)
    private int acceptedAnswerID = 0;

    /**
     * Default constructor that initializes the creation time to the current date and time.
     */
    public Question() {
        this.creationTime = LocalDateTime.now(); // Automatically set timestamp when question is created
    }

    /**
     * Constructor to create a new Question with specified author, title, and text.
     * The creation time is automatically set to the current date and time.
     */
    public Question(String author, String title, String text) {
        this(); // Calls the default constructor to set creationTime
        setAuthor(author);           // Validate and assign author
        setQuestionTitle(title);     // Validate and assign title
        setQuestionText(text);       // Validate and assign question text
    }

    // -------------------- Getters and Setters --------------------

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
     * Sets the author of the question.
     * Throws IllegalArgumentException if author is null or empty.
     */
    public void setAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or blank.");
        }
        this.author = author;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    /**
     * Sets the title of the question.
     * Throws IllegalArgumentException if title is null or empty.
     */
    public void setQuestionTitle(String questionTitle) {
        if (questionTitle == null || questionTitle.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
        this.questionTitle = questionTitle;
    }

    public String getQuestionText() {
        return questionText;
    }

    /**
     * Sets the text of the question.
     * Throws IllegalArgumentException if text is null or empty.
     */
    public void setQuestionText(String questionText) {
        if (questionText == null || questionText.isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or blank.");
        }
        this.questionText = questionText;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getAcceptedAnswerID() {
        return acceptedAnswerID;
    }

    public void setAcceptedAnswerID(int acceptedAnswerID) {
        this.acceptedAnswerID = acceptedAnswerID;
    }

    // -------------------- Utility Method --------------------

    /**
     * Returns a string representation of the question, showing its title.
     * Appends "(Answered)" if the question has been solved.
     */
    @Override
    public String toString() {
        if (solved) {
            return questionTitle + " (Answered)";
        }
        return questionTitle;
    }
}