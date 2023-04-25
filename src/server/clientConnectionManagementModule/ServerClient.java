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

    // Envoi d'un message au client via le flux d'écriture
    public void send(String message) throws IOException {
        if (message.equals("/disconnected")) close();
        else writer.println(message);
    }

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
                case "ban_user":
                    String pseudo = parts[2];
                    ban(pseudo);

                    break;
                case "unban_user":
                    //dao uunban
                    break;
                case "upgrade":
                    //dao uograde...
                    break;
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





    private void reporting() throws SQLException {
        Reporting reporting = new Reporting(daoUser.findAll());
        reporting.GraphGrades();
        reporting.GraphStatus();
        reporting.GraphTopUser(daoMessage);
    }

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

    private void signUp(String lastName, String firstName, String pseudo, String password) throws SQLException, IOException {
        User newUser = new User(lastName, firstName, pseudo, password);
        daoUser.add(newUser);
        Status status = newUser.getStatus();
        Grades grade = newUser.getGrade();
        Boolean ban = newUser.isBan();
        String message = ("USER::signUp::"+pseudo +"::"+firstName +"::"+lastName+"::"+status+"::"+grade+"::"+ban);
        server.sendToAllClients(message, id);
    }

    private void ban(String pseudo) throws SQLException, IOException {
        User user = daoUser.findByPseudo(pseudo);
        user.setBan(true);
        daoUser.update(user);
        server.sendToAllClients("USER::ban::"+user.getPseudo(),id);
    }

    private void setStatus(String username, String status) throws SQLException, IOException {
        daoUser.setDaoStatus(status, username);
        System.out.println(username + " is now " + status);
        String message = "USER::update_status::" + status + "::" + username;
        //informe tous les clients que le user "username" a changer son statut
        server.sendToAllClients(message, id);
    }


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

    private void sendDaoMessage(String author, String timestamp, String content) throws SQLException {
        Message message = new Message(author,timestamp,content);
        daoMessage.add(message);
    }

    private void deleteDaoMessage(String author, String timestamp, String content) throws SQLException{
        Message message = new Message(author,timestamp,content);
        daoMessage.delete(message);
    }

    public void stopRunning(){
        threadListenClient.running = false; // Arrêt du thread pour écouter les messages du client
    }
}
