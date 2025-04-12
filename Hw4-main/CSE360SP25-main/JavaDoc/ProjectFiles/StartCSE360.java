// File: StartCSE360.java

/**
 * <p><b>Title:</b> StartCSE360</p>
 * 
 * <p><b>Description:</b> This is the main entry point for the CSE360 application. 
 * It initializes the application, establishes the database connection, and determines 
 * whether to start the user at the first-time setup page or the login selection page.</p>
 *
 * <p>This class also provides static access to shared instances of:
 * <ul>
 *   <li>{@code DatabaseHelper} – for managing DB connections and operations</li>
 *   <li>{@code Questions} – for handling question-related operations</li>
 *   <li>{@code Answers} – for handling answer-related operations</li>
 * </ul>
 * </p>
 * 
 * <p><b>Copyright:</b> ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00 – 2025-04-01 Initial implementation
 */
public class StartCSE360 extends Application {

    /** Singleton instance for handling database operations throughout the app */
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    /** Manager for all question-related logic and persistence */
    private static Questions questions;

    /** Manager for all answer-related logic and persistence */
    private static Answers answers;

    /**
     * The main method that launches the JavaFX application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the application. It connects to the database, sets up data managers,
     * and determines whether to display the first-time setup page or the login screen.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Establish a connection to the H2 database
            databaseHelper.connectToDatabase();

            // Create question and answer managers using the database
            questions = new Questions(databaseHelper);
            answers = new Answers(databaseHelper);

            // Determine which screen to show based on database content
            if (databaseHelper.isDatabaseEmpty()) {
                // First-time setup if no users exist
                new FirstPage(databaseHelper).show(primaryStage);
            } else {
                // Normal startup flow
                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the singleton Questions manager, used for all question logic and UI binding.
     *
     * @return The {@code Questions} manager instance.
     */
    public static Questions getQuestions() {
        return questions;
    }

    /**
     * Gets the singleton Answers manager, used for managing answer entities.
     *
     * @return The {@code Answers} manager instance.
     */
    public static Answers getAnswers() {
        return answers;
    }

    /**
     * Gets the singleton DatabaseHelper instance for DB queries and connection.
     *
     * @return The {@code DatabaseHelper} instance.
     */
    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}