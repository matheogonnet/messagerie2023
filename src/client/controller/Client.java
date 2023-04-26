package client.controller;


import client.model.*;
import client.view.DisplayStepHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Classe qui va créer un client, lancer un thread d'écoute et gérer tous les messages venant du serveur
 */
public class Client {
    private Socket socket;
    private PrintWriter writer;
    private ThreadListenServer threadListenServer;
    private User user;
    private Conversation conversation = new Conversation("Salon");
    private ArrayList<User>userList = new ArrayList<>();


    /**
     * Constructeur d'un client
     *
     * @param host l'adresse du serveur
     * @param port le port de communication
     */
    public Client(String host, int port) {
        try {
            // Etablissement d'un socket pour la communication avec le serveur
            socket = new Socket(host, port);

            // Initialisation des flux de sortie et d'entrée pour la communication avec le serveur
            writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            threadListenServer = new ThreadListenServer(reader, this);

            Thread listenThread = new Thread(() -> {
                // Démarrage d'un thread pour écouter les messages entrants du serveur
                threadListenServer.start();
            });
            listenThread.start();

            // Lancement de la méthode view dans le thread principal
            DisplayStepHandler.view(this, conversation, userList);

        } catch (IOException e) {
            System.err.println("IOException occurred.");
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Envoie un message au serveur
     *
     * @param message le message à envoyer
     */
    public void send(String message) {
        // Si message de déconnexion
        if (message.equals("/disconnected") || !threadListenServer.running) {
            // Arrêt du thread d'écoute des messages du serveur et fermeture de la connexion avec le serveur
            try {
                System.out.println("Closing client");
                threadListenServer.running = false;
                close();
            } catch (IOException e) {
                System.err.println("Error closing socket.");
            }
            System.out.println("Chat terminated");
        }
        // Sinon envoi du message au serveur
        else {
            writer.println(message);
        }
    }

    /**
     * Fermeture de la connexion avec le serveur
     *
     * @throws IOException en cas d'erreur lors de la fermeture de la connexion
     */
    public void close() throws IOException {
        socket.close();
    }

    /*
     *Gestion des messages reçus du serveur.
     * @param messageServer Le message reçu sous forme de chaîne de caractères.
     */
    public void handleMessage(String messageServer){
        String[] parts = messageServer.split("::");
        String type = parts[0];
        String command = parts[1];
        System.out.println("---------------------------------------\n    NOUVEAU MESSAGE RECU DU SERVEUR    \n   type: "+type+"  commande: "+command+"\n---------------------------------------");

        if (type.equals("USER")) {
            switch (command) {
                case "login" -> {
                    String acces = parts[2];
                    if (acces.equals("ACCES GRANTED")) {
                        String pseudo = parts[3];
                        String firstName = parts[4];
                        String lastName = parts[5];
                        String status = parts[6];
                        String grade = parts[7];
                        String ban = parts[8];
                        DisplayStepHandler.acces = true;
                        this.user = create_user(pseudo, lastName, firstName, status, grade, ban);
                        for (User logUser : userList){
                            if (logUser.getPseudo().equals(pseudo)){
                                logUser.setStatus("Online");
                            }
                        }
                    }
                    else if (acces.equals("ACCES DENIED")) {
                        DisplayStepHandler.acces = false;
                        this.user = new User();

                    }
                }

                case "newLogin"->{
                    String pseudo = parts[2];
                    for (User user : userList){
                        if (user.getPseudo().equals(pseudo)){
                            user.setStatus("Online");
                        }
                    }
                }

                case "log_out"->{
                    String pseudo = parts[2];
                    for (User user : userList){
                        if (user.getPseudo().equals(pseudo)){
                            user.setStatus("Offline");
                        }
                    }

                }

                case "signUp" -> {
                    String pseudo = parts[2];
                    String firstName = parts[3];
                    String lastName = parts[4];
                    String status = parts[5];
                    String grade = parts[6];
                    String ban = parts[7];
                    User newUser = create_user(pseudo, lastName, firstName, status, grade, ban);
                    userList.add(newUser);

                }
                case "getUserList" -> {
                    String[] messages = parts[2].split("/"); // Séparer tous les users
                    for (String message : messages) {
                        String[] messageParts = message.split("_");
                        String pseudo = messageParts[0];
                        String firstName = messageParts[1];
                        String lastName = messageParts[2];
                        String status = messageParts[3];
                        String grade = messageParts[4];
                        String ban = messageParts[5];
                        User newUser = create_user(pseudo, lastName, firstName, status, grade, ban);
                        userList.add(newUser);
                    }
                }
                case "update_status" -> {
                    String status = parts[2];
                    String username = parts[3];
                    for (User user : userList) {
                        if (user.getPseudo().equals(username)) {
                            user.setStatus(status);
                        }
                    }
                }

                case "ban" -> {
                    String pseudo = parts[2];
                    for (User user : userList) {
                        if (user.getPseudo().equals(pseudo)) {
                            user.setBan(true);
                            user.setStatus(Status.Offline);
                        }
                    }
                }

                case "upgrade" -> {
                    String pseudo = parts[2];
                    String grade = parts[3];
                    for (User user : userList) {
                        if (user.getPseudo().equals(pseudo)) {
                            if (grade.equals("Administrator")){
                                user.setGrade(Grades.Administrator);
                            }
                            else {
                                user.setGrade(Grades.Moderator);
                            }

                        }
                    }
                }
            }
        }
        else if (type.equals("MESSAGE")) {
            if (command.equals("getChat")) {
                String[] messages = parts[2].split("/"); // Séparer tous les messages
                for (String message : messages) {
                    String[] messageParts = message.split("_");
                    String content = messageParts[0];
                    String timestamp = messageParts[1];
                    String author = messageParts[2];
                    Message newMessage = new Message(author, timestamp, content);
                    conversation.getConversation().add(newMessage);
                }
            }
            else if(command.equals("newMessage")){
                String author = parts[2];
                String timestamp = parts[3];
                String content = parts[4];
                Message newMessage = new Message(author,timestamp,content);
                conversation.getConversation().add(newMessage);
            }
        }
        else {
            System.out.println("Invalid command");
        }
    }

    /**
     * Crée un nouvel utilisateur avec les informations données.
     *
     * @param pseudo     le pseudo de l'utilisateur
     * @param last_name  le nom de famille de l'utilisateur
     * @param first_name le prénom de l'utilisateur
     * @param status     le statut de l'utilisateur ("Online", "Offline" ou "Away")
     * @param grade      le grade de l'utilisateur (Administrateur, Modérateur ou Utilisateur)
     * @param ban        indique si l'utilisateur est banni (true ou false)
     * @return un nouvel objet User avec les informations données
     */
    private User create_user(String pseudo, String last_name, String first_name, String status, String grade, String ban){
        return new User(pseudo,last_name,first_name,grade,status,ban);
    }
}