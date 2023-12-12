package server.clientConnectionManagementModule;

import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class manages client connections and sends messages to all connected clients.
 */
public class Server extends Thread {
    public boolean isActive = true; // Indicates if the connection is active
    private ServerSocket serverSocket;
    private String yourMessage;
    private static ArrayList<ServerClient> clients = new ArrayList<>(); // List of all Server clients
    private DaoMessage daoMessage;
    private DaoUser daoUser;
    private static int nextClientId = 1;

    public ArrayList<ServerClient> getClients() {
        return clients;
    }

    /**
     * Constructor for the Server class.
     *
     * @param port       The port on which the server will listen for incoming connections.
     * @param daoMessage The DAO to manage messages stored in the database.
     * @param daoUser    The DAO to manage users stored in the database.
     */
    public Server(int port, DaoMessage daoMessage, DaoUser daoUser) {
        this.daoMessage = daoMessage;
        this.daoUser = daoUser;

        try {
            System.out.println("Hosting chat server on port " + port);
            serverSocket = new ServerSocket(port);
            start();

            Scanner inputScanner = new Scanner(System.in);
            while (isActive) {
                yourMessage = inputScanner.nextLine();
                System.out.println("Server: " + yourMessage);

                // If the user enters "/disconnected," the server shuts down
                if (yourMessage.equals("/disconnected")) {
                    isActive = false;
                    break;
                }
            }

            // Closing the connections and threads
            for (ServerClient client : clients) {
                client.stopRunning();
            }
            System.out.println("Closing server");
            serverSocket.close();

        } catch (Exception e) {
            System.out.println("Error ");
            e.printStackTrace();
        }
    }

    /**
     * The run() method is executed when starting an instance of the class as a Thread.
     * It handles incoming connections, creating a new ServerClient object for each connecting client
     * and adding it to the server's list of clients.
     */
    public void run() {
        while (isActive) {
            System.out.println("Waiting for connection...");
            try {
                Socket socket = serverSocket.accept(); // Wait for a new connection
                ServerClient serverClient = new ServerClient(socket, this, daoMessage, daoUser, nextClientId++);
                clients.add(serverClient); // Add the client to the list of connected clients
                System.out.println("New connection registered: Client #" + serverClient.getId());
            } catch (SocketException e) {
                if (e.getMessage().equals("Socket closed")) {
                    isActive = false; // If the connection is closed, stop the loop and terminate the thread
                    break;
                }
            } catch (IOException e) {
                System.err.println("Error handling incoming connection");
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message to all connected clients except the one with the given ID.
     *
     * @param message The message to send.
     * @param id      The ID of the client to exclude from the message broadcast.
     */
    public void sendToAllClients(String message, int id) throws IOException {
        for (ServerClient client : clients) {
            if (client.getId() != id) {
                client.send(message);
            }
        }
    }
}
