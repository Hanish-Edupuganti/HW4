/**
 * <p>Title: Questions</p>
 * 
 * <p>Description: This class manages the CRUD operations for Question objects. 
 * It delegates the actual database operations to the DatabaseHelper class.</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2025-04-01 Initial implementation
 */
public class Questions {
    
    /**
     * The DatabaseHelper instance used for interacting with the database.
     */
    private DatabaseHelper dbHelper;

    /**
     * Constructs a Questions manager with the given DatabaseHelper.
     *
     * @param dbHelper The DatabaseHelper instance used for database operations.
     */
    public Questions(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Adds a new question to the database.
     *
     * @param question The Question object to be added to the database.
     */
    public void addQuestion(Question question) {
        try {
            dbHelper.createQuestion(question); // Delegates question creation to the DB helper
        } catch (SQLException e) {
            e.printStackTrace(); // Logs SQL errors if insertion fails
        }
    }

    /**
     * Retrieves all questions from the database.
     *
     * @return A list of all Question objects stored in the database.
     */
    public List<Question> getAllQuestions() {
        return dbHelper.getAllQuestions(); // Directly returns the result from the DB helper
    }

    /**
     * Retrieves a specific question by its ID from the database.
     *
     * @param questionID The ID of the question to be retrieved.
     * @return The Question object with the specified ID, or null if not found.
     */
    public Question getQuestionByID(int questionID) {
        return dbHelper.getQuestionByID(questionID); // Queries DB for a question by ID
    }

    /**
     * Updates an existing question in the database.
     *
     * @param question The Question object containing updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateQuestion(Question question) {
        try {
            dbHelper.updateQuestion(question); // Attempts to update question in DB
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Logs any SQL exceptions
            return false;
        }
    }

    /**
     * Deletes a question from the database by its ID.
     *
     * @param questionID The ID of the question to be deleted.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteQuestion(int questionID) {
        try {
            dbHelper.deleteQuestion(questionID); // Attempts deletion by ID
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Logs any errors encountered during deletion
            return false;
        }
    }

    /**
     * Searches for questions that contain a specific keyword in their text.
     *
     * @param keyword The keyword to search for in the question text.
     * @return A list of Question objects that contain the specified keyword in their text.
     */
    public List<Question> searchByKeyword(String keyword) {
        List<Question> all = dbHelper.getAllQuestions(); // Get all questions for filtering

        // If no keyword is provided, return all questions
        if (keyword == null || keyword.isBlank()) {
            return all;
        }

        // Filter the list based on keyword presence in the question text (case-insensitive)
        return all.stream()
            .filter(q -> q.getQuestionText().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
}