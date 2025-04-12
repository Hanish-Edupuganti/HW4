package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StaffHomePage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    public StaffHomePage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Button reviewContentBtn = new Button("Review All Content");
        reviewContentBtn.setOnAction(e -> {
            new ContentReviewPage(dbHelper, currentUser).show(primaryStage);
        });

        Button viewRequestsBtn = new Button("View Requests");
        viewRequestsBtn.setOnAction(e -> {
            new RequestsPage(dbHelper, currentUser).show(primaryStage);
        });

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            new SetupLoginSelectionPage(dbHelper).show(primaryStage);
        });

        layout.getChildren().addAll(reviewContentBtn, viewRequestsBtn, logoutBtn);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Home");
        primaryStage.show();
    }
}
