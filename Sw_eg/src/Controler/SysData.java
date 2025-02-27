package Controler; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Model.Question;

public class SysData {
	private ArrayList<Question> questions=new ArrayList<Question>();
	public SysData() {
		
		this.questions = new ArrayList<>();
		loadQuestionsFromFile("src/Model/questions.json");
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
	            System.out.println(e.getMessage());
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        }
	    }

}
