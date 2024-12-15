package client;

import server.service.IServices;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void menuDisplay(){
        System.out.println("\n==================================================");
        System.out.println("         Please choose a game to play: 🎲           ");
        System.out.println("====================================================");
        System.out.println("  1️⃣ Number Guessing Game");
        System.out.println("  2️⃣ Trivia Quiz");
        System.out.println("  3️⃣ Word Scramble");
        System.out.println("  4️⃣ Guess The Word");
        System.out.println("  5️⃣ Quit");
        System.out.print("> ");
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playing = true;

        try {
            // Connect to RMI registry and lookup server
            Registry registry = LocateRegistry.getRegistry("localhost", 2301);
            IServices games = (IServices) registry.lookup("Games");


            System.out.println("=========================================");
            System.out.println("      WELCOME TO PLAYZONE! 🎮           ");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("Please enter your username to get started:");
            System.out.print("> ");
            String username = scanner.nextLine();

            if (username.isEmpty()) {
                System.out.println("\n⚠️ Username cannot be empty! Please restart the game.");
                return;
            }

            System.out.println("\nHello, " + username + "! Are you ready to have some fun?");
            System.out.println();
            menuDisplay();

            int gameChoice = scanner.nextInt();
            while (gameChoice>0){
                switch (gameChoice) {
                    case 1:
                        System.out.println("\n🎯 You chose: Number Guessing Game");
                        games.startGame(username,0);
                        System.out.println("Game started! Guess a number between 1 and 100.");
                        while (playing) {
                            System.out.print("Enter your guess: ");
                            int guess = scanner.nextInt();
                            String response = games.guessNumber(username, guess);
                            System.out.println(response);
                            if (response.contains("Congratulations")) {
                                playing = false;
                            }
                        }
                        games.endGame(username);
                        System.out.println("Game ended. Thanks for playing!");
                        break;
                    case 2:
                        System.out.println("\n🧠 You chose: Trivia Quiz");
                        games.startGame(username,1);
                        System.out.println("Game started!");
                        List<Question> questions = Utils.readQuestionsFromFile("C:\\Users\\Mehrez\\projects\\play-zone\\play-zone\\src\\client\\quiz.csv");
                        int score = 0;
                        int index= 0;
                        while (playing) {
                            for (Question q : questions) {
                                System.out.println(" - "+q.getQuestion());
                                for (int i = 0; i < q.getAnswers().length; i++) {
                                    System.out.println((i + 1) + ". " + q.getAnswers()[i]);
                                }
                                System.out.print("Enter your answer (1-4):");
                                int answer = scanner.nextInt();
                                score += games.verifyAnswerQuiz(username,index,answer-1);
                                index++;
                            }
                            playing = false;
                        }
                        games.endGame(username);
                        System.out.println("Game ended. Thanks for playing! Your score = "+score);
                        break;
                    case 3:
                        System.out.println("\n🔠 You chose: Word Scramble");
                        games.startGame(username,2);
                        System.out.println("Game started! Guess the word : "+ games.getShuffleWord(username));
                        while (playing) {
                            System.out.println();
                            System.out.print("Enter your guess: ");
                            String guess = scanner.next();
                            String response = games.wordScramble(username, guess);
                            System.out.println(response);
                            if (response.contains("Congratulations")) {
                                playing = false;
                            }
                        }
                        games.endGame(username);
                        System.out.println("Game ended. Thanks for playing!");
                        break;
                    case 4:
                        System.out.println("\n🔠 You chose: Guess The Word");
                        games.startGame(username,3);
                        String wordToGuess = games.getWordToGuess(username);
                        System.out.println("Game started! Guess the word : "+ wordToGuess);
                        while (playing) {
                            System.out.println();
                            System.out.print("Enter your guess: ");
                            String guess = scanner.next();
                            String response = games.wordGuess(username, guess);
                            System.out.println(response);
                            if (response.contains("Congratulations")) {
                                playing = false;
                            }
                            else{
                                if((wordToGuess.chars()
                                        .filter(ch -> ch == '-')
                                        .count())==1){
                                    System.out.println("The word was : "+ games.getWordToGuess(username));
                                    break;
                                }
                                wordToGuess = games.getWordToGuess(username);
                                System.out.println("The word : "+ wordToGuess);
                            }
                        }
                        games.endGame(username);
                        System.out.println("Game ended. Thanks for playing!");
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("\n⚠️ Invalid choice. Please restart the game.");
                }
                menuDisplay();
                System.out.print("> ");
                gameChoice = scanner.nextInt();
                playing = true;
            }

            System.out.println("\n=========================================");
            System.out.println("         THANK YOU FOR PLAYING!          ");
            System.out.println("=========================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
