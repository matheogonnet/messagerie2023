package client.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ChatClient {

    private String userName; // username du client

    private Scanner scanner;

    private String serverHost; // adresse IP ou nom d'hôte du serveur
    private int serverPort; // port sur lequel le serveur écoute les connexions entrantes
    private Socket socket; // objet Socket pour communiquer avec le serveur

    private PrintWriter writer; // objet PrintWriter pour écrire des données à envoyer au serveur

    private BufferedReader reader; // flux d'entrée (réception des messages du serveur)

    private ThreadListenClient threadListenClient;

    public ChatClient(String serverHost, int port) throws IOException { // initialiser l'identifiant du client
        this.serverHost = serverHost;
        this.serverPort = port;
        // initialisation à une valeur par défaut

        scanner = new Scanner(System.in);

        while (true){
            System.out.print("Enter username: ");
            userName = scanner.nextLine();
            if (userName.equals("server") || userName.equals("global")){
                System.out.println("Error: reserved usernames cannot be used");
            } else {
                break;
            }
        }

        try {
            // Créer une socket pour communiquer avec le serveur
            socket = new Socket(serverHost, port);
            // Initialiser les flux de sortie et d'entrée pour la communication avec le serveur
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(userName);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Vous êtes connecté avec le nom d'utilisateur " + userName);

            // Démarrer un thread pour écouter les messages entrants du serveur
            threadListenClient = new ThreadListenClient(reader, null, userName);
            threadListenClient.start();
        } catch (IOException e) {

            System.err.println("IOException occurred.");
            return;
        }

        run();
    }
    /**
     * Boucle principale qui gère l'envoi de messages au serveur et la réception de messages du serveur.
     * Tant que la boucle tourne, l'utilisateur est invité à entrer un message à envoyer au serveur.
     * Si l'utilisateur entre "/disconnect" ou si le thread qui écoute les messages entrants du serveur se termine,
     * la boucle se termine et le programme se déconnecte du serveur.
     * Enfin, le thread d'écoute des messages du serveur est arrêté et la connexion avec le serveur est fermée.
     */
    private void run() {
        System.out.println("Begin chat with " + serverHost + " on port " + serverPort);

        while (true) {
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();

            // Envoie le message au serveur
            writer.println(userName + ": " + message);

            // Vérifie si l'utilisateur souhaite se déconnecter
            if (message.equals("/disconnect") || !threadListenClient.running) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println(userName + ": " + message);
            }
        }

        // Arrête le thread d'écoute des messages du serveur et ferme la connexion avec le serveur
        try {
            threadListenClient.running = false;
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket.");
        }

        System.out.println("Chat terminated");
    }
}
