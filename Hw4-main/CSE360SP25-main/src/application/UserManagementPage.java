package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class UserManagementPage {

    private databasePart1.DatabaseHelper dbHelper;

    public UserManagementPage(databasePart1.DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label title = new Label("User Management");

        // ListView to display users.
        ListView<User> userListView = new ListView<>();
        List<User> users = dbHelper.getAllUsers(); // Ensure getAllUsers() exists in DatabaseHelper.
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        userListView.setItems(userObservableList);

        // Customize list cell to show username and roles.
        userListView.setCellFactory(listView -> new ListCell<User>() {
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

        // Delete user button.
        Button deleteBtn = new Button("Delete Selected User");
        deleteBtn.setOnAction(e -> {
            User selected = userListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            // Prevent deletion of the only admin.
            if (selected.getRole().contains("admin")) {
                long adminCount = users.stream().filter(u -> u.getRole().contains("admin")).count();
                if (adminCount <= 1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete the only admin!");
                    alert.showAndWait();
                    return;
                }
            }
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

        Button backBtn = new Button("Back to Admin Home");
        backBtn.setOnAction(e -> new AdminHomePage().show(primaryStage, new User("admin", "", "admin"))); // Replace with current admin user.

        layout.getChildren().addAll(title, userListView, deleteBtn, backBtn);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Management");
        primaryStage.show();
    }
}
