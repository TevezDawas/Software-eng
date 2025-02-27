package Model;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class gameRecordsManagerFX extends Application {

    private List<GameRecord> gameRecords = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main JavaFX entry point.
     * Loads and displays all Model records without filtering by difficulty.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game Records App");

        // Load all Model records from the JSON file
        loadGameRecordsFromFile("src/Model/gameRecords.json");

        // If there are no records, show an alert and exit
        if (gameRecords.isEmpty()) {
            showAlert("No Records",
                      "No Model records found in the JSON file.",
                      null, null);
            return;
        }

        // Create a TableView to display all records
        TableView<GameRecord> tableView = createTableView(gameRecords);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setCenter(tableView);

        Scene scene = new Scene(root, 600, 400);
        Stage recordsStage = new Stage();
        recordsStage.setScene(scene);
        recordsStage.initModality(Modality.APPLICATION_MODAL);
        recordsStage.setTitle("All Game Records");
        recordsStage.showAndWait();
    }

    /**
     * Loads Model records from the specified JSON file into the `gameRecords` list.
     */
    private void loadGameRecordsFromFile(String filePath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray recordsArray = jsonObject.getJSONArray("gameRecords");

            gameRecords.clear(); // Clear old records if needed

            for (int i = 0; i < recordsArray.length(); i++) {
                JSONObject recordObj = recordsArray.getJSONObject(i);

                String datePlayed = recordObj.getString("datePlayed");
                String player1Name = recordObj.getString("player1Name");
                String player2Name = recordObj.getString("player2Name");
                String winnerName = recordObj.getString("winnerName");
                String difficulty = recordObj.getString("difficulty");

                GameRecord record = new GameRecord(
                    datePlayed, player1Name, player2Name, winnerName, difficulty
                );
                gameRecords.add(record);
            }

        } catch (Exception e) {
            showAlert("File Error",
                      "Error loading Model records: " + e.getMessage(),
                      null, null);
        }
    }

    /**
     * Creates a TableView to display the provided list of GameRecord objects.
     */
    private TableView<GameRecord> createTableView(List<GameRecord> records) {
        TableView<GameRecord> tableView = new TableView<>();

        TableColumn<GameRecord, String> dateCol = new TableColumn<>("Date Played");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("datePlayed"));

        TableColumn<GameRecord, String> p1Col = new TableColumn<>("Player 1");
        p1Col.setCellValueFactory(new PropertyValueFactory<>("player1Name"));

        TableColumn<GameRecord, String> p2Col = new TableColumn<>("Player 2");
        p2Col.setCellValueFactory(new PropertyValueFactory<>("player2Name"));

        TableColumn<GameRecord, String> winnerCol = new TableColumn<>("Winner");
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winnerName"));

        TableColumn<GameRecord, String> diffCol = new TableColumn<>("Difficulty");
        diffCol.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        // Add columns to the table
        tableView.getColumns().addAll(dateCol, p1Col, p2Col, winnerCol, diffCol);

        // Populate the table with all records
        tableView.setItems(FXCollections.observableArrayList(records));
        return tableView;
    }

    /**
     * Show an alert dialog; optionally close the provided stages if needed.
     */
    private void showAlert(String title, String message, Stage mainStage, Stage childStage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        if (childStage != null) {
            childStage.close();
        }
        if (mainStage != null) {
            mainStage.close();
        }
    }
}

