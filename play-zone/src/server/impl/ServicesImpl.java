package server.impl;

import handler.ClientHandler;
import server.service.IServices;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesImpl extends UnicastRemoteObject implements IServices {
    private ConcurrentHashMap<String, Thread> activeThreads; // Track active threads
    private ConcurrentHashMap<String, ClientHandler> clientHandlers; // Client-specific handlers

    public ServicesImpl() throws RemoteException {
        super();
        activeThreads = new ConcurrentHashMap<>();
        clientHandlers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void startGame(String username, int gameNumber) throws RemoteException {
        if (activeThreads.containsKey(username)) {
            throw new RemoteException("A game is already active for user: " + username);
        }
        ClientHandler handler = new ClientHandler(username,gameNumber);
        Thread clientThread = new Thread(handler);
        activeThreads.put(username, clientThread);
        clientHandlers.put(username, handler);
        clientThread.start();
        System.out.println("Game started for user: " + username);
    }

    @Override
    public synchronized String guessNumber(String username, int guess) throws RemoteException {
        ClientHandler handler = clientHandlers.get(username);
        if (handler == null) {
            return "No active game found for user: " + username;
        }
        return handler.processGuess(guess);
    }

    @Override
    public String wordScramble(String username, String word) throws RemoteException {
        ClientHandler handler = clientHandlers.get(username);
        if (handler == null) {
            return "No active game found for user: " + username;
        }
        return handler.processScramble(word);
    }

    @Override
    public String getShuffleWord(String username) throws RemoteException {
        ClientHandler handler = clientHandlers.get(username);
        if (handler == null) {
            return "No active game found for user: " + username;
        }
        return handler.getShuffleWord();
    }

    @Override
    public int verifyAnswerQuiz(String username, int index, int answer) throws RemoteException{
        ClientHandler handler = clientHandlers.get(username);
        if (handler == null) {
            return 0;
        }
        return handler.verifyAnswerQuiz(username,index,answer);
    }

    @Override
    public String getWordToGuess(String username) throws RemoteException {
        ClientHandler handler = clientHandlers.get(username);
        if (handler == null) {
            return "No active game found for user: " + username;
        }
        return handler.getWordToGuess();
    }

    @Override
    public String wordGuess(String username, String guess) throws RemoteException {
        ClientHandler handler = clientHandlers.get(username);
        if (handler == null) {
            return "No active game found for user: " + username;
        }
        return handler.processWordGuess(guess);
    }


    @Override
    public synchronized void endGame(String username) throws RemoteException {
        Thread clientThread = activeThreads.remove(username);
        ClientHandler handler = clientHandlers.remove(username);

        if (clientThread != null && handler != null) {
            handler.stopGame(); // Stop the handler logic
            clientThread.interrupt(); // Interrupt the thread
            System.out.println("Game ended for user: " + username);
        } else {
            System.out.println("No active game found for user: " + username);
        }
    }
}

