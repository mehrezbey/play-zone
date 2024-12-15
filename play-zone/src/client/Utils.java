package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Question> readQuestionsFromFile(String fileName) {
        List<Question> questions = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            for (String line : lines) {
                if (!line.startsWith("Question")) { // Skip header line
                    String[] parts = line.split(",");
                    String questionText = parts[0];
                    String[] answers = {parts[1], parts[2], parts[3], parts[4]};
                    String correctAnswer = parts[5];
                    questions.add(new Question(questionText, answers, correctAnswer));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}

class Question {
    private String question;
    private String[] answers;
    private String correctAnswer;

    public Question(String question, String[] answers, String correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }
}
