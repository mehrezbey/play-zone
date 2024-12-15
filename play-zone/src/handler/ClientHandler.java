package handler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable {
    private String username;
    private int gameNumber;
    private ConcurrentHashMap<String, String> gameDetails;
    private List<Question> questions;

    private boolean running;


    public ClientHandler(String username,int gameNumber) {
        this.username = username;
        this.gameNumber = gameNumber;
        this.running = true;
        this.gameDetails = new ConcurrentHashMap<>();
        if(gameNumber == 0){
            gameDetails.put("targetNumber", String.valueOf(new Random().nextInt(100) + 1));// Random number between 1 and 100
        } else if (gameNumber == 1) {
            questions = Utils.readQuestionsFromFile("C:\\Users\\Mehrez\\projects\\play-zone\\play-zone\\src\\handler\\quiz.csv");

        } else if (gameNumber == 2) {
            // Path to the file
            String filePath = "C:\\Users\\Mehrez\\projects\\play-zone\\play-zone\\src\\handler\\words.txt";
            try {
                // Read all lines from the file into a List
                List<String> words = Files.readAllLines(Paths.get(filePath));
                // Check if the list is not empty
                if (words.isEmpty()) {
                    System.out.println("The file is empty. Please add words to the file.");
                    return;
                }
                // Generate a random index and get the word
                Random random = new Random();
                String randomWord = words.get(random.nextInt(words.size()));
                gameDetails.put("shuffledWord",shuffleString(randomWord));
                gameDetails.put("targetWord",randomWord);

            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }
        } else if (gameNumber==3) {
            String filePath = "C:\\Users\\Mehrez\\projects\\play-zone\\play-zone\\src\\handler\\words.txt";
            try {
                // Read all lines from the file into a List
                List<String> words = Files.readAllLines(Paths.get(filePath));
                // Check if the list is not empty
                if (words.isEmpty()) {
                    System.out.println("The file is empty. Please add words to the file.");
                    return;
                }
                // Generate a random index and get the word
                Random random = new Random();
                String randomWord = words.get(random.nextInt(words.size()));
                gameDetails.put("wordToGuess",randomWord);
                gameDetails.put("WordToDisplay",new StringBuilder("-".repeat(gameDetails.get("wordToGuess").length())).toString());
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // Simulate server-side waiting or processing
            } catch (InterruptedException e) {
                System.out.println("Game thread interrupted for user: " + username);
                return;
            }
        }
    }

    public String processGuess(int guess) {
        if (guess < Integer.valueOf(gameDetails.get("targetNumber"))) {
            return "Too low!";
        } else if (guess > Integer.valueOf(gameDetails.get("targetNumber"))) {
            return "Too high!";
        } else {
            running = false; // Stop the game if guessed correctly
            return "Congratulations! You guessed the number!";
        }
    }

    public String processScramble(String word) {
        if(word.equalsIgnoreCase(gameDetails.get("targetWord"))){
            running = true;
            return "Congratulations! You guessed the word!";
        }
        else{
            return "Try again!";
        }
    }
    public String getShuffleWord() {
        return gameDetails.get("shuffledWord");
    }


    public void stopGame() {
        running = false;
    }

    private static String shuffleString(String input) {

        // Convert String to a list of Characters
        input = input.toLowerCase();
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }

        // Shuffle the list
        Collections.shuffle(characters);

        // Convert the list back to String
        StringBuilder shuffledString = new StringBuilder();

        for (char c : characters) {
            shuffledString.append(c);
        }

        return shuffledString.toString();
    }


    public int verifyAnswerQuiz(String username,int index, int answer) {
        return (Integer.valueOf(questions.get(index).getCorrectAnswer().trim()) == answer)?1:0;
    }




    public String getWordToGuess() {
        String wordToGuess = gameDetails.get("wordToGuess");
        String wordToDisplay = gameDetails.get("WordToDisplay");
        int pos = new Random().nextInt(wordToGuess.length());
        while (wordToDisplay.charAt(pos)!='-'){
            pos = new Random().nextInt(wordToGuess.length());
        }
        gameDetails.put("WordToDisplay",(new StringBuilder(wordToDisplay).replace(pos,pos+1,""+wordToGuess.charAt(pos))).toString());
        return gameDetails.get("WordToDisplay");
    }

    public String processWordGuess(String guess) {
        if(guess.equalsIgnoreCase(gameDetails.get("wordToGuess"))){
            running = true;
            return "Congratulations! You guessed the word!";
        }
        else{
            return "Try again!";
        }
    }
}
