package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;

public class TrustedReviewersPage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    public TrustedReviewersPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        // Ensure that only an admin can access this page.
        if (!currentUser.getRoles().contains("admin")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You do not have permission to view this page.");
            alert.showAndWait();
            return;
        }
        
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        Label title = new Label("Manage Trusted Reviewers for a Student");

        // Input field for entering the student's username.
        TextField studentField = new TextField();
        studentField.setPromptText("Enter student username");

        Button loadBtn = new Button("Load Trusted Reviewers");
        ListView<TrustedReviewer> listView = new ListView<>();
        ObservableList<TrustedReviewer> observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);

        listView.setCellFactory(lv -> new ListCell<TrustedReviewer>() {
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

        loadBtn.setOnAction(e -> {
            String owner = studentField.getText();
            if (owner == null || owner.isBlank()) {
                return;
            }
            List<TrustedReviewer> list = dbHelper.getTrustedReviewers(owner);
            observableList.setAll(list);
        });

        // UI to add a new trusted reviewer for the student.
        TextField reviewerField = new TextField();
        reviewerField.setPromptText("Reviewer username");
        Spinner<Integer> weightSpinner = new Spinner<>(1, 10, 5);
        Button addBtn = new Button("Add Reviewer");
        addBtn.setOnAction(e -> {
            String owner = studentField.getText();
            String reviewer = reviewerField.getText();
            int weight = weightSpinner.getValue();
            if (owner.isBlank() || reviewer.isBlank()) {
                return;
            }
            dbHelper.addOrUpdateTrustedReviewer(owner, reviewer, weight);
            observableList.setAll(dbHelper.getTrustedReviewers(owner));
            reviewerField.clear();
        });

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            TrustedReviewer sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) {
                dbHelper.removeTrustedReviewer(studentField.getText(), sel.getReviewerUserName());
                observableList.setAll(dbHelper.getTrustedReviewers(studentField.getText()));
            }
        });

        Button backBtn = new Button("Back to Admin Home");
        backBtn.setOnAction(e -> {
            new AdminHomePage().show(primaryStage, currentUser);
        });

        layout.getChildren().addAll(title, studentField, loadBtn, listView, reviewerField, weightSpinner, addBtn, removeBtn, backBtn);
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trusted Reviewers Management");
        primaryStage.show();
    }
}
