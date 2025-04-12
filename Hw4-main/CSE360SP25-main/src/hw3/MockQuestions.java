package hw3;

import application.Question;
import application.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockQuestions {

    private List<Question> questions = new ArrayList<>();

    /**
     * Seeds the mock manager with sample data belonging to different users.
     */
    public void seedSampleQuestions() {
        // questionID=1 belongs to 'alice'
        Question q1 = new Question("alice", "Java Basics", "How to start with Java?");
        q1.setQuestionID(1);
        questions.add(q1);

        // questionID=2 belongs to 'bob'
        Question q2 = new Question("bob", "Spring Boot", "Need help with Spring Boot config");
        q2.setQuestionID(2);
        questions.add(q2);

        // questionID=3 belongs to 'charlie'
        Question q3 = new Question("charlie", "Data Structures in Java", "Implementing a BST in Java");
        q3.setQuestionID(3);
        questions.add(q3);
    }

    /**
     * Attempts to delete the specified question if the user is admin or the author.
     *
     * @param questionID the ID of the question to delete
     * @param currentUser the user attempting the deletion
     * @return true if deletion is successful (authorized), false otherwise
     */
    public boolean attemptDelete(int questionID, User currentUser) {
        Question found = null;
        for (Question q : questions) {
            if (q.getQuestionID() == questionID) {
                found = q;
                break;
            }
        }
        if (found == null) {
            // Not found => can't delete
            return false;
        }
        boolean isAdmin = "admin".equalsIgnoreCase(currentUser.getRole());
        boolean isAuthor = found.getAuthor().equalsIgnoreCase(currentUser.getUserName());
        if (isAdmin || isAuthor) {
            questions.remove(found);
            return true;
        }
        return false;
    }

    /**
     * Searches for questions containing the given keyword in either
     * the question's title or text.
     *
     * @param keyword the search text
     * @return a list of questions matching the keyword
     */
    public List<Question> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(questions);
        }
        String lower = keyword.toLowerCase();
        return questions.stream()
                .filter(q -> q.getQuestionTitle().toLowerCase().contains(lower)
                          || q.getQuestionText().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}