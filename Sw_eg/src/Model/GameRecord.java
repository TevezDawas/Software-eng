package Model;

public class GameRecord {
    private String datePlayed;    // Using String for simplicity; adjust if needed (e.g., LocalDateTime)
    private String player1Name;
    private String player2Name;
    private String winnerName;
    private String difficulty;

    public GameRecord(String datePlayed, String player1Name, String player2Name,
                      String winnerName, String difficulty) {
        this.datePlayed = datePlayed;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerName = winnerName;
        this.difficulty = difficulty;
    }

    public String getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(String datePlayed) {
        this.datePlayed = datePlayed;
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
