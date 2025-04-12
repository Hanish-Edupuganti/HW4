package application;

import databasePart1.DatabaseHelper;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * Main entry point for the CSE360 application, now using a database-backed
 * Questions and Answers manager.
 */
public class StartCSE360 extends Application {

    // The single DatabaseHelper instance shared across the app
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    // Database-backed managers for questions and answers
    private static Questions questions;
    private static Answers answers;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Connect to H2 database
            databaseHelper.connectToDatabase();

            // Instantiate DB-based managers
            questions = new Questions(databaseHelper);
            answers = new Answers(databaseHelper);

            // Check if no users exist; if so, show first-time admin setup
            if (databaseHelper.isDatabaseEmpty()) {
                new FirstPage(databaseHelper).show(primaryStage);
            } else {
                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton Questions manager (backed by the DB).
     */
    public static Questions getQuestions() {
        return questions;
    }

    /**
     * Returns the singleton Answers manager (backed by the DB).
     */
    public static Answers getAnswers() {
        return answers;
    }

    /**
     * Returns the singleton DatabaseHelper instance.
     */
    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
