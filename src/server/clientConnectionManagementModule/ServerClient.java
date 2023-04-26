package server.clientConnectionManagementModule;


import client.controller.Reporting;
import client.model.Grades;
import client.model.Message;
import client.model.Status;
import client.model.User;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**


    Cette classe représente un client connecté au serveur.

    Elle permet de gérer la communication entre le client et le serveur.
    */
public class ServerClient{

    public BufferedReader reader;
    public PrintWriter writer;
    public ThreadListenClient threadListenClient; // Thread pour écouter les messages du client
    private UserController controller;
    private DaoMessage daoMessage;
    private DaoUser daoUser;
    private int id;
    private Server server;
    private Socket clientSocket;

    public int getId() {
        return id;
    }

    /**

     * Constructeur de la classe ServerClient
     * @param clientSocket Socket associé au client
     * @param server Référence vers le serveur
     * @param userController Contrôleur des utilisateurs
     * @param messageDao DAO des messages
     * @param userDao DAO des utilisateurs
     * @param id ID du client



     */

    public ServerClient(Socket clientSocket, Server server, UserController userController, DaoMessage messageDao, DaoUser userDao, int id) {
        this.daoMessage = messageDao;
        this.daoUser = userDao;
        this.id = id;
        this.server = server;
        this.controller = userController;
        this.clientSocket = clientSocket;
        try{
            // Initialisation des flux de lecture et d'écriture
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            getDaoChat(daoMessage);
            getDaoUserList(daoUser);
            threadListenClient = new ThreadListenClient(reader, server, this); // Initialisation du thread pour écouter les messages du client
            threadListenClient.start(); // Démarrage du thread pour écouter les messages du client


        } catch(Exception e){
            System.out.println("Erreur lors de l'acceptation de la connexion ");
            e.printStackTrace();
        }
    }

    // méthode close()
    public void close() throws IOException {
        clientSocket.close();
    }

    /**
     * Envoi d'un message au client via le flux d'écriture
     */

    public void send(String message) throws IOException {
        if (message.equals("/disconnected")) close();
        else writer.println(message);
    }

    /**

     Cette méthode traite les messages reçus par le client.
     Elle prend en entrée le message du client et le traite selon son type et sa commande.
     Si le type est "USER_REQUEST", la commande est traitée en fonction de son contenu.
     Si le type est "MESSAGE", la commande est également traitée en fonction de son contenu.
     Si le type est inconnu, un message d'erreur est affiché.
     @param messageClient le message envoyé par le client
     @throws SQLException si une erreur survient lors de l'exécution d'une requête SQL
     @throws IOException si une erreur survient lors de la lecture ou l'écriture d'un flux
     */
    public void handleMessage(String messageClient) throws SQLException, IOException {

        String[] parts = messageClient.split("::");
        String type = parts[0];
        String command = parts[1];
        System.out.println("---------------------------------------\n    NOUVEAU MESSAGE RECU DU CLIENT N°"+id+"    \n    type: "+type+" commande: "+command+"\n---------------------------------------");


        if (type.equals("USER_REQUEST")) {
            switch (command) {
                case "login": {
                    String pseudo = parts[2];
                    String password = parts[3];
                    login(password, pseudo);
                    break;
                }
                case "log_out":{
                    String pseudo = parts[2];
                    logout(pseudo);
                }

                case "signUp": {

                    String pseudo = parts[2];
                    String firstName = parts[3];
                    String lastName = parts[4];
                    String password = parts[5];
                    signUp(lastName,firstName,pseudo,password);
                    break;
                }
                case "setStatus": {
                    String status = parts[2];
                    String username = parts[3];
                    setStatus(username,status);
                    break;
                }
                case "reporting":{
                    reporting();
                }
                case "ban_user":{
                    String pseudo = parts[2];
                    ban(pseudo);
                    break;
                }

                case "upgrade": {
                    String pseudo = parts[2];
                    String grade = parts[3];
                    upgrade(pseudo, grade);
                    break;
                }
            }

        }else if (type.equals("MESSAGE")) {
            if (command.equals("sendMessage")){
                String author = parts[2];
                String timestamp = parts[3];
                String content = parts[4];
                sendDaoMessage(author,timestamp,content);
                String message =("MESSAGE::newMessage::"+author+"::"+timestamp+"::"+content);
                server.sendToAllClients(message,id);
            }
            if (command.equals("delete")){
                String author = parts[2];
                String timestamp = parts[3];
                String content = parts[4];
                deleteDaoMessage(author,timestamp,content);
            }
        }
        else {
            System.out.println("Invalid command");
        }
    }





    /**

     * Cette méthode crée un rapport en utilisant l'objet Reporting, en appelant les méthodes GraphGrades(), GraphStatus()
     *      et GraphTopUser(), qui sont utilisées pour créer des graphiques contenant des informations sur les utilisateurs et les messages.
     * Les données utilisées pour générer ces graphiques sont obtenues à partir de la base de données en utilisant les objets DaoUser et DaoMessage.
     * La méthode lève une SQLException en cas d'erreur de communication avec la base de données.

     */
    private void reporting() throws SQLException {
        Reporting reporting = new Reporting(daoUser.findAll());
        reporting.GraphGrades();
        reporting.GraphStatus();
        reporting.GraphTopUser(daoMessage);
    }

    /**
     * Cette méthode permet de gérer la connexion d'un utilisateur en vérifiant les informations de login.
     * Si les informations sont correctes, l'utilisateur est connecté et un message de confirmation est envoyé au client.
     * Si les informations sont incorrectes, un message d'erreur est envoyé au client.
     * @param password le mot de passe de l'utilisateur
     * @param pseudo le pseudo de l'utilisateur
     * @throws SQLException si une erreur SQL se produit lors de l'accès à la base de données
     * @throws IOException si une erreur d'entrée/sortie se produit lors de l'envoi du message au client
     */
    private void login(String password, String pseudo) throws SQLException, IOException {
        if (controller.loginUser(pseudo,password)){
            String firstName = controller.getUser().getFirst_name();
            String lastName = controller.getUser().getLast_name();
            Status status = Status.Online; //le status est directement est directement mit online à la connexion
            Grades grade = controller.getUser().getGrade();
            Boolean ban = controller.getUser().isBan();
            send("USER::login::ACCES GRANTED::"+pseudo+"::"+firstName+"::"+lastName+"::"+status+"::"+grade+"::"+ban);

        }
        else send("USER::login::ACCES DENIED");
    }

    private void  logout(String pseudo) throws SQLException, IOException {
        User user = daoUser.findByPseudo(pseudo);
        user.setStatus(Status.Offline);
        daoUser.update(user);
        server.sendToAllClients("USER::log_out::"+pseudo, id);
    }


    /**

     * Crée un nouvel utilisateur avec les informations fournies, l'ajoute à la base de données et envoie un message de notification à tous les clients connectés.
     * @param lastName Le nom de famille du nouvel utilisateur.
     * @param firstName Le prénom du nouvel utilisateur.
     * @param pseudo Le pseudonyme du nouvel utilisateur.
     * @param password Le mot de passe du nouvel utilisateur.
     * @throws SQLException Si une erreur survient lors de l'accès à la base de données.
     * @throws IOException Si une erreur survient lors de l'envoi du message de notification aux clients connectés.
     */

    private void signUp(String lastName, String firstName, String pseudo, String password) throws SQLException, IOException {
        User newUser = new User(lastName, firstName, pseudo, password);
        daoUser.add(newUser);
        Status status = newUser.getStatus();
        Grades grade = newUser.getGrade();
        Boolean ban = newUser.isBan();
        String message = ("USER::signUp::"+pseudo +"::"+firstName +"::"+lastName+"::"+status+"::"+grade+"::"+ban);
        server.sendToAllClients(message, id);
    }

    /**

     * Met un utilisateur en mode "banni"
     * @param pseudo le pseudo de l'utilisateur à bannir
     * @throws SQLException si une erreur SQL se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    private void ban(String pseudo) throws SQLException, IOException {
        User user = daoUser.findByPseudo(pseudo);
        user.setBan(true);
        daoUser.update(user);
        server.sendToAllClients("USER::ban::"+user.getPseudo(),id);
    }

    private void upgrade(String pseudo, String grade) throws SQLException, IOException {
        User user = daoUser.findByPseudo(pseudo);
        if (grade.equals("Administrator"))user.setGrade(Grades.Administrator);
        else user.setGrade(Grades.Moderator);
        daoUser.update(user);
        server.sendToAllClients("USER::upgrade::"+user.getPseudo()+"::"+grade,id);
    }


    /**

     * Modifie le statut d'un utilisateur
     * @param username le nom d'utilisateur de l'utilisateur dont le statut doit être modifié
     * @param status le nouveau statut
     * @throws SQLException si une erreur SQL se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    private void setStatus(String username, String status) throws SQLException, IOException {
        daoUser.setDaoStatus(status, username);
        System.out.println(username + " is now " + status);
        String message = "USER::update_status::" + status + "::" + username;
        //informe tous les clients que le user "username" a changer son statut
        server.sendToAllClients(message, id);
    }


    /**

     * Récupère toutes les conversations enregistrées dans la base de données et les envoie au client.

     * @param daoMessage une instance de la classe DaoMessage permettant d'accéder aux messages enregistrés.

     * @throws SQLException si une erreur survient lors de l'accès à la base de données.

     * @throws IOException si une erreur survient lors de l'envoi du message au client.
     */
    private void getDaoChat(DaoMessage daoMessage) throws SQLException, IOException {
        ArrayList<Message> conversation = daoMessage.findAll();
        StringBuilder messageString = new StringBuilder();
        for (Message message : conversation) {
            messageString.append(message.getContent()).append("_")
                    .append(message.getTimeStamp()).append("_")
                    .append(message.getAuthor())
                    .append("/");
        }
        send("MESSAGE::getChat::"+messageString);

    }

    /**

     * Récupère la liste de tous les utilisateurs enregistrés dans la base de données et les envoie au client sous forme de message
     * @param daoUser l'objet DaoUser pour accéder à la base de données
     * @throws SQLException si une erreur SQL se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    private void getDaoUserList(DaoUser daoUser) throws SQLException, IOException {
        ArrayList<User> userList = daoUser.findAll();
        StringBuilder messageString = new StringBuilder();
        for (User user : userList) {
            messageString.append(user.getPseudo()).append("_")
                    .append(user.getFirst_name()).append("_")
                    .append(user.getLast_name()).append("_")
                    .append(user.getStatus()).append("_")
                    .append(user.getGrade()).append("_")
                    .append(user.isBan())
                    .append("/");
        }
        send("USER::getUserList::"+messageString);
    }


    /**

     * Ajoute un message à la base de données
     * @param author l'auteur du message
     * @param timestamp la date d'envoi du message
     * @param content le contenu du message
     * @throws SQLException si une erreur SQL se produit
     */

    private void sendDaoMessage(String author, String timestamp, String content) throws SQLException {
        Message message = new Message(author,timestamp,content);
        daoMessage.add(message);
    }


    /**

     * Supprime un message de la base de données
     * @param author l'auteur du message
     * @param timestamp la date d'envoi du message
     * @param content le contenu du message
     * @throws SQLException si une erreur SQL se produit
     */
    private void deleteDaoMessage(String author, String timestamp, String content) throws SQLException{
        Message message = new Message(author,timestamp,content);
        daoMessage.delete(message);
    }


    /**

     * Arrête l'exécution du thread pour écouter les messages du client
     */
    public void stopRunning(){
        threadListenClient.running = false; // Arrêt du thread pour écouter les messages du client
    }
}
