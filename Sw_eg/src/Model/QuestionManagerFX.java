 package Model;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

public class QuestionManagerFX extends Application {
    private ObservableList<Question> questions = FXCollections.observableArrayList();
    private TableView<Question> table = new TableView<>(questions);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Question Manager");

        loadQuestionsFromFile("src/Model/questions.json");

        // Define columns for the table
        TableColumn<Question, String> questionColumn = new TableColumn<>("Question");
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));

        TableColumn<Question, String> answersColumn = new TableColumn<>("Answers");
        answersColumn.setCellValueFactory(new PropertyValueFactory<>("answersFormatted"));

        TableColumn<Question, String> correctAnswerColumn = new TableColumn<>("Correct Answer");
        correctAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("correctAnswerFormatted"));

        TableColumn<Question, String> difficultyColumn = new TableColumn<>("Difficulty");
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        table.getColumns().addAll(questionColumn, answersColumn, correctAnswerColumn, difficultyColumn);

        // Buttons for Add, Edit, Remove
        Button addButton = new Button("Add Question");
        Button editButton = new Button("Edit Question");
        Button removeButton = new Button("Remove Question");

        addButton.setOnAction(e -> {
            showQuestionDialog(null);
            saveQuestionsToFile("src/Model/questions.json");
        });
        editButton.setOnAction(e -> {
            Question selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showQuestionDialog(selected);
                saveQuestionsToFile("src/Model/questions.json");
            } else {
                showAlert("No Selection", "Please select a question to edit.");
            }
        });
        removeButton.setOnAction(e -> {
            Question selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                questions.remove(selected);
                saveQuestionsToFile("src/Model/questions.json");
            } else {
                showAlert("No Selection", "Please select a question to remove.");
            }
        });

        HBox buttonPanel = new HBox(10, addButton, editButton, removeButton);

        // Layout
        VBox layout = new VBox(10, table, buttonPanel);
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadQuestionsFromFile(String filename) {
        try {
            // Read the file contents
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObj = questionsArray.getJSONObject(i);

                // Extract question details
                String question = questionObj.getString("question");
                JSONArray answersArray = questionObj.getJSONArray("answers");
                String correctAns = questionObj.getString("correct_ans");
                String difficulty = questionObj.getString("difficulty");

                String[] answers = new String[answersArray.length()];
                for (int j = 0; j < answersArray.length(); j++) {
                    answers[j] = answersArray.getString(j);
                }

                int correctAnswerIndex = Integer.parseInt(correctAns);
                questions.add(new Question(question, answers, correctAnswerIndex, difficulty));
            }
        } catch (IOException e) {
            showAlert("File Error", "Error reading file: " + e.getMessage());
        } catch (Exception e) {
            showAlert("JSON Error", "Error parsing JSON data: " + e.getMessage());
        }
    }

    private void saveQuestionsToFile(String filename) {
        try {
            JSONArray questionsArray = new JSONArray();
            for (Question q : questions) {
                JSONObject questionObj = new JSONObject();
                questionObj.put("question", q.getQuestion());
                questionObj.put("answers", new JSONArray(q.getAnswers()));
                questionObj.put("difficulty", q.getDifficulty());
                questionObj.put("correct_ans", String.valueOf(q.getCorrectAnswerIndex()));
               
                questionsArray.put(questionObj);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("questions", questionsArray);

            Files.write(Paths.get(filename), jsonObject.toString(4).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            showAlert("File Error", "Error saving to file: " + e.getMessage());
        }
    }
    private void showQuestionDialog(Question question) {
        Dialog<Question> dialog = new Dialog<>();
        dialog.setTitle((question == null) ? "Add Question" : "Edit Question");

        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType okButtonType = ButtonType.OK;
        dialogPane.getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField questionField = new TextField();
        questionField.setPromptText("Enter the question");
        TextField[] answersFields = new TextField[4];
        RadioButton[] radioButtons = new RadioButton[4];
        ToggleGroup group = new ToggleGroup();

        grid.add(new Label("Question:"), 0, 0);
        grid.add(questionField, 1, 0);
        for (int i = 0; i < 4; i++) {
            answersFields[i] = new TextField();
            answersFields[i].setPromptText("Answer " + (i + 1));
            radioButtons[i] = new RadioButton();
            radioButtons[i].setToggleGroup(group);
            grid.add(new Label("Answer " + (i + 1) + ":"), 0, i + 1);
            grid.add(answersFields[i], 1, i + 1);
            grid.add(radioButtons[i], 2, i + 1);
        }

        TextField difficultyField = new TextField();
        difficultyField.setPromptText("Enter difficulty level (1-3)");
        grid.add(new Label("Difficulty:"), 0, 5);
        grid.add(difficultyField, 1, 5);

        if (question != null) {
            questionField.setText(question.getQuestion());
            String[] answers = question.getAnswers();
            for (int i = 0; i < answers.length; i++) {
                answersFields[i].setText(answers[i]);
                if (i == question.getCorrectAnswerIndex()) {
                    radioButtons[i].setSelected(true);
                }
            }
            difficultyField.setText(question.getDifficulty());
        }

        dialogPane.setContent(grid);

        // Get the OK button and attach a custom event filter
        Button okButton = (Button) dialogPane.lookupButton(okButtonType);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String[] answers = new String[4];
            int correctAnswer = -1;

            for (int i = 0; i < 4; i++) {
                answers[i] = answersFields[i].getText().trim();
                if (answers[i].isEmpty()) {
                    showAlert("Invalid Input", "All answer fields must be filled.");
                    event.consume(); // Prevent dialog from closing
                    return;
                }
                if (radioButtons[i].isSelected()) {
                    correctAnswer = i;
                }
            }

            if (correctAnswer == -1) {
                showAlert("Invalid Input", "A correct answer must be selected.");
                event.consume(); // Prevent dialog from closing
                return;
            }

            String questionText = questionField.getText().trim();
            if (questionText.isEmpty()) {
                showAlert("Invalid Input", "Question field must not be empty.");
                event.consume(); // Prevent dialog from closing
                return;
            }

            String difficulty = difficultyField.getText().trim();
            try {
                int difficultyLevel = Integer.parseInt(difficulty);
                if (difficultyLevel < 1 || difficultyLevel > 3) {
                    showAlert("Invalid Difficulty", "Difficulty must be between 1 and 3.");
                    event.consume(); // Prevent dialog from closing
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Difficulty", "Difficulty must be a number between 1 and 3.");
                event.consume(); // Prevent dialog from closing
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                String[] answers = new String[4];
                int correctAnswer = -1;
                for (int i = 0; i < 4; i++) {
                    answers[i] = answersFields[i].getText().trim();
                    if (radioButtons[i].isSelected()) {
                        correctAnswer = i;
                    }
                }

                return new Question(questionField.getText().trim(), answers, correctAnswer, difficultyField.getText().trim());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (question == null) {
                questions.add(result);
            } else {
                question.setQuestion(result.getQuestion());
                question.setAnswers(result.getAnswers());
                question.setCorrectAnswerIndex(result.getCorrectAnswerIndex());
                question.setDifficulty(result.getDifficulty());
                table.refresh();
            }
        });
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}