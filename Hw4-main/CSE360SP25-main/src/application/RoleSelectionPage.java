package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class RoleSelectionPage {

    private User user;
    private DatabaseHelper dbHelper;

    public RoleSelectionPage(User user, DatabaseHelper dbHelper) {
        this.user = user;
        this.dbHelper = dbHelper;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label prompt = new Label("Select your role (this choice is permanent):");
        layout.getChildren().add(prompt);

        List<String> roles = user.getRoles();
        for (String r : roles) {
            Button btn = new Button(r);
            btn.setOnAction(e -> {
                // Overwrite the user's roles with this single choice.
                dbHelper.setSingleRoleForUser(user.getUserName(), r);
                user.setRole(r);
                // Navigate to the appropriate home page.
                if (r.equalsIgnoreCase("admin")) {
                    new AdminHomePage().show(primaryStage, user);
                } else {
                    new UserHomePage().show(primaryStage, user);
                }
            });
            layout.getChildren().add(btn);
        }

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Role");
        primaryStage.show();
    }
}
