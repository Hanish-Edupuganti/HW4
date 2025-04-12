/**
 * <p>Title: RequestsPage</p>
 * 
 * <p>Description: This class is responsible for displaying and managing the requests made by users, 
 * allowing the creation, viewing, reopening, and closing of requests based on the user's role 
 * (instructor, admin, etc.).</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2025-04-01 Initial implementation
 */
public class RequestsPage {

    /**
     * The DatabaseHelper instance used for interacting with the database.
     */
    private DatabaseHelper dbHelper;

    /**
     * The current user who is logged into the application.
     */
    private User currentUser;

    /**
     * Constructs a RequestsPage with the specified DatabaseHelper and currentUser.
     *
     * @param dbHelper The DatabaseHelper instance used for database operations.
     * @param currentUser The current user who is logged into the system.
     */
    public RequestsPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the requests page in a new window.
     * The page is customized based on the role of the current user.
     *
     * @param primaryStage The primary stage where the scene will be set.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label title = new Label("Requests");

        // Retrieve all requests from the database and wrap in observable list for UI
        List<Request> requestList = dbHelper.getAllRequests();
        ObservableList<Request> obsList = FXCollections.observableArrayList(requestList);

        // ListView to display requests
        ListView<Request> listView = new ListView<>(obsList);

        // Custom formatting for how each request appears in the list
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Request req, boolean empty) {
                super.updateItem(req, empty);
                if (empty || req == null) {
                    setText(null);
                } else {
                    setText("Request #" + req.getRequestId() + " | " + req.getStatus()
                            + "\nCreated by: " + req.getCreatedBy()
                            + "\n" + req.getDescription()
                            + "\nNotes: " + req.getNotes());
                }
            }
        });

        // ---------- INSTRUCTOR VIEW ----------
        if (currentUser.getRoles().contains("instructor")) {
            TextArea requestDesc = new TextArea();
            requestDesc.setPromptText("Describe your request here...");

            // Button to create new request
            Button createBtn = new Button("Create Request");
            createBtn.setOnAction(e -> {
                String desc = requestDesc.getText();
                if (!desc.isBlank()) {
                    dbHelper.createRequest(currentUser.getUserName(), desc);
                    obsList.setAll(dbHelper.getAllRequests());
                    requestDesc.clear();
                }
            });

            // Button to reopen a selected closed request
            Button reopenBtn = new Button("Reopen Selected");
            reopenBtn.setOnAction(e -> {
                Request selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null && "CLOSED".equalsIgnoreCase(selected.getStatus())) {
                    dbHelper.reopenRequest(selected.getRequestId(), "Reopened request with updated details.");
                    obsList.setAll(dbHelper.getAllRequests());
                }
            });

            // Add instructor controls to layout
            layout.getChildren().addAll(requestDesc, createBtn, reopenBtn);
        }

        // ---------- ADMIN VIEW ----------
        if (currentUser.getRoles().contains("admin")) {
            // Button to close a selected open request
            Button closeBtn = new Button("Close Selected");
            closeBtn.setOnAction(e -> {
                Request selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null && !"CLOSED".equalsIgnoreCase(selected.getStatus())) {
                    dbHelper.closeRequest(selected.getRequestId(), "Admin closed the request.");
                    obsList.setAll(dbHelper.getAllRequests());
                }
            });
            layout.getChildren().add(closeBtn);
        }

        // ---------- BACK BUTTON ----------
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            // Navigate to the appropriate home page based on user role
            if (currentUser.getRoles().contains("admin")) {
                new AdminHomePage().show(primaryStage, currentUser);
            } else if (currentUser.getRoles().contains("instructor")) {
                new InstructorHomePage(dbHelper, currentUser).show(primaryStage);
            } else if (currentUser.getRoles().contains("staff")) {
                new StaffHomePage(dbHelper, currentUser).show(primaryStage);
            }
        });

        // Final layout arrangement and scene setup
        layout.getChildren().addAll(title, listView, backBtn);
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Requests");
        primaryStage.show();
    }
}