package hw3;

import application.PasswordEvaluator;

/**
 * HW3_PasswordTests
 *
 * <p>This class is a standalone test mainline for the PasswordEvaluator. It demonstrates
 * five automated tests allocated to me for HW3:
 * <ul>
 *   <li>Test 1: Valid password with uppercase, lowercase, digit, special char, length >= 8</li>
 *   <li>Test 2: Too short password</li>
 *   <li>Test 3: Missing digit</li>
 *   <li>Test 4: Missing special character</li>
 *   <li>Test 5: Contains an invalid character '=' (if your rules forbid it)</li>
 * </ul>
 *
 * @author 
 *   Anthony D'Agosto
 * @version 
 *   1.0
 * @since 
 *   3/24/2025
 */
public class HW3_PasswordTests {

    /**
     * A counter for the number of tests that pass.
     */
    private static int numPassed = 0;

    /**
     * A counter for the number of tests that fail.
     */
    private static int numFailed = 0;

    /**
     * The main entry point for this automated test class. It runs five test scenarios on
     * the PasswordEvaluator and prints pass/fail results to the console.
     *
     * @param args the command-line arguments (unused)
     */
    public static void main(String[] args) {
        System.out.println("Starting HW3: Automated Password Tests...");

        // 5 test scenarios
        performTestCase(1, "Aa!12345", true);   // valid
        performTestCase(2, "Ab12!", false);     // too short
        performTestCase(3, "ABCdefgh", false);  // missing digit
        performTestCase(4, "Abc12345", false);  // missing special char
        performTestCase(5, "Ab=12!45", false);  // invalid '=' if your rules forbid it

        // Summary
        System.out.println("----------------------------------------");
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /**
     * Executes a single test case by invoking the PasswordEvaluator and checking whether
     * the returned message is empty (valid password) or non-empty (invalid password).
     * Compares that result to the expected outcome.
     *
     * @param testCase the unique test case ID
     * @param inputText the password string to be tested
     * @param expectedPass true if the password is expected to be valid, false otherwise
     */
    private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
        System.out.println("\nTest case: " + testCase);
        System.out.println("Input password: \"" + inputText + "\"");

        // Evaluate using your existing PasswordEvaluator
        String resultText = PasswordEvaluator.evaluatePassword(inputText);

        // If resultText is empty => password is valid
        boolean isActuallyValid = resultText.isEmpty();

        // Compare actual vs expected
        if (isActuallyValid == expectedPass) {
            System.out.println("***Success***");
            numPassed++;
        } else {
            System.out.println("***Failure***");
            System.out.println("  Expected: " + (expectedPass ? "valid" : "invalid"));
            System.out.println("  Evaluator says: " + (isActuallyValid ? "valid" : "invalid"));
            if (!resultText.isEmpty()) {
                System.out.println("  Error message: " + resultText);
            }
            numFailed++;
        }
    }

}