// File: StaffHomePage.java

/**
 * <p><b>Title:</b> StaffHomePage</p>
 * 
 * <p><b>Description:</b> This class represents the homepage for staff users. 
 * It allows staff members to review submitted content, view user requests, 
 * or log out of the application. Navigation is handled through JavaFX buttons 
 * that load different pages depending on the selected action.</p>
 * 
 * <p><b>Copyright:</b> ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00 – 2025-04-01 Initial implementation
 */
public class StaffHomePage {

    /**
     * The DatabaseHelper instance used for interacting with the database.
     */
    private DatabaseHelper dbHelper;

    /**
     * The current user who is logged into the application.
     */
    private User currentUser;

    /**
     * Constructs a StaffHomePage with the specified DatabaseHelper and currentUser.
     *
     * @param dbHelper The DatabaseHelper instance used for database operations.
     * @param currentUser The current user who is logged into the system.
     */
    public StaffHomePage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the homepage for staff users. This page includes three primary actions:
     * <ul>
     *   <li><b>Review All Content:</b> Navigates to the ContentReviewPage</li>
     *   <li><b>View Requests:</b> Navigates to the RequestsPage for handling user requests</li>
     *   <li><b>Logout:</b> Logs out the user and redirects to the SetupLoginSelectionPage</li>
     * </ul>
     *
     * @param primaryStage The primary stage where the scene will be set.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Button to open the Content Review page
        Button reviewContentBtn = new Button("Review All Content");
        reviewContentBtn.setOnAction(e -> {
            new ContentReviewPage(dbHelper, currentUser).show(primaryStage);
        });

        // Button to open the Requests page
        Button viewRequestsBtn = new Button("View Requests");
        viewRequestsBtn.setOnAction(e -> {
            new RequestsPage(dbHelper, currentUser).show(primaryStage);
        });

        // Button to log out and return to login selection
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(dbHelper).show(primaryStage);
        });

        // Assemble UI layout
        layout.getChildren().addAll(reviewContentBtn, viewRequestsBtn, logoutBtn);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Home");
        primaryStage.show();
    }
}