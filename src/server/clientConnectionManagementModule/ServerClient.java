package server.clientConnectionManagementModule;

import client.view.ThreadListenClient;

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerClient extends Thread {

    public BufferedReader reader; // Flux de lecture pour lire les données envoyées par le client sur le socket
    public PrintWriter writer; // Flux d'écriture pour écrire des données dans le flux de sortie du socket client
    public String userName; // Nom d'utilisateur du client
    public ThreadListenClient threadListenClient; // Thread pour écouter les messages du client

    public ServerClient(Socket clientSocket, ChatServer server) throws IOException {

        try{
            // Initialisation des flux de lecture et d'écriture
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            userName = reader.readLine(); // Récupération du nom d'utilisateur du client

            server.sendToAllClients(userName + " connected", "global"); // Envoi d'un message de connexion globale à tous les clients
            threadListenClient = new ThreadListenClient(reader, server, userName); // Initialisation du thread pour écouter les messages du client
            threadListenClient.start(); // Démarrage du thread pour écouter les messages du client

        } catch(Exception e){
            System.out.println("Erreur lors de l'acceptation de la connexion ");
            e.printStackTrace();
        }
    }

    public void send(String message){
        writer.println(message); // Envoi d'un message au client via le flux d'écriture

        //envoyer le message ici à la base de donnée
    }

    public void stopRunning(){
        threadListenClient.running = false; // Arrêt du thread pour écouter les messages du client
    }
}
