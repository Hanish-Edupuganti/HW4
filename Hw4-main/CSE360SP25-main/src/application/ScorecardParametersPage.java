package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScorecardParametersPage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    public ScorecardParametersPage(DatabaseHelper dbHelper, User currentUser) {
        this.dbHelper = dbHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label title = new Label("Set Reviewer Scorecard Parameters");

        // Example parameters: upvoteWeight, downvoteWeight, ratingWeight.
        TextField upvoteField = new TextField();
        upvoteField.setPromptText("Upvote Weight");
        TextField downvoteField = new TextField();
        downvoteField.setPromptText("Downvote Weight");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating Weight");

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> {
            int upvoteWeight = Integer.parseInt(upvoteField.getText());
            int downvoteWeight = Integer.parseInt(downvoteField.getText());
            int ratingWeight = Integer.parseInt(ratingField.getText());
            dbHelper.setScorecardParam("upvoteWeight", upvoteWeight);
            dbHelper.setScorecardParam("downvoteWeight", downvoteWeight);
            dbHelper.setScorecardParam("ratingWeight", ratingWeight);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Parameters saved.");
            alert.showAndWait();
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> new InstructorHomePage(dbHelper, currentUser).show(primaryStage));

        layout.getChildren().addAll(title, upvoteField, downvoteField, ratingField, saveBtn, backBtn);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Scorecard Parameters");
        primaryStage.show();
    }
}
