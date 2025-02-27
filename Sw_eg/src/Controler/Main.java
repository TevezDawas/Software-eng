package Controler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

import Model.GameRecord;
import Model.RandomPip;
import Model.addGame;
import Model.gameRecordsManagerFX;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;


/**
 * This class runs the entire application.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
/*this is main*/


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);	// calls start method.
	}
	//tevez
	@Override
	public void start(Stage stage) throws Exception {
		System.out.println("after MVC");
		System.out.println("after mvc2");
		System.out.println("after mvc7");
		System.out.println("ayman photos");
		System.out.println("ayman photos2");
		RandomPip.runAll();
//		System.out.println(RandomPip.firstRandomPipQ);
//		System.out.println(RandomPip.secondRandomPipQ);
//		System.out.println(RandomPip.thRandomPipQ);
//		System.out.println(RandomPip.RandomPipS);
		MatchController root = new MatchController(stage);
		Scene scene = new Scene(root);
		
		
		
		stage.setScene(scene);
		stage.setTitle("Backgammon");
//		//////////////////
	
		
		
//		//////////////////
		stage.show();
		setStageIcon(stage);
		
		// these must be set only after stage is shown.
		root.setRollDiceAccelarator();
		root.requestFocus();
		

		

		
		}
	
	/**
	 * Set the application's icon.
	 * @param stage, the stage of the application.
	 */
	public void setStageIcon(Stage stage) {
		try {
			InputStream input = Main.class.getResourceAsStream("/Model/img/icon/icon.png");
			stage.getIcons().add(new Image(input));
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
	
	
//	private void menu_game() {
//	    // Create a VBox for the contents
//	    VBox contents = new VBox();
//	    contents.setSpacing(20);  // Adjust spacing between buttons
//	    contents.setStyle("-fx-background-color: #A5D6A7; -fx-padding: 70px;");
//	    contents.setAlignment(Pos.CENTER);
//
//	    // Title Label
//	    Label welcomeLabel = new Label("Welcome! Let's start the Model");
//	    welcomeLabel.setStyle("-fx-font-size: 24px; "
//	            + "-fx-font-weight: bold; "
//	            + "-fx-text-fill: #2C6B2F;");
//
//	    // Buttons
//	    Button startButton = createFlatButton("Start Game", "#388E3C");
//	    Button historyButton = createFlatButton("History", "#1976D2");
//	    Button manageButton = createFlatButton("Manage Questions", "#FFC107");
//	    Button exitButton = createFlatButton("Exit", "#FF5722");
//
//	    // Add components
//	    contents.getChildren().addAll(welcomeLabel, startButton, historyButton, manageButton, exitButton);
//
//	    // Create scene
//	    Scene scene = new Scene(contents, 600, 500);
//	    Stage primaryStage = new Stage();
//	    primaryStage.setTitle("Game Start");
//	    primaryStage.setScene(scene);
//	    primaryStage.show();
//
//	    // Button actions
////	    CommandController command = new CommandController();
//	    
//	    startButton.setOnAction(event ->{
////	    		command.runCommand("/start");
//	    		System.out.println("Starting the Model...");
//	    		primaryStage.close();
//	    });
//	    historyButton.setOnAction(event ->
//	    	System.out.println("Viewing history...")
//	    	);
//	    manageButton.setOnAction(event ->
//	    	System.out.println("Managing questions...")
//	    	);
//	    exitButton.setOnAction(event -> {
//	        System.out.println("Exiting the Model...");
//	        primaryStage.close();
//	    });
//	}
//
//	// Method to create clean, flat buttons
//	private Button createFlatButton(String text, String bgColor) {
//	    Button button = new Button(text);
//	    button.setStyle("-fx-background-color: " + bgColor + "; "
//	            + "-fx-text-fill: white; "
//	            + "-fx-font-size: 20px; "
//	            + "-fx-font-weight: bold; "
//	            + "-fx-border-radius: 20px; "
//	            + "-fx-background-radius: 20px; "
//	            + "-fx-padding: 15px 30px; "
//	            + "-fx-cursor: hand;");
//	    button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + lightenColor(bgColor) + "; "
//	            + "-fx-text-fill: white; "
//	            + "-fx-font-size: 20px; "
//	            + "-fx-font-weight: bold; "
//	            + "-fx-border-radius: 20px; "
//	            + "-fx-background-radius: 20px; "
//	            + "-fx-padding: 15px 30px; "
//	            + "-fx-cursor: hand;"));
//	    button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + bgColor + "; "
//	            + "-fx-text-fill: white; "
//	            + "-fx-font-size: 20px; "
//	            + "-fx-font-weight: bold; "
//	            + "-fx-border-radius: 20px; "
//	            + "-fx-background-radius: 20px; "
//	            + "-fx-padding: 15px 30px; "
//	            + "-fx-cursor: hand;"));
//	    return button;
//	}
//
//	// Helper to lighten colors on hover
//	private String lightenColor(String color) {
//	    switch (color) {
//	        case "#388E3C": return "#45A049";
//	        case "#1976D2": return "#1565C0";
//	        case "#FFC107": return "#FFD54F";
//	        case "#FF5722": return "#FF7043";
//	        default: return color;
//	    }
//	}
//
//}
