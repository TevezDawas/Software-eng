package Model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomPip {
//	public static int firstRandomPipQ = -1;
//	public static int secondRandomPipQ = -1;
//	public static int thRandomPipQ = -1;
//	public static int RandomPipS = -1;
//	public static int common = -1;
	
	public static String firstRandomPipQ = "-1";
	public static String secondRandomPipQ = "-1";
	public static String thRandomPipQ = "-1";
	public static String RandomPipS = "-1";
	public static String common = "-1";
	public static int Double = 0;
	public static String mood = "";
	public static int qDiff = -1;
	public static Boolean isFirst = true;
	 
	
	public static void runAll() {
		
		generateQuestions();
		generateSurprise();
		theCommon();
		
	}
	
	public static void generateQuestions() {
		Random random = new Random();
		Set<Integer> set = new HashSet<Integer>();
		while(set.size()<3) {
			int rand = random.nextInt(24);
			set.add(rand);
		}
		firstRandomPipQ = set.toArray()[0].toString();
		secondRandomPipQ = set.toArray()[1].toString();
		thRandomPipQ = set.toArray()[2].toString();
		
		
	}
	 
	 public static void generateSurprise() {
			Random random = new Random();
			RandomPipS = String.valueOf(random.nextInt(24));
		}
	 public static String theCommon() {
		 if(RandomPipS.equals(firstRandomPipQ) || RandomPipS.equals(secondRandomPipQ)||
				 RandomPipS.equals(thRandomPipQ)) {
			 common =RandomPipS;
			 return RandomPipS;
			 }
		return "-1";
		
	}
	

}