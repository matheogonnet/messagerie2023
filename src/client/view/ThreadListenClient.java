package client.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

import server.clientConnectionManagementModule.ChatServer;

public class ThreadListenClient extends Thread {

    private final BufferedReader reader;

    public boolean running = true;

    private ChatServer server;
    private String userName;

    public ThreadListenClient(BufferedReader reader, ChatServer server, String userName) {
        this.reader = reader;
        this.server = server;
        this.userName = userName;
    }

    // Boucle pour lire les messages envoyés par le serveur
    public void run() {
        String message = "";

        while (running){
            try {
                // Lire le message envoyé par le serveur
                message = reader.readLine();

                // Vérifier si le client s'est déconnecté ou non
                if (message == null || message.substring(message.indexOf(":") + 2).equals("/disconnect")){

                    // Afficher le message de déconnexion du client
                    print(userName + " disconnected");
                    running = false;
                } else {
                    // Afficher le message envoyé par le serveur dans le main client
                    print(message);
                }
            } catch(SocketException e){
                if(e.getMessage().equals("Socket closed")){
                    System.out.println("Socket closed");
                    running = false;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // Afficher le message envoyé par le serveur
    private void print(String text){
        if (server != null){
            server.sendToAllClients(text, userName);
        } else {
            System.out.println(text);
        }
    }
}