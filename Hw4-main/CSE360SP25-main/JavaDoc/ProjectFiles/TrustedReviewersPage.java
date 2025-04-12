// File: TrustedReviewersPage.java

/**
 * <p><b>Title:</b> TrustedReviewersPage</p>
 * 
 * <p><b>Description:</b> This class provides a page for managing trusted reviewers for a student. 
 * Only an admin has access to this page, and it allows admins to load, add, and remove trusted 
 * reviewers for a student.</p>
 * 
 * <p><b>Author:</b> Lynn Robert Carter</p>
 * 
 * @version 1.00 — 2025-04-01 Initial implementation
 */
public class TrustedReviewersPage {

    /** The DatabaseHelper instance used for interacting with the database */
    private DatabaseHelper dbHelper;

    /** The currently logged-in user */
    private User currentUser;

    /**
     * Constructs a TrustedReviewersPage with the specified DatabaseHelper and currentUser.
     *
     * @param dbHelper The helper class used to execute database operations.
     * @param currentUser The admin currently logged in.
     */
    public TrustedReviewersPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    /**
     * Displays the Trusted Reviewers Management page.
     * Allows the admin to:
     * <ul>
     *   <li>Load a student's trusted reviewers</li>
     *   <li>Add or update a trusted reviewer</li>
     *   <li>Remove an existing trusted reviewer</li>
     * </ul>
     *
     * @param primaryStage The primary JavaFX stage.
     */
    public void show(Stage primaryStage) {
        // Ensure access is restricted to admins only
        if (!currentUser.getRoles().contains("admin")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You do not have permission to view this page.");
            alert.showAndWait();
            return;
        }

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label title = new Label("Manage Trusted Reviewers for a Student");

        // Input for the student username
        TextField studentField = new TextField();
        studentField.setPromptText("Enter student username");

        // Button to load the list of trusted reviewers
        Button loadBtn = new Button("Load Trusted Reviewers");

        // ListView to display all trusted reviewers for the specified student
        ListView<TrustedReviewer> listView = new ListView<>();
        ObservableList<TrustedReviewer> observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);

        // Custom cell formatting for trusted reviewer list
        listView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(TrustedReviewer tr, boolean empty) {
                super.updateItem(tr, empty);
                if (empty || tr == null) {
                    setText(null);
                } else {
                    setText("Reviewer: " + tr.getReviewerUserName() + " (Weight: " + tr.getWeight() + ")");
                }
            }
        });

        // Action to load the list of trusted reviewers
        loadBtn.setOnAction(e -> {
            String owner = studentField.getText();
            if (owner == null || owner.isBlank()) return;
            List<TrustedReviewer> list = dbHelper.getTrustedReviewers(owner);
            observableList.setAll(list);
        });

        // Input for adding a new trusted reviewer
        TextField reviewerField = new TextField();
        reviewerField.setPromptText("Reviewer username");
        Spinner<Integer> weightSpinner = new Spinner<>(1, 10, 5);

        // Add or update reviewer action
        Button addBtn = new Button("Add Reviewer");
        addBtn.setOnAction(e -> {
            String owner = studentField.getText();
            String reviewer = reviewerField.getText();
            int weight = weightSpinner.getValue();
            if (owner.isBlank() || reviewer.isBlank()) return;
            dbHelper.addOrUpdateTrustedReviewer(owner, reviewer, weight);
            observableList.setAll(dbHelper.getTrustedReviewers(owner));
            reviewerField.clear();
        });

        // Remove reviewer from trusted list
        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            TrustedReviewer sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) {
                dbHelper.removeTrustedReviewer(studentField.getText(), sel.getReviewerUserName());
                observableList.setAll(dbHelper.getTrustedReviewers(studentField.getText()));
            }
        });

        // Go back to Admin Home
        Button backBtn = new Button("Back to Admin Home");
        backBtn.setOnAction(e -> {
            new AdminHomePage().show(primaryStage, currentUser);
        });

        // Assemble layout and display
        layout.getChildren().addAll(
                title,
                studentField,
                loadBtn,
                listView,
                reviewerField,
                weightSpinner,
                addBtn,
                removeBtn,
                backBtn
        );

        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trusted Reviewers Management");
        primaryStage.show();
    }
}