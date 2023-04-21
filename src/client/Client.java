package client;
import client.controller.UserController;
import client.model.User;
import client.view.ChatClient;
import client.view.DisplayStepHandler;
import client.view.Menu;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;


import java.io.IOException;
import java.sql.SQLException;


public class Client {
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("--- CLIENT ---");

        //ouverture d'un daoUser dans la BDD
        DaoUser daoUser = new DaoUser("jdbc:mysql://localhost:3306/pi2023","root","");
        DaoMessage daoMessage = new DaoMessage("jdbc:mysql://localhost:3306/pi2023","root","");
        daoMessage.openConnection();
        daoUser.openConnection();

        //creation d'un controlleur pour les fonctions user
        UserController userController = new UserController(daoUser);

        //view
        DisplayStepHandler.run(userController, daoUser, daoMessage);


        //new ChatClient("localhost", 4000);
        daoUser.closeConnection();
        daoMessage.closeConnection();


    }
}



