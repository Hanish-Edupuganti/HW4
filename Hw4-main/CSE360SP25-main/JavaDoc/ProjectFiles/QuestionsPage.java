/**
 * A UI page that displays all questions from the database,
 * providing functionality for searching (questions + answers), adding, and deleting questions.
 * Only an admin or the question's author can delete a question.
 */
public class QuestionsPage {

    private final Questions questions;  // DB-based question manager
    private final Answers answers;      // DB-based answer manager
    private final User currentUser;     // Represents the user currently logged in (could be admin, reviewer, etc.)

    /**
     * Constructor to initialize the QuestionsPage with the given questions, answers, and current user.
     *
     * @param questions The database of questions.
     * @param answers The database of answers.
     * @param currentUser The currently logged-in user.
     */
    public QuestionsPage(Questions questions, Answers answers, User currentUser) {
        this.questions = questions;
        this.answers = answers;
        this.currentUser = currentUser;
    }

    /**
     * Displays the questions management page in the provided stage.
     * This method is expected to initialize all the UI elements and handle user interactions.
     *
     * @param primaryStage The stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // UI setup and event handling code here
        // Could include: a ListView for questions, buttons for Add/Delete/Search, etc.
    }

    /**
     * Performs a combined search on both questions and answers, searching by keyword.
     * The results are shown in the given ListView.
     *
     * @param keyword The keyword to search for.
     * @param listView The ListView to display the search results.
     */
    private void combinedSearch(String keyword, ListView<Question> listView) {
        // 1) Search for the keyword within all question texts
        List<Question> questionsFound = questions.searchByKeyword(keyword);

        // 2) Search all answers to find those that contain the keyword
        List<Answer> allAnswers = answers.getAllAnswers();
        List<Answer> answersFound = allAnswers.stream()
                .filter(a -> a.getAnswerText().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        // 3) From matching answers, extract the IDs of their associated questions
        Set<Integer> questionIDs = answersFound.stream()
                .map(Answer::getQuestionID)
                .collect(Collectors.toSet());

        // 4) Add any matching questions from those IDs (from answer match)
        for (int qID : questionIDs) {
            Question q = questions.getQuestionByID(qID);
            if (q != null) {
                questionsFound.add(q);
            }
        }

        // 5) Remove duplicates in case a question matched both directly and via an answer
        List<Question> uniqueResults = questionsFound.stream()
                .distinct()
                .collect(Collectors.toList());

        // Display results in the UI
        listView.getItems().addAll(uniqueResults);
    }
}