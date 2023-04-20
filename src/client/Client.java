package client;
import client.controller.UserController;
import client.model.User;
import client.view.ChatClient;
import client.view.Menu;
import server.dataAccesModule.DaoUser;


import java.io.IOException;
import java.sql.SQLException;


public class Client {
    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Hello world : graphique, test");
        //User user = new User();
        DaoUser daoUser = new DaoUser("jdbc:mysql://localhost:3306/pi2023","root","");
        daoUser.openConnection();
        UserController userController = new UserController(daoUser);
        Menu menu = new Menu(userController);

        System.out.println("Bienvenue sur le chat !");
        new ChatClient("localhost", 4000);
        daoUser.closeConnection();

    }
}



