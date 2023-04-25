package server.clientConnectionManagementModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

import server.clientConnectionManagementModule.Server;

public class ThreadListenClient extends Thread {

    private final BufferedReader reader;

    public boolean running = true;

    private Server server;
    private ServerClient client;
    public ThreadListenClient(BufferedReader reader, Server server, ServerClient client) {
        this.reader = reader;
        this.server = server;
        this.client = client;
    }

    //Thread qui lit les messages request provenants du client tant que celui ne se déconnnecte pas
    public void run() {
        while (running){
            try {
                String message = reader.readLine();
                if (message!=null){
                    client.handleMessage(message); //traiter le message en fonction de la requete
                }
                // Vérifier si le client s'est déconnecté ou non
                if (message == null || message.substring(message.indexOf(":") + 2).equals("/disconnect")){
                    // Afficher le message de déconnexion du client
                    running = false;

                }

                assert message != null;
                if (message.equals("/disconnected")){
                    break;
                }

            } catch(SocketException e){
                if(e.getMessage().equals("Socket closed")){
                    System.out.println("Socket closed");
                    running = false;
                }
            } catch (IOException | SQLException e){
                e.printStackTrace();
            }
        }
    }

}