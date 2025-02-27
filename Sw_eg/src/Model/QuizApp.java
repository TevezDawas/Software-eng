package Model;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp extends Application {

    private List<Question> questions = new ArrayList<>();
    private boolean isAnswerCorrect = false;

    public static void main(String[] args) {
        launch(args);
    }

    public boolean startQuiz(Stage stage, String difficultyLevel) {
        start(stage, difficultyLevel);
        return isAnswerCorrect;
    }
	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

    public void start(Stage stage, String difficultyLevel) {
        stage.setTitle("Quiz Game");

        loadQuestionsFromFile("src/Model/questions.json");

        Question randomQuestion = getRandomQuestionByDifficulty(difficultyLevel);
        if (randomQuestion != null) {
            VBox root = createQuestionPane(randomQuestion, stage);
            Scene scene = new Scene(root, 700, 300);
            Stage quizStage = new Stage();
            quizStage.setScene(scene);
            quizStage.initModality(Modality.APPLICATION_MODAL);
            quizStage.setTitle("Quiz Game");
            quizStage.showAndWait();
        } else {
            showAlert("No Questions", "No questions available for the selected difficulty.", stage, null);
        }
    }

    private void loadQuestionsFromFile(String filename) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray questionsArray = jsonObject.getJSONArray("questions");

            for (int i = 0; i < questionsArray.length(); i++) {
                JSONObject questionObj = questionsArray.getJSONObject(i);

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
        } catch (Exception e) {
            showAlert("File Error", "Error loading questions: " + e.getMessage(), null, null);
        }
    }

    private Question getRandomQuestionByDifficulty(String difficulty) {
        List<Question> filteredQuestions = new ArrayList<>();
        for (Question q : questions) {
            if (q.getDifficulty().equals(difficulty)) {
                filteredQuestions.add(q);
            }
        }
        if (!filteredQuestions.isEmpty()) {
            Collections.shuffle(filteredQuestions);
            return filteredQuestions.get(0);
        }
        return null;
    }

    private VBox createQuestionPane(Question question, Stage mainStage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label questionLabel = new Label("Question: " + question.getQuestion());
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        root.getChildren().add(questionLabel);

        ToggleGroup answersGroup = new ToggleGroup();

        List<RadioButton> answerButtons = new ArrayList<>();
        for (int i = 0; i < question.getAnswers().length; i++) {
            RadioButton answerButton = new RadioButton((i + 1) + ". " + question.getAnswers()[i]);
            answerButton.setToggleGroup(answersGroup);
            answerButtons.add(answerButton);
            root.getChildren().add(answerButton);
        }

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        submitButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) answersGroup.getSelectedToggle();
            if (selected == null) {
                showAlert("No Selection", "Please select an answer.", null, null);
                return;
            }

            int selectedIndex = answerButtons.indexOf(selected);
            Stage quizStage = (Stage) submitButton.getScene().getWindow();
            if (selectedIndex == question.getCorrectAnswerIndex()) {
                isAnswerCorrect = true;
                showAlert("Correct", "The answer is correct, move done.", mainStage, quizStage);
            } else {
                isAnswerCorrect = false;
                showAlert("Incorrect", "The answer is not correct, move can't be done.", mainStage, quizStage);
            }
        });

        root.getChildren().add(submitButton);
        ShapeFactory sf = new ShapeFactory();
        Shape s = sf.getShape(question.getDifficulty());
        s.draw();
        
        return root;
    }

    private void showAlert(String title, String message, Stage mainStage, Stage quizStage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        if (quizStage != null) {
            quizStage.close();
        }
        if (mainStage != null) {
            mainStage.close();
        }
    }
}