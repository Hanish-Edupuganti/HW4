/**
 * The InvitationPage class represents a page where an admin can generate an invitation code.
 * Upon clicking a button, the invitation code is generated and displayed to the user.
 */
package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InvitationPage {

    /**
     * Displays the Invitation Page in the provided primary stage.
     * This page allows an admin to generate an invitation code and view it.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
        // Create a vertical layout for aligning components
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the page title
        Label userLabel = new Label("Invite");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button that triggers the invitation code generation
        Button showCodeButton = new Button("Generate Invitation Code");

        // Label to display the generated invitation code
        Label inviteCodeLabel = new Label("");  // Initially empty
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

        // Define behavior when the "Generate Invitation Code" button is clicked
        showCodeButton.setOnAction(a -> {
            // Use the DatabaseHelper to generate the invitation code
            String invitationCode = databaseHelper.generateInvitationCode();

            // Display the generated code in the label
            inviteCodeLabel.setText(invitationCode);
        });

        // Add UI elements to the layout
        layout.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel);

        // Create and configure the scene
        Scene inviteScene = new Scene(layout, 800, 400);

        // Set the scene on the primary stage and apply the title
        primaryStage.setScene(inviteScene);
        primaryStage.setTitle("Invite Page");
    }
}