package Model;

public class Question {
    private String question;
    private String[] answers;
    private int correctAnswerIndex;
    private String difficulty;

    public Question(String question, String[] answers, int correctAnswerIndex, String difficulty) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getAnswersFormatted() {
        return String.join(", ", answers);
    }

    public String getCorrectAnswerFormatted() {
        return answers[correctAnswerIndex];
    }
}


