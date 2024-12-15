package client.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServices extends Remote {
    void startGame(String username, int gameNumber) throws RemoteException;
    String guessNumber(String username, int guess) throws RemoteException;
    String wordScramble(String username, String word) throws  RemoteException;
    String getShuffleWord(String username) throws  RemoteException;
    int verifyAnswerQuiz(String username, int index, int answer) throws RemoteException;
    String getWordToGuess(String username)  throws  RemoteException;
    String wordGuess(String username, String guess) throws  RemoteException;
    void endGame(String username) throws RemoteException;

}
