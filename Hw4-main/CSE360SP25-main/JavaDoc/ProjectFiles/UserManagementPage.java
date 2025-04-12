// File: UserManagementPage.java

/**
 * <p>Title: UserManagementPage</p>
 * 
 * <p>Description: This class provides a user interface for administrators to manage user accounts.
 * Admins can view all users, see their roles, and delete selected users (with safeguards in place
 * such as preventing the deletion of the last remaining admin).</p>
 * 
 * <p>Copyright: ©️ 2022</p>
 * 
 * @version 1.00	2025-04-01 Initial implementation
 */
public class UserManagementPage {

    /**
     * The database helper used to interact with the backend database.
     */
    private databasePart1.DatabaseHelper dbHelper;

    /**
     * Constructor to initialize the UserManagementPage with the given database helper.
     *
     * @param dbHelper The DatabaseHelper instance for performing DB operations.
     */
    public UserManagementPage(databasePart1.DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Displays the User Management page on the specified primary stage.
     * This page includes a list of users, a delete button, and a back button.
     *
     * @param primaryStage The main JavaFX window where this page will be shown.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label title = new Label("User Management");

        // --- ListView to display users fetched from the database ---
        ListView<User> userListView = new ListView<>();
        List<User> users = dbHelper.getAllUsers(); // Get all users
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        userListView.setItems(userObservableList);

        // Customize how each user is shown in the list (username and roles)
        userListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(User u, boolean empty) {
                super.updateItem(u, empty);
                if (empty || u == null) {
                    setText(null);
                } else {
                    setText("Username: " + u.getUserName() + ", Roles: " + u.getRole());
                }
            }
        });

        // --- Button to delete the selected user ---
        Button deleteBtn = new Button("Delete Selected User");
        deleteBtn.setOnAction(e -> {
            User selected = userListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            // Prevent deleting the only admin
            if (selected.getRole().contains("admin")) {
                long adminCount = users.stream()
                        .filter(u -> u.getRole().contains("admin"))
                        .count();
                if (adminCount <= 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete the only admin!");
                    alert.showAndWait();
                    return;
                }
            }

            // Confirm deletion
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete user " + selected.getUserName() + "?",
                    ButtonType.YES, ButtonType.CANCEL);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    dbHelper.deleteUser(selected.getUserName());
                    userObservableList.remove(selected);
                }
            });
        });

        // --- Back button to return to the admin home page ---
        Button backBtn = new Button("Back to Admin Home");
        backBtn.setOnAction(e -> {
            // Ideally, you'd pass in the logged-in admin user instead of hardcoding "admin"
            new AdminHomePage().show(primaryStage, new User("admin", "", "admin"));
        });

        // Add all UI components to the layout
        layout.getChildren().addAll(title, userListView, deleteBtn, backBtn);

        // Set up and show the scene
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Management");
        primaryStage.show();
    }
}