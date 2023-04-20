package server.clientConnectionManagementModule;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatServer {

    private ServerSocket serverSocket;
    private ArrayList<ServerClient> clients; // liste de tous les clients connectés

    private String yourMessage; // message envoyé par le serveur

    public ChatServer(int port) {

        clients = new ArrayList<>();
        Scanner inputScanner = new Scanner(System.in);

        try { // Écoute les connexions entrantes

            System.out.println("Hosting chat server on port " + port);
            serverSocket = new ServerSocket(port);
            ConnectionHandler connectionHandler = new ConnectionHandler(serverSocket, this);
            connectionHandler.start();
            //démarre un nouveau thread pour gérer la communication avec le nouveau client qui vient de se connecter.
            // En appelant la méthode "start()" du thread, le code exécuté par le thread est lancé et s'exécute en
            // arrière-plan, tandis que le reste du programme continue à s'exécuter. Cela permet au serveur d'accepter
            // de nouveaux clients sans bloquer le thread principal qui écoute les connexions entrantes.

            while(true){

                yourMessage= inputScanner.nextLine();
                System.out.println("Waiting for connection...");
                System.out.println("Server: " + yourMessage);

                sendToAllClients("Server: " + yourMessage, "server");

                if (yourMessage.equals("quit()")){
                    break;
                }
            }

            // Fermeture de la connexion et des threads

            connectionHandler.isActive = false;
            for (int i = 0; i < clients.size(); i++){
                clients.get(i).stopRunning();
            }
            System.out.println("Closing server");
            serverSocket.close();

        }
        catch(Exception e){
            System.out.println("Error ");
            e.printStackTrace();
        }
    }

    // Ajoute un nouveau client à la liste des clients connectés

    public void addServerClient(ServerClient serverClient){
        clients.add(serverClient);
    }

    // Envoie un message à tous les clients, sauf celui qui l'a envoyé

    public void sendToAllClients(String message, String src) {

        for (ServerClient client : clients) {
            if (!client.userName.equals(src)) {

                client.send(message);

            }
        }

        // Si la source du message n'est pas le serveur, affiche le message dans la console du serveur

        if (!src.equals("server")){
            System.out.println(message);
        }
    }
}
