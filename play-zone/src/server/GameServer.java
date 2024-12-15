package server;

import server.impl.ServicesImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class GameServer {
    public static void main(String[] args) {
        try {
            ServicesImpl server = new ServicesImpl();
            Registry registry = LocateRegistry.createRegistry(2301); // Start RMI registry on port 1099
            registry.rebind("Games", server); // Bind server instance to name
            System.out.println("Game server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
