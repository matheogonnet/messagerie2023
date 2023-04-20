package server.clientConnectionManagementModule;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ConnectionHandler extends Thread {

    public boolean isActive = true; // Indique si la connexion est active

    private ServerSocket serverSocket; // Socket serveur
    private ChatServer server; // Instance du serveur

    /**
     * Constructeur de la classe ConnectionHandler
     *
     * @param sock   : le socket serveur pour la gestion des connexions entrantes
     * @param server : l'instance du serveur pour la gestion des clients
     */
    public ConnectionHandler(ServerSocket sock, ChatServer server) {
        this.serverSocket = sock;
        this.server = server;
    }

    /**
     * Cette méthode est exécutée lorsqu'on démarre l'instance de la classe en tant que Thread.
     * Elle gère les connexions entrantes, en créant un nouvel objet ServerClient pour chaque client qui se connecte
     * et en l'ajoutant à la liste des clients du serveur.
     */
    public void run() {
        while (isActive) {
            try {
                Socket socket = serverSocket.accept(); // Attendre une nouvelle connexion
                server.addServerClient(new ServerClient(socket, server)); // Créer un nouvel objet ServerClient pour le client connecté
            } catch (SocketException e) {
                if (e.getMessage().equals("Socket closed")) {
                    isActive = false; // Si la connexion est fermée, arrêter la boucle et terminer l'exécution de la tâche
                    break;
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la gestion de la connexion entrante");
                e.printStackTrace();
            }
        }
    }
}