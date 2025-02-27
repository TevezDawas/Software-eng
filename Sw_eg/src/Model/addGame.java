package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class addGame {

    /**
     * Loads Model records from the specified JSON file and returns them as a List.
     */
//    public static List<GameRecord> loadGameRecordsFromFile(String filePath) throws Exception {
//        List<GameRecord> loadedRecords = new ArrayList<>();
//
//        // 1. Read file content
//        String content = new String(Files.readAllBytes(Paths.get(filePath)));
//        
//        // 2. Parse JSON
//        JSONObject jsonObject = new JSONObject(content);
//        JSONArray recordsArray = jsonObject.getJSONArray("gameRecords");
//
//        // 3. Convert JSON objects into GameRecord instances
//        for (int i = 0; i < recordsArray.length(); i++) {
//            JSONObject recordObj = recordsArray.getJSONObject(i);
//
//            String datePlayed = recordObj.getString("datePlayed");
//            String player1Name = recordObj.getString("player1Name");
//            String player2Name = recordObj.getString("player2Name");
//            String winnerName = recordObj.getString("winnerName");
//            String difficulty = recordObj.getString("difficulty");
//
//            GameRecord record = new GameRecord(
//                datePlayed, player1Name, player2Name, winnerName, difficulty
//            );
//            loadedRecords.add(record);
//        }
//
//        return loadedRecords;
//    }

    /**
     * Appends a new GameRecord to the existing JSON file.
     */
    public static void addGameRecordToFile(GameRecord newRecord) throws Exception {
        // 1. Read existing content
    	String filePath = "src/Model/gameRecords.json";
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject jsonObject = new JSONObject(content);
        JSONArray recordsArray = jsonObject.getJSONArray("gameRecords");

        // 2. Create a new JSON object from the GameRecord
        JSONObject newRecordObj = new JSONObject();
        newRecordObj.put("datePlayed", newRecord.getDatePlayed());
        newRecordObj.put("player1Name", newRecord.getPlayer1Name());
        newRecordObj.put("player2Name", newRecord.getPlayer2Name());
        newRecordObj.put("winnerName", newRecord.getWinnerName());
        newRecordObj.put("difficulty", newRecord.getDifficulty());

        // 3. Append it to the array
        recordsArray.put(newRecordObj);

        // 4. Write updated content back to file (with some indentation)
        Files.write(Paths.get(filePath), jsonObject.toString(4).getBytes());
    }
}
