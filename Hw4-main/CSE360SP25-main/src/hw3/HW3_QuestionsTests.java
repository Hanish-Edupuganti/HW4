package hw3;

import application.Question;
import application.User;

import java.util.List;

/**
 * HW3_QuestionTests
 *
 * <p>This class demonstrates automated tests for question-related features, such as:
 * <ul>
 *   <li>Unauthorized deletion (normal user trying to delete someone else's question)</li>
 *   <li>Authorized deletion by the author</li>
 *   <li>Admin user deletion of any question</li>
 *   <li>Searching for questions by keyword</li>
 *   <li>Searching for a non-existent keyword</li>
 * </ul>
 *
 * @author 
 *   Anthony D'Agosto
 * @version 
 *   1.0
 * @since 
 *   3/24/2025
 */
public class HW3_QuestionsTests {

    /**
     * Counter for passed tests.
     */
    private static int numPassed = 0;

    /**
     * Counter for failed tests.
     */
    private static int numFailed = 0;

    private static MockQuestions mockQuestions;

    /**
     * Runs the question-based tests: unauthorized vs authorized deletion, searching, etc.
     * 
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("Starting HW3: Question Tests...");

        // Initialize or reset mock
        mockQuestions = new MockQuestions();
        mockQuestions.seedSampleQuestions();

        // 1) Unauthorized user tries to delete someone else's question
        testUnauthorizedDelete();
        // 2) Author deletes their own question
        testAuthorDelete();
        // 3) Admin user deletes someone else's question
        testAdminDelete();
        // 4) Searching for known keyword
        testSearchFound();
        // 5) Searching for non-existent keyword
        testSearchNotFound();

        // Summary
        System.out.println("-------------------------------------");
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * Verifies that a user who is neither admin nor the author cannot delete a question.
     */
    private static void testUnauthorizedDelete() {
        System.out.println("\nTest: Unauthorized user tries to delete another user's question...");

        // questionID=1 belongs to 'alice'
        User unauthorizedUser = new User("bob", "bobPass", "user");
        boolean result = mockQuestions.attemptDelete(1, unauthorizedUser);

        if (!result) {
            pass("Unauthorized user was blocked. (Correct)");
        } else {
            fail("Unauthorized user was able to delete. (Incorrect)");
        }
    }

    /**
     * Verifies that a user can delete their own question if they are the author.
     */
    private static void testAuthorDelete() {
        System.out.println("\nTest: Author user tries to delete their own question...");

        // questionID=2 belongs to 'bob'
        User authorUser = new User("bob", "bobPass", "user");
        boolean result = mockQuestions.attemptDelete(2, authorUser);

        if (result) {
            pass("Author was allowed to delete their own question. (Correct)");
        } else {
            fail("Author was blocked from deleting their own question. (Incorrect)");
        }
    }

    /**
     * Verifies that an admin can delete any question regardless of author.
     */
    private static void testAdminDelete() {
        System.out.println("\nTest: Admin user tries to delete another user's question...");

        // questionID=3 belongs to 'charlie'
        User adminUser = new User("adminUser", "adminPass", "admin");
        boolean result = mockQuestions.attemptDelete(3, adminUser);

        if (result) {
            pass("Admin was allowed to delete. (Correct)");
        } else {
            fail("Admin was blocked from deleting. (Incorrect)");
        }
    }

    /**
     * Tests that searching for a known keyword returns at least one result.
     */
    private static void testSearchFound() {
        System.out.println("\nTest: Searching for questions with keyword 'Java'...");

        List<Question> results = mockQuestions.searchByKeyword("Java");
        if (!results.isEmpty()) {
            pass("Found matching questions for 'Java' (Correct). Count=" + results.size());
        } else {
            fail("Expected to find matches for 'Java', but got none.");
        }
    }

    /**
     * Tests that searching for a non-existent keyword yields zero results.
     */
    private static void testSearchNotFound() {
        System.out.println("\nTest: Searching for questions with keyword 'NonExistentKeyword'...");

        List<Question> results = mockQuestions.searchByKeyword("NonExistentKeyword");
        if (results.isEmpty()) {
            pass("No matching questions found (Correct).");
        } else {
            fail("Expected zero matches, but got " + results.size() + ".");
        }
    }

    /** Helper to record a passed test. */
    private static void pass(String message) {
        System.out.println("PASS: " + message);
        numPassed++;
    }

    /** Helper to record a failed test. */
    private static void fail(String message) {
        System.out.println("FAIL: " + message);
        numFailed++;
    }
}