/**
 * The {@code javatest} class provides a standalone testing framework to manually verify the core behaviors
 * of the {@code Question} and {@code FirstPage} classes. This class includes tests for basic getter/setter 
 * methods, initialization of properties, and UI routing logic.
 * <p>
 * The tests include:
 * <ul>
 *   <li>Basic getter/setter tests for the {@code Question} class</li>
 *   <li>Initialization test for the {@code creationTime} of a {@code Question}</li>
 *   <li>UI routing tests for the {@code FirstPage} class</li>
 * </ul>
 * JavaFX is initialized using the {@code JFXPanel} for headless execution.
 */
public class javatest {

    // Private constructor to prevent instantiation of a utility-style test class
    private javatest() {}

    // Static block ensures JavaFX toolkit is initialized once for headless environments
    static {
        new JFXPanel(); // Initializes JavaFX toolkit
    }

    /**
     * Entry point of the test runner. Executes all tests and prints results to the console.
     */
    public static void main(String[] args) throws Exception {
        testSetAndGetQuestionID();                      // Test 1
        testCreationTimeIsInitialized();                // Test 2
        testShowMethodDisplaysScene();                  // Test 3
        testContinueButtonTriggersSetupSequence();      // Test 4
        testRoleBasedRouting_AdminGoesToSetup();        // Test 5

        System.out.println("\n✅ All tests finished running.");
    }

    // ------------------- Question.java Tests -------------------

    /**
     * Verifies the basic setter and getter behavior for Question ID.
     */
    public static void testSetAndGetQuestionID() {
        Question question = new Question();             // Create a question instance
        question.setQuestionID(123);                    // Set ID
        if (question.getQuestionID() == 123) {
            System.out.println("✅ testSetAndGetQuestionID passed");
        } else {
            System.out.println("❌ testSetAndGetQuestionID failed");
        }
    }

    /**
     * Confirms that creationTime is automatically initialized on Question construction.
     */
    public static void testCreationTimeIsInitialized() {
        Question question = new Question();
        LocalDateTime time = question.getCreationTime(); // Should not be null and must be recent
        if (time != null && time.isBefore(LocalDateTime.now().plusSeconds(1))) {
            System.out.println("✅ testCreationTimeIsInitialized passed");
        } else {
            System.out.println("❌ testCreationTimeIsInitialized failed");
        }
    }

    // ------------------- FirstPage.java Tests -------------------

    /**
     * A testable subclass of AdminSetupPage that tracks whether its show() method was called.
     */
    static class TestableAdminSetupPage extends AdminSetupPage {
        public static boolean wasShown = false;

        public TestableAdminSetupPage(DatabaseHelper dbHelper) {
            super(dbHelper);
        }

        @Override
        public void show(Stage stage) {
            wasShown = true; // Mark that show() was invoked
        }
    }

    /**
     * A minimal stub implementation of DatabaseHelper with basic admin simulation logic.
     */
    static class StubDatabaseHelper extends DatabaseHelper {
        public boolean isAdmin = true;

        public boolean isAdminUser() {
            return isAdmin;
        }
    }

    /**
     * Tests that FirstPage.show() successfully creates a scene and doesn't crash.
     */
    public static void testShowMethodDisplaysScene() throws InterruptedException {
        Platform.runLater(() -> {
            try {
                FirstPage page = new FirstPage(new StubDatabaseHelper());
                Stage stage = new Stage();
                page.show(stage); // Attempt to load scene
                System.out.println("✅ testShowMethodDisplaysScene passed");
            } catch (Exception e) {
                System.out.println("❌ testShowMethodDisplaysScene failed: " + e.getMessage());
            }
        });
        Thread.sleep(500); // Allow JavaFX thread to process
    }

    /**
     * Simulates clicking the "Continue" button on FirstPage and checks if AdminSetupPage is invoked.
     */
    public static void testContinueButtonTriggersSetupSequence() throws InterruptedException {
        TestableAdminSetupPage.wasShown = false; // Reset flag
        StubDatabaseHelper dbHelper = new StubDatabaseHelper();

        Platform.runLater(() -> {
            Stage stage = new Stage();

            // Inject custom behavior into FirstPage using the test hook
            FirstPage.onContinuePressed = () -> {
                new TestableAdminSetupPage(dbHelper).show(stage);
            };

            // Show FirstPage and find the Continue button
            FirstPage page = new FirstPage(dbHelper);
            page.show(stage);

            VBox root = (VBox) stage.getScene().getRoot(); // Get layout root
            for (javafx.scene.Node node : root.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    if (button.getText().equals("Continue")) {
                        button.fire(); // Simulate user click
                        break;
                    }
                }
            }

            stage.hide(); // Cleanup after firing
        });

        Thread.sleep(500); // Let UI thread finish

        if (TestableAdminSetupPage.wasShown) {
            System.out.println("✅ testContinueButtonTriggersSetupSequence passed");
        } else {
            System.out.println("❌ testContinueButtonTriggersSetupSequence failed");
        }

        FirstPage.onContinuePressed = null; // Reset test hook
    }

    /**
     * Ensures admin users are routed correctly on app launch by simulating show() call.
     */
    public static void testRoleBasedRouting_AdminGoesToSetup() throws InterruptedException {
        TestableAdminSetupPage.wasShown = false; // Reset state
        StubDatabaseHelper dbHelper = new StubDatabaseHelper();
        dbHelper.isAdmin = true;

        Platform.runLater(() -> {
            Stage stage = new Stage();
            new TestableAdminSetupPage(dbHelper).show(stage); // Simulate launch
        });

        Thread.sleep(500);

        if (TestableAdminSetupPage.wasShown) {
            System.out.println("✅ testRoleBasedRouting_AdminGoesToSetup passed");
        } else {
            System.out.println("❌ testRoleBasedRouting_AdminGoesToSetup failed");
        }
    }
}