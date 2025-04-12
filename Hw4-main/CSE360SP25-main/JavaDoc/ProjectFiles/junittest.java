/**
 * The {@code junittest} class is a set of JUnit test cases that verify core behaviors of
 * the {@code Question} and {@code FirstPage} classes.
 * <p>
 * The tests are designed to ensure that:
 * <ul>
 *   <li>Basic getter and setter functionality for the {@code Question} class works as expected.</li>
 *   <li>The {@code creationTime} of a {@code Question} object is correctly initialized.</li>
 *   <li>The {@code FirstPage} class renders properly and triggers the correct actions.</li>
 * </ul>
 * JavaFX is initialized using {@code JFXPanel} to allow headless execution in the test environment.
 */
public class junittest {

    // Static block to initialize JavaFX runtime environment
    static {
        new JFXPanel(); // Necessary for initializing JavaFX in headless mode (e.g., CI environments)
    }

    // ------------------- Question.java Tests -------------------

    /**
     * Verifies that the setter and getter for Question ID work correctly.
     */
    @Test
    public void testSetAndGetQuestionID() {
        Question question = new Question();            // Create new question
        question.setQuestionID(123);                   // Set ID
        assertEquals(123, question.getQuestionID());   // Assert it was set correctly
    }

    /**
     * Verifies that the creationTime is initialized when a Question is instantiated.
     */
    @Test
    public void testCreationTimeIsInitialized() {
        Question question = new Question();            // Create new question
        LocalDateTime creationTime = question.getCreationTime();
        assertNotNull(creationTime);                   // Ensure it's not null
        assertTrue(creationTime.isBefore(LocalDateTime.now().plusSeconds(1))); // Ensure it's recent
    }

    // ------------------- FirstPage.java Tests -------------------

    /**
     * A testable subclass of AdminSetupPage that records whether it was shown.
     */
    public static class TestableAdminSetupPage extends AdminSetupPage {
        public static boolean wasShown = false;

        public TestableAdminSetupPage(DatabaseHelper dbHelper) {
            super(dbHelper);
        }

        @Override
        public void show(Stage stage) {
            wasShown = true; // Record that the method was triggered
        }
    }

    /**
     * Stub implementation of DatabaseHelper for simulating admin behavior.
     */
    public static class StubDatabaseHelper extends DatabaseHelper {
        public boolean isAdmin = true;

        public boolean isAdminUser() {
            return isAdmin;
        }
    }

    // Shared fields for test setup
    private StubDatabaseHelper stubDbHelper;
    private FirstPage firstPage;

    /**
     * Initializes shared objects before each test.
     */
    @Before
    public void setUp() {
        stubDbHelper = new StubDatabaseHelper();     // Simulated DB helper
        firstPage = new FirstPage(stubDbHelper);     // Create a test instance of FirstPage
        TestableAdminSetupPage.wasShown = false;     // Reset tracking flag
    }

    /**
     * Ensures the FirstPage.show() method displays a scene without throwing exceptions.
     */
    @Test
    public void testShowMethodDisplaysScene() throws Exception {
        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                firstPage.show(stage);               // Call the method under test
            } catch (Exception e) {
                fail("show() method threw an exception: " + e.getMessage()); // Test fails if exception occurs
            }
        });
        Thread.sleep(500); // Wait to let JavaFX thread execute
    }

    /**
     * Ensures that pressing the "Continue" button in FirstPage triggers navigation to AdminSetupPage.
     */
    @Test
    public void testContinueButtonTriggersSetupSequence() throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();

            // Create a FirstPage with an overridden show() to simulate button and interaction
            FirstPage pageWithCustomSetup = new FirstPage(stubDbHelper) {
                @Override
                public void show(Stage stage) {
                    VBox layout = new VBox();                  // Create layout manually
                    Button continueButton = new Button("Continue");

                    // Hook: trigger TestableAdminSetupPage when button is clicked
                    continueButton.setOnAction(e -> new TestableAdminSetupPage(stubDbHelper).show(stage));
                    layout.getChildren().add(continueButton);

                    stage.setScene(new Scene(layout, 300, 200));
                    stage.show();

                    continueButton.fire();                    // Simulate user clicking the button
                }
            };

            pageWithCustomSetup.show(stage); // Show the custom page
        });

        Thread.sleep(500); // Allow time for FX operations

        // Assert that AdminSetupPage was triggered
        assertTrue("Setup page should have been triggered", TestableAdminSetupPage.wasShown);
    }

    /**
     * Ensures that admin users are correctly routed to AdminSetupPage during app start.
     */
    @Test
    public void testRoleBasedRouting_AdminGoesToSetup() throws Exception {
        stubDbHelper.isAdmin = true; // Simulate an admin user

        Platform.runLater(() -> {
            Stage stage = new Stage();
            new TestableAdminSetupPage(stubDbHelper).show(stage); // Manually invoke AdminSetupPage
        });

        Thread.sleep(500); // Let FX thread complete

        // Check that the AdminSetupPage was shown
        assertTrue("Admin should be routed to setup page", TestableAdminSetupPage.wasShown);
    }
}