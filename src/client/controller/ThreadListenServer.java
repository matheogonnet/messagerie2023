package client.controller;

import client.controller.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

/**
 * Classe ThreadListenServer qui hérite de la classe Thread.
 * Elle permet de créer un thread pour écouter les messages provenant du serveur.
 */
public class ThreadListenServer extends Thread {

    private final BufferedReader reader;
    private Client client;
    public boolean running = true;

    /**
     * Constructeur de la classe ThreadListenServer.
     * @param reader BufferedReader qui permet de lire les messages envoyés par le serveur.
     * @param client Client à qui est associé ce ThreadListenServer.
     */
    public ThreadListenServer(BufferedReader reader, Client client) {
        this.reader = reader;
        this.client = client;


    }

    /**
     *Méthode run qui permet de lancer le thread pour écouter les messages provenant du serveur.
     * Le thread s'arrête lorsque le client se déconnecte ou que le serveur envoie un message de déconnexion.
     */
    public void run() {
        while (running){
            try {
                //message provenant du server
                String message = reader.readLine();
                if (message!=null){
                    client.handleMessage(message); //fonction qui traite le message en fonction de la requete
                }
                // Vérifier si le client s'est déconnecté ou non
                if (message == null || message.substring(message.indexOf(":") + 2).equals("/disconnect")){
                    // Afficher le message de déconnexion du client
                    running = false;

                }
                //forcer a quitter
                if (message.equals("/disconnected")){
                    break;
                }

            } catch(SocketException e){
                //envoie un message de déconnection au client pour le déco du socket
                if(e.getMessage().equals("Socket closed")){
                    System.out.println("Socket closed");
                    running = false;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}