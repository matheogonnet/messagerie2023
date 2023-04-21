package client.view;

import client.controller.MessageController;
import client.controller.UserController;
import client.model.Conversation;
import client.model.User;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class DisplayStepHandler {

    //constructeur privé pour ne pas instancier cette class
    private DisplayStepHandler(){}
    private static String serverHost = "localhost"; // adresse IP ou nom d'hôte du serveur
    private static int serverPort=  4000; // port sur lequel le serveur écoute les connexions entrantes
    private static Socket socket; // objet Socket pour communiquer avec le serveur

    private static PrintWriter writer; // objet PrintWriter pour écrire des données à envoyer au serveur
    public static String clientMessage;
    private static BufferedReader reader; // flux d'entrée (réception des messages du serveur)

    private static ThreadListenClient threadListenClient;

    public static int level = 0;
    public static boolean isDisplay = false;
    private static User user= new User();
    private static MessageController messageController;

    public static void setDisplay(int stage){
        level = stage;
        isDisplay = false;
    }


    public static void run(UserController userController, DaoUser daoUser, DaoMessage daoMessage) throws SQLException {

        Conversation conversation = new Conversation("Salon");
        //retrouver l'historique des messages
        conversation.setConversation(daoMessage.findAll()); // doit etre salon

        Login login = new Login();
        SignUp signUp = new SignUp();
        Salon salon = new Salon();
        Menu menu = new Menu();
        boolean close =true;

        while(true){
            switch (level) {
                case 0 -> { //affichage de la fenetre de menu (démarrage)
                    if (!isDisplay) {
                        isDisplay = true;
                        menu.display();
                    }
                    System.out.print("");
                }
                case 1 -> { //affichage de la fenetre de login
                    if (!isDisplay) {
                        isDisplay = true;
                        login.display();
                    }
                    System.out.print("");
                }
                case 2 -> { //affichage de la fenetre de sign up
                    if (!isDisplay) {
                        isDisplay = true;
                        signUp.display();
                    }
                    System.out.print("");
                }
                case 3 -> { //affichage du salon
                    if (!isDisplay) {
                        isDisplay = true;
                        System.out.println("--- OUVERTURE DU SALON ---");
                        salon.display(user, daoUser, userController, conversation);
                    }
                    System.out.print("");
                }

                // Appeler la fonction userController.logIn() pour connecter l'utilisateur
                case 4 -> { //verifier les données du login dans la BDD
                    if (userController.loginUser(login.getPseudo(), login.getPassword())) {
                        login.setHasAccess(1);
                        user = userController.getUser();
                        try {
                            // Créer une socket pour communiquer avec le serveur
                            socket = new Socket(serverHost, serverPort);
                            // Initialiser les flux de sortie et d'entrée pour la communication avec le serveur
                            writer = new PrintWriter(socket.getOutputStream(), true);
                            writer.println(user.getPseudo());
                            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                            System.out.println("Vous êtes connecté avec le nom d'utilisateur " + user.getPseudo());

                            // Démarrer un thread pour écouter les messages entrants du serveur
                            threadListenClient = new ThreadListenClient(reader, null, user.getPseudo());
                            threadListenClient.start();
                        } catch (IOException e) {

                            System.err.println("IOException occurred.");
                            return;
                        }

                        messageController = new MessageController(user,daoMessage,conversation);
                        level = 1;
                    } else {
                        login.setHasAccess(0);
                        level = 1;
                    }
                }
                case 5 -> { //inscrire un nouvel user dans la BDD
                    userController.registerUser(signUp.getFirstName(), signUp.getLastName(), signUp.getPassword(), signUp.getPseudo());
                    level = 0;
                }
                case 6 -> { //ecrire un nouveau message
                    messageController.send(clientMessage);
                    writeMessage(clientMessage);
                    level = 3;

                }
                default -> {
                }
                // Si la valeur de "level" ne correspond à aucun des cas ci-dessus, faire quelque chose ici
            }
        }



    }

    private static void writeMessage(String message) {
        System.out.println("Begin chat with " + serverHost + " on port " + serverPort);

        // Envoie le message au serveur
        writer.println(user.getPseudo() + ": " + message);

        // Vérifie si l'utilisateur souhaite se déconnecter
        if (message.equals("/disconnect") || !threadListenClient.running) {
            System.out.println("Exiting...");
            try {
                threadListenClient.running = false;
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket.");
            }

            System.out.println("Chat terminated");
        } else {
            System.out.println(user.getPseudo() + ": " + message);
        }

        // Arrête le thread d'écoute des messages du serveur et ferme la connexion avec le serveur

    }

}

