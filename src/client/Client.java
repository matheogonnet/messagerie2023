package client;
import client.view.ChatClient;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException  {
        //System.out.println("Hello world : graphique, test");
        //User user = new User();
        //Menu menu = new Menu(user);

        System.out.println("Bienvenue sur le chat !");
        new ChatClient("localhost", 4000);

    }
}



