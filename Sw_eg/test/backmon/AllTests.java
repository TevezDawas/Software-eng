package backmon;
import Model.RandomPip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Dimension;

import org.junit.Test;

import constants.GameConstants;
import Model.GameRecord;

import Model.Question;

import Controler.Settings;





public class AllTests {
	



	@Test

	public void randomTest() {

		// check if the random numbers is generated 

		RandomPip.runAll();

		assertNotEquals(RandomPip.firstRandomPipQ, "-1");

		assertNotEquals(RandomPip.secondRandomPipQ, "-1");

		assertNotEquals(RandomPip.thRandomPipQ, "-1");

		assertNotEquals(RandomPip.RandomPipS, "-1");

		

		/*fail("Not yet implemented");*/

	}

	@Test

	public void DifficultyTest() {

		Settings.setDifficulty("hard");

		String Dif = "hard";

		assertEquals(Dif, Settings.getDifficulty());

		/*fail("Not yet implemented");*/

	}

	@Test

	public void HalfBoardSizeTest() {

		Dimension d = new Dimension();

		d.setSize(381,589);

		assertEquals(d,GameConstants.getHalfBoardSize());

	}

	

	@Test

	public void QuestionTest() {

		Question q = new Question("are you here?", null, 0, null);

		String qu = "are you here?";

		assertEquals(q.getQuestion(), qu);

	}

	@Test

	public void gameRecordTest() {

		GameRecord h = new GameRecord(null, "yosef", null, null, null);

		assertEquals("yosef", h.getPlayer1Name());

	}

}
