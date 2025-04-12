package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class RequestsPage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    public RequestsPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        Label title = new Label("Requests");

        List<Request> requestList = dbHelper.getAllRequests();
        ObservableList<Request> obsList = FXCollections.observableArrayList(requestList);
        ListView<Request> listView = new ListView<>(obsList);
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

        if (currentUser.getRoles().contains("instructor")) {
            TextArea requestDesc = new TextArea();
            requestDesc.setPromptText("Describe your request here...");
            Button createBtn = new Button("Create Request");
            createBtn.setOnAction(e -> {
                String desc = requestDesc.getText();
                if (!desc.isBlank()) {
                    dbHelper.createRequest(currentUser.getUserName(), desc);
                    obsList.setAll(dbHelper.getAllRequests());
                    requestDesc.clear();
                }
            });

            Button reopenBtn = new Button("Reopen Selected");
            reopenBtn.setOnAction(e -> {
                Request selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null && "CLOSED".equalsIgnoreCase(selected.getStatus())) {
                    dbHelper.reopenRequest(selected.getRequestId(), "Reopened request with updated details.");
                    obsList.setAll(dbHelper.getAllRequests());
                }
            });
            layout.getChildren().addAll(requestDesc, createBtn, reopenBtn);
        }

        if (currentUser.getRoles().contains("admin")) {
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

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            if (currentUser.getRoles().contains("admin")) {
                new AdminHomePage().show(primaryStage, currentUser);
            } else if (currentUser.getRoles().contains("instructor")) {
                new InstructorHomePage(dbHelper, currentUser).show(primaryStage);
            } else if (currentUser.getRoles().contains("staff")) {
                new StaffHomePage(dbHelper, currentUser).show(primaryStage);
            }
        });

        layout.getChildren().addAll(title, listView, backBtn);
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Requests");
        primaryStage.show();
    }
}
