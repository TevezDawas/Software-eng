package Controler;

import java.util.Optional;

import Model.RandomPip;
import View.CommandPanel;
import View.Dialogs;
import View.InfoPanel;
import View.RollDieButton;
import View.ScoreboardPrompt;
import constants.GameConstants;
import constants.MessageType;
import constants.PlayerPerspectiveFrom;
import interfaces.ColorPerspectiveParser;
import interfaces.InputValidator;
import musicplayer.MusicPlayer;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class represents the entire component of the application,
 * consisting of the Model components and the UI components.
 * 
 * These components are children of this class, therefore
 * this class is the root in the layout structure/tree.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class MatchController extends GridPane implements ColorPerspectiveParser, InputValidator {
	private Player bottomPlayer;
	private Player topPlayer;
	private GameComponentsController game;
	private InfoPanel infoPnl;
	private RollDieButton rollDieBtn;
	private CommandPanel cmdPnl;
	private CommandController cmd;
	private GameplayController gameplay;
	private EventController event;
	private MusicPlayer musicPlayer;
	private Stage stage;
	private boolean isPlayerInfosEnteredFirstTime, isPromptCancel, hadCrawfordGame, isCrawfordGame;
	
	/**
	 * Default Constructor
	 * 		- Initialize all the components (Model, commandPanel, InfoPanel, RollDieButton, etc).
	 * 		- Initialize sub-controllers (Command, Gameplay, GameComponents, Event).
	 * 		- Initialize the layout of the components.
	 * 		- Style the application.
	 */
	
	public MatchController(Stage stage) {
		super();
		this.stage = stage;
		initApplication();
		initGame();
		style();
		
		
	}
	
	
	
	
	/**
	 * Initialize players and UI components.
	 */
	private void initApplication() {
		bottomPlayer = new Player(PlayerPerspectiveFrom.BOTTOM);
		topPlayer = new Player(PlayerPerspectiveFrom.TOP);
		infoPnl = new InfoPanel();
		rollDieBtn = new RollDieButton();
		cmdPnl = new CommandPanel();
		musicPlayer = new MusicPlayer();
		isPlayerInfosEnteredFirstTime = true;
		isPromptCancel = false;
		
		
	}
	
	/**
	 * Initialize Model components and sub-controllers.
	 */
	private void initGame() {
		game = new GameComponentsController(bottomPlayer, topPlayer);
		gameplay = new GameplayController(stage, this, game, infoPnl, bottomPlayer, topPlayer);
		cmd = new CommandController(stage, this, game, gameplay, infoPnl, cmdPnl, bottomPlayer, topPlayer, musicPlayer);
		gameplay.setCommandController(cmd);
		event = new EventController(stage, this, game, gameplay, cmdPnl, cmd, infoPnl, rollDieBtn);
		cmd.setEventController(event);
		initLayout();
	}
	
	public void resetApplication() {
		cmdPnl.reset();
		musicPlayer.reset();
		bottomPlayer.reset();
		topPlayer.reset();
		infoPnl.reset();
		resetGame();
		game.resetTimers();
		
		isPlayerInfosEnteredFirstTime = true;
		isPromptCancel = false;
		hadCrawfordGame = false;
		isCrawfordGame = false;
		Settings.setTotalGames(Settings.DEFAULT_TOTAL_GAMES);
	}
	
	public void resetGame() {
		bottomPlayer.setHasCube(false);
		topPlayer.setHasCube(false);
		game.reset();
		gameplay.reset();
		cmd.reset();
		event.reset();
	}
	
	/**
	 * Remove previous event listeners and start Model.
	 * Called every /start.
	 */
	public void restartGame() {
		resetGame();
		startGame();
	}
	
	/**
	 * Asks for player name and start the Model.
	 */
	private void startGame() {
		
		// prompt players for their infos only if it is their first time.
		if (isPlayerInfosEnteredFirstTime) {
			promptStartGame();
		}
		
		if (!isPromptCancel) {
			/* Handle Crawford Game */
			// if did not have a crawford Model,
			// we check if next Model is crawford Model.
			if (!hadCrawfordGame && checkIsCrawfordGame()) {
				isCrawfordGame = true;
				hadCrawfordGame = true;
				infoPnl.print("Current Model is a Crawford Model.");
			// if had crawford Model,
			// we check if the current Model is a crawford Model,
			// if it is, we reset.
			// giving us:
			// isCrawfordGame = false, hadCrawfordGame = true.
			} else if (isCrawfordGame) {
				isCrawfordGame = false;
				infoPnl.print("New Model is not a Crawford Model.", MessageType.DEBUG);
			}
			
			if (!hadCrawfordGame && GameConstants.FORCE_TEST_AFTER_CRAWFORD_RULE) {
				isCrawfordGame = true;
				hadCrawfordGame = true;
				infoPnl.print("Current Model is a Crawford Model.");
			}
			
			isPlayerInfosEnteredFirstTime = false;
			gameplay.start();
		}
	}
	// Checks if next Model is crawford Model.
	// is crawford Model either winner match score, i.e. TOTAL_GAMES_IN_A_MATCH-1.
	private boolean checkIsCrawfordGame() {
		return topPlayer.getScore() == Settings.TOTAL_GAMES_IN_A_MATCH-1 || bottomPlayer.getScore() == Settings.TOTAL_GAMES_IN_A_MATCH-1;
	}
	
	/**
	 * Returns true if match over.
	 * Match over if player score greater or equal than total games.
	 * @return boolean value indicating if match is over.
	 */
	public boolean isMatchOver() {
		return (topPlayer.getScore() >= Settings.TOTAL_GAMES_IN_A_MATCH) || (bottomPlayer.getScore() >= Settings.TOTAL_GAMES_IN_A_MATCH);
	}
	
	/**
	 * If a player has a score equal to the maximum score,
	 * then announce the winner and ask if they want to play again.
	 */
	public void handleMatchOver(boolean isOutOfTime) {
		Player winner;
		if (isOutOfTime) winner = gameplay.getOpponent();
		else winner = gameplay.getCurrent();
		
		Dialogs<ButtonType> dialog = new Dialogs<ButtonType>("Congratulations, " + winner.getShortName() + " wins the match!", stage, "Play again");
		
		ScoreboardPrompt contents = new ScoreboardPrompt(topPlayer, bottomPlayer);
		
		dialog.getDialogPane().setContent(contents);
		
		// Run later because this method will be called from a running animation.
		// This animation resides in GameplayTimer.java.
		//
		// Animations are handled via events, showAndWait() below relies on
		// events too.
		//
		// So, calling showAndWait() from a running animations means there is a
		// nested event loop. This cannot happen, so the code below will have
		// to run later.
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Optional<ButtonType> result = dialog.showAndWait();
				
				// Restart Model if player wishes,
				// else exit gameplay mode and enter free-for-all mode.
				if (ButtonType.OK.equals(result.get())) {
					resetApplication();
					cmd.runCommand("/start");
				} else {
					resetApplication();
					infoPnl.print("Enter /start if you wish to play again.", MessageType.ANNOUNCEMENT);
					infoPnl.print("Enter /quit if you wish to quit.", MessageType.ANNOUNCEMENT);
				}
			}
		});
	}
	public void handleMatchOver() {
		handleMatchOver(false);
	}
	
	/**
	 * Displays a dialog that prompts players to input
	 * 		- Player names and colors.
	 * 		- Score limit for this match.
	 * 
	 * NOTE: 
	 * 		- Players can start the Model with or without changing default round by clicking start.
	 * 		- Players can cancel starting the Model by clicking cancel.
	 * 		- Players can NOT have empty names.
	 * 		- Players can NOT have negative or even totalGames.
	 */
	private void promptStartGame() {
	    // Create dialog.
	    Dialogs<promptResults> dialog = new Dialogs<promptResults>("Please enter player names, number of games, and choose difficulty", stage, "Start");
	    
	    // Create dialog contents.
	    ScoreboardPrompt contents = new ScoreboardPrompt();
	    
	    // Create difficulty selection (ChoiceBox).
	    ChoiceBox<String> difficultyChoiceBox = new ChoiceBox<>();
	    difficultyChoiceBox.getItems().addAll("easy", "medium", "hard");
	    difficultyChoiceBox.setValue("easy"); // Default difficulty
	    
	    
	    // Add difficulty selection to the dialog's content.
	    contents.add(difficultyChoiceBox, 0, 2); // Assuming contents is a grid or similar layout, adjust position as needed
	    
	    // Add contents to dialog.
	    dialog.getDialogPane().setContent(contents);
	    
	    // On click start button, return player names and difficulty level as result.
	    // Else result is null, cancel the Model.
	    dialog.setResultConverter(click -> {
	        if (click == dialog.getButton()) {
	        	
	            String difficulty = difficultyChoiceBox.getValue();
	            RandomPip.mood = difficulty;
//	            System.out.println(RandomPip.mood);
	            return new promptResults(contents.getPlayerInput("black"), contents.getPlayerInput("white"), contents.getPlayerInput("score"), difficulty);
	        }
	        return null;
	    });
	    
	    // Show dialog to get player input.
	    Optional<promptResults> result = dialog.showAndWait();
	    
	    // If result is present and name is not empty, change player names and set difficulty.
	    // If result is null, cancel starting the Model.
	    result.ifPresent((promptResults results) -> {
	        if (results.bName.length() != 0)
	            cmd.runCommand("/name black " + results.bName);
	        if (results.wName.length() != 0)
	            cmd.runCommand("/name white " + results.wName);

	        String userInput = results.totalGames;
	        if (userInput.length() == 0 || isValidInput(userInput)) {
	            if (userInput.length() != 0) {
	                Settings.setTotalGames(Integer.parseInt(userInput));
	                game.getPlayerPanel(Settings.getTopPerspectiveColor()).updateTotalGames();
	                game.getPlayerPanel(Settings.getBottomPerspectiveColor()).updateTotalGames();
	            }
	            infoPnl.print("Max totalGames per Model set to " + Settings.TOTAL_GAMES_IN_A_MATCH + ".");
	            // Set the Model difficulty based on the user's choice.
	            Settings.setDifficulty(results.difficulty); 
	            infoPnl.print("Difficulty level set to: " + results.difficulty);
	            isPromptCancel = false;
	        } else {
	            infoPnl.print("You must play to a positive odd number less than 100. Please try again.", MessageType.ERROR);
	            isPromptCancel = true;
	            promptStartGame();
	        }
	    });
	    
	    if (!result.isPresent()) {
	        isPromptCancel = true;
	        infoPnl.print("Game not started.");
	    }
	}
	/**
	 * Inner class that stores results of promptStartGame() so we can process user input.
	 * 		- Player names + total games they play to
	 * NOTE: totalGames is a string here so we can validate player input before processing it
	 */
	public static class promptResults {
		String bName;
		String wName;
		String totalGames;
		String  difficulty;
		
		

		
		public promptResults(String bName, String wName, String totalGames, String difficulty) {
	        this.bName = bName;
	        this.wName = wName;
	        this.totalGames = totalGames;
	        this.difficulty = difficulty;
	    }
		public String getDifficulty() {
		    return  this.difficulty;
		}
		
	}
	// valid input if:
	// - is a number.
	// - positive.
	// - odd.
	private boolean isValidInput(String userInput) {
		boolean isValidInput = false;
		// is a number.
		if (isNumber(userInput)) {
			int num = Integer.parseInt(userInput);
			
			// positive and odd.
			if (num > 0 && num % 2 != 0 && num < 100) {
				isValidInput = true;
			}
		}
		return isValidInput;
	}
	
	/**
	 * Style MainController (i.e. root).
	 */
	public void style() {
		//setStyle("-fx-font-size: " + GameConstants.FONT_SIZE + "px; -fx-font-family: '" + GameConstants.FONT_FAMILY + "';");
		setBackground(GameConstants.getTableImage());
		setPadding(new Insets(10));
		setVgap(GameConstants.getUIVGap());
		setHgap(5);
		setAlignment(Pos.CENTER);
		setMaxSize(GameConstants.getScreenSize().getWidth(), GameConstants.getScreenSize().getHeight());
		
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> menu_game()); // Show menu after delay
        delay.play();
//		
//		menu_game();
	}

	/**
	 * Manages the layout of the children, then adds them as the child of MainController (i.e. root).
	 */
	public void initLayout() {
		VBox terminal = new VBox();
		terminal.getChildren().addAll(infoPnl, cmdPnl);
		terminal.setAlignment(Pos.CENTER);
		terminal.setEffect(new DropShadow(20, 0, 0, Color.BLACK));
		
		getChildren().clear();
		add(game, 0, 0, 1, 3);
		add(terminal, 1, 0);
		add(rollDieBtn, 1, 2);
	}
	
	/**
	 * DO NOT TOUCH THIS OR ADD THIS ANYWHERE ELSE,
	 * KEEP IN MIND THIS METHOD IS CALLED AFTER THE STAGE IS DONE SHOWING.
	 * ALTERNATIVE METHOD WHERE I DON'T HAVE TO DO THE ABOVE IS PREFERRED.
	 * 
	 * Binds shortcut CTRL+R key combination to the roll dice button.
	 */
	public void setRollDiceAccelarator() {
		Scene scene = rollDieBtn.getScene();
		if (scene == null) {
			throw new IllegalArgumentException("Roll Dice Button not attached to a scene.");
		}
		
		scene.getAccelerators().put(
			new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
			new Runnable() {
				@Override
				public void run() {
					rollDieBtn.fire();
				}
			}
		);
	}
	
	public boolean isCrawfordGame() {
		return isCrawfordGame;
	}
	
	
	private void menu_game() {
	    // Create a VBox for the contents
	    VBox contents = new VBox();
	    contents.setSpacing(20);  // Adjust spacing between buttons
	    contents.setStyle("-fx-background-color: #A5D6A7; -fx-padding: 70px;");
	    contents.setAlignment(Pos.CENTER);

	    // Title Label
	    Label welcomeLabel = new Label("Welcome! Let's start the Model");
	    welcomeLabel.setStyle("-fx-font-size: 24px; "
	            + "-fx-font-weight: bold; "
	            + "-fx-text-fill: #2C6B2F;");

	    // Buttons
	    Button startButton = createFlatButton("Start Game", "#388E3C");
	    Button historyButton = createFlatButton("History", "#1976D2");
	    Button manageButton = createFlatButton("Manage Questions", "#FFC107");
	    Button exitButton = createFlatButton("Exit", "#FF5722");

	    // Add components
	    contents.getChildren().addAll(welcomeLabel, startButton, historyButton, manageButton, exitButton);

	    // Create scene
	    Scene scene = new Scene(contents, 600, 500);
	    Stage primaryStage = new Stage();
	    primaryStage.setTitle("Game Start");
	    primaryStage.setScene(scene);
	    primaryStage.show();

	    // Button actions
//	    CommandController command = new CommandController();
	    
	    startButton.setOnAction(event ->{
	    	System.out.println("Starting the Model...");
    		cmd.runCommand("/start");
    		primaryStage.close();
	    });
	    historyButton.setOnAction(event ->{
	    	System.out.println("Viewing history...");
	    	cmd.runCommand("/game_history");
	    });
	    manageButton.setOnAction(event ->{
	    	System.out.println("Managing questions...");
	    	cmd.runCommand("/qu_manage");
	    });
	    exitButton.setOnAction(event -> {
	        System.out.println("Exiting the Model...");
	        primaryStage.close();
//	        cmd.runCommand("/quit");
	        stage.close();
	        
	    });
	    
	    primaryStage.setOnCloseRequest(event ->{
	    	primaryStage.close();
	    	stage.close();
	    });
	    
	}

	// Method to create clean, flat buttons
	private Button createFlatButton(String text, String bgColor) {
	    Button button = new Button(text);
	    button.setStyle("-fx-background-color: " + bgColor + "; "
	            + "-fx-text-fill: white; "
	            + "-fx-font-size: 20px; "
	            + "-fx-font-weight: bold; "
	            + "-fx-border-radius: 20px; "
	            + "-fx-background-radius: 20px; "
	            + "-fx-padding: 15px 30px; "
	            + "-fx-cursor: hand;");
	    button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + lightenColor(bgColor) + "; "
	            + "-fx-text-fill: white; "
	            + "-fx-font-size: 20px; "
	            + "-fx-font-weight: bold; "
	            + "-fx-border-radius: 20px; "
	            + "-fx-background-radius: 20px; "
	            + "-fx-padding: 15px 30px; "
	            + "-fx-cursor: hand;"));
	    button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + bgColor + "; "
	            + "-fx-text-fill: white; "
	            + "-fx-font-size: 20px; "
	            + "-fx-font-weight: bold; "
	            + "-fx-border-radius: 20px; "
	            + "-fx-background-radius: 20px; "
	            + "-fx-padding: 15px 30px; "
	            + "-fx-cursor: hand;"));
	    return button;
	}

	// Helper to lighten colors on hover
	private String lightenColor(String color) {
	    switch (color) {
	        case "#388E3C": return "#45A049";
	        case "#1976D2": return "#1565C0";
	        case "#FFC107": return "#FFD54F";
	        case "#FF5722": return "#FF7043";
	        default: return color;
	    }
	}
	
	
	
	
}
