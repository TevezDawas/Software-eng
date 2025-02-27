package Model;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginPopup extends Application {

    private boolean isAuthenticated = false;

    @Override
    public void start(Stage primaryStage) {
        // We do NOT show the primaryStage at all.
        // If you do not have another window to show, the application will appear
        // to do nothing after the login popup closes (unless you add more logic).
        showLoginPopup();
    }

    private void showLoginPopup() {
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setTitle("Login");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button cancelButton = new Button("Cancel");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if ("admin".equals(username) && "admin".equals(password)) {
                System.out.println("Login successful!");
                isAuthenticated = true;
                // Close just this window
                new QuestionManagerFX().start(new Stage());
                loginStage.close();
            } else {
                System.out.println("Invalid username or password.");
                // Clear the fields to allow another attempt
                usernameField.clear();
                passwordField.clear();
            }
        });

        cancelButton.setOnAction(e -> {
            // Simply close this window (do not exit the application)
            loginStage.close();
        });

        // If the user presses the 'X' at the top-right corner,
        // just close this window rather than exiting the entire program.
        loginStage.setOnCloseRequest(event -> {
            event.consume();
            loginStage.close();
        });

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);

        HBox buttonBox = new HBox(10, loginButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        gridPane.add(buttonBox, 1, 2);

        Scene popupScene = new Scene(gridPane, 300, 150);
        loginStage.setScene(popupScene);

        // Show the login popup (blocks until it is closed)
        loginStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
