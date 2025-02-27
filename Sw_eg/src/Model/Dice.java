package Model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.hamcrest.core.IsNull;

import constants.DieInstance;
import interfaces.ColorParser;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * This class represents the dice object in Backgammon Model.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class Dice extends ImageView implements ColorParser {
	private final int MAX_DICE_SIZE = 6;
	private Image[] dices;
	private int diceRollResult;
	private ColorAdjust colorAdjust;
	private Color color;
	private Integer[] path;

	/**
	 * Constructors - Initialize the dices array with the possible dice images (i.e.
	 * 1-6).
	 */
	public Dice(Dice otherDice) {
		this(otherDice.getColor());
		this.diceRollResult = otherDice.getDiceResult();
		// path = new Integer[MAX_DICE_SIZE];
	}

	public Dice(int diceRollResult) {
		this(Color.RED);
		this.diceRollResult = diceRollResult;
//path = new Integer[MAX_DICE_SIZE];
	}

	public Dice(Color color) {
		super();
		this.color = color;
		dices = new Image[MAX_DICE_SIZE];
		path = new Integer[MAX_DICE_SIZE];
		initImages();

		colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.5);
	}

	/**
	 * Initializes the dice images: - by getting the images from file, - adding them
	 * to the dices array.
	 * 
	 * @param color
	 */
//	colorString.equals(Color.BLACK.toString()

	private void initImages() {

		String colorString = parseColor(color);
		System.out.println("after mvc");
		System.out.println("shhade committt3");
		System.out.println("COMMIT 4");
		System.out.println("COMMIT 5");
		System.err.println("ayman");

		System.err.println("abrahem");
		System.out.println("abrahim pull");
		if (RandomPip.mood.equals("hard") && !colorString.equals("black")) {
			Random rand = new Random();

			for (int i = 0; i < dices.length; i++) {
				try {
					int res = rand.nextInt(MAX_DICE_SIZE+4) - 3;
					if (res == 0)
						res = -2;
					path[i] = res;

					InputStream input = getClass().getResourceAsStream("img/dices/" + "red_minus" + "/" + res + ".png");
					dices[i] = new Image(input);
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (RandomPip.mood.equals("medium") || RandomPip.mood.equals("easy") || colorString.equals("black")) {

			for (int i = 0; i < dices.length; i++) {
				try {
					path[i] = i + 1;
					InputStream input = getClass()
							.getResourceAsStream("img/dices/" + colorString + "/" + (i + 1) + ".png");
					dices[i] = new Image(input);
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (RandomPip.mood.equals("")) {

			for (int i = 0; i < dices.length; i++) {
				try {
					path[i] = i + 1;
					InputStream input = getClass()
							.getResourceAsStream("img/dices/" + colorString + "/" + (i + 1) + ".png");
					dices[i] = new Image(input);
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Get the roll dice result (i.e. number from 1 to 6).
	 * 
	 * @return the roll dice result.
	 */
	public int roll() {
		Random rand = new Random();
		int res = rand.nextInt(MAX_DICE_SIZE) + 1;
		return res;
	}

	/**
	 * Set the image of dice based on result. i.e. If result is 1, show image with
	 * dice at 1.
	 * 
	 * @param result of dice roll.
	 */
	public Dice draw(int result) {
//		diceRollResult = result; 
		String colorString = parseColor(color);
		initImages();
//		int c = 0;
		if(RandomPip.Double == 1 && !colorString.equals("black")) {
			System.out.println(RandomPip.Double);
			for (int i = 0; i < dices.length; i++) {
				if(path[i] == result) { 	
					result = i + 1;
				}
				
			}
		}
		
		
		diceRollResult = path[result - 1];
		setImage(dices[result - 1]);
		setEffect(null);
		rotate();

		return this;
	}

	/**
	 * Rotate the dice image.
	 */
	private void rotate() {
		// rotation range of 15 to -15.
		Random rand = new Random();
		int rotation = rand.nextInt(30) - 15 + 1;
		setRotate(rotation);
	}

	public void setUsed() {
		// darken image.
		setEffect(colorAdjust);
	}

	public void setNotUsed() {
		setEffect(null);
	}

	public int getDiceResult() {
		return diceRollResult;
	}

	public Color getColor() {
		return color;
	}

	public boolean equalsValueOf(Dice otherDice) {
		return diceRollResult == otherDice.getDiceResult();
	}

//	public String[] getPath() {
//		return path;
//	}
}