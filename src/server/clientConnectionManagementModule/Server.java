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
 * Cette classe gère la connexion avec les clients et envoie des messages à tous les clients connectés.
 */

public class Server extends Thread {
    public boolean isActive = true; // Indique si la connexion est active
    private ServerSocket serverSocket;
    private String yourMessage;
    private static ArrayList<ServerClient> clients = new ArrayList<>(); //liste de tous les Servers client
    private  UserController userController;
    private DaoMessage daoMessage;
    private DaoUser daoUser;
    private static int nextClientId = 1;



    public ArrayList<ServerClient> getClients() {
        return clients;
    }

    /**
     * Constructeur de la classe Server.
     * @param userController Le contrôleur d'utilisateur pour gérer les utilisateurs.
     * @param daoMessage Le DAO pour gérer les messages stockés dans la base de données.
     * @param daoUser Le DAO pour gérer les utilisateurs stockés dans la base de données.
     */
    public Server(int port, UserController userController, DaoMessage daoMessage, DaoUser daoUser) {
        this.daoMessage = daoMessage;
        this.daoUser = daoUser;
        this.userController =userController;


        try { // Écoute les connexions entrantes

            System.out.println("Hosting chat server on port " + port);
            serverSocket = new ServerSocket(port);
            start();
            //démarre un nouveau thread pour gérer la communication avec le nouveau client qui vient de se connecter.
            // En appelant la méthode "start()" du thread, le code exécuté par le thread est lancé et s'exécute en
            // arrière-plan, tandis que le reste du programme continue à s'exécuter. Cela permet au serveur d'accepter
            // de nouveaux clients sans bloquer le thread principal qui écoute les connexions entrantes.

            Scanner inputScanner = new Scanner(System.in);
            while(isActive){
                yourMessage = inputScanner.nextLine();
                System.out.println("Server: " + yourMessage);

                // Si l'utilisateur entre la chaîne "quit()", le serveur se ferme
                if (yourMessage.equals("/disconnected")) {
                    isActive = false;
                    break;
                }
            }
            // Fermeture de la connexion et des threads
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
     * La méthode run() est exécutée lorsqu'on démarre l'instance de la classe en tant que Thread.
     * Elle gère les connexions entrantes, en créant un nouvel objet ServerClient pour chaque client qui se connecte
     * et en l'ajoutant à la liste des clients du serveur.
     */

    public void run() {
        while (isActive) {
            System.out.println("Waiting for connection...");
            try {
                Socket socket = serverSocket.accept(); // Attendre une nouvelle connexion
                ServerClient serverClient = new ServerClient(socket, this, userController, daoMessage, daoUser, nextClientId++);
                clients.add(serverClient); // Ajouter le client à la liste des clients connectés
                System.out.println("Nouvelle connexion enregistrée : client n°"+serverClient.getId());
            } catch (SocketException e) {
                if (e.getMessage().equals("Socket closed")) {
                    isActive = false; // Si la connexion est fermée, arrêter la boucle et terminer l'exécution de la tâche
                    break;
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la gestion de la connexion entrante");
                e.printStackTrace();
            }
        }
    }

    /**
     * Envoie un message à tous les clients connectés.
     * @param message Le message à envoyer.
     */
    public void sendToAllClients(String message, int id) throws IOException {
        for (ServerClient client :clients){
            if(client.getId()!=id){
                client.send(message);
            }

        }
    }

    // Ajoute un nouveau client à la liste des clients connectés
    public synchronized void addServerClient(ServerClient serverClient){
        clients.add(serverClient); // Ajouter le client à la liste des clients connectés
    }
}
