package Model;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HardShape implements Shape {

    @Override
    public void draw() {
        // Ensure JavaFX application thread is running
        Platform.runLater(() -> {
            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED); // Removes the "X" button and title bar
            popupStage.setAlwaysOnTop(true);

            // Create a label to display the message
            Label message = new Label("Hard question");
            message.setTextFill(Color.WHITE); // Text color
            message.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Text font and size

            // Create a StackPane for background and center alignment
            StackPane root = new StackPane(message);
            root.setStyle("-fx-background-color: #FF0000; -fx-padding: 20px; -fx-border-radius: 10px; -fx-background-radius: 10px;"); // Stylish background and rounded corners
            root.setAlignment(Pos.CENTER);

            // Create a Scene with the styled StackPane
            Scene scene = new Scene(root, 300, 150);
            popupStage.setScene(scene);
            popupStage.show();

            // Close the popup after 3 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(popupStage::close);
            }).start();
        });
    }
}
