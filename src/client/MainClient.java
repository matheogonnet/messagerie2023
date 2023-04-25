package client;

import client.view.Client;
import server.clientConnectionManagementModule.UserController;
import client.view.DisplayStepHandler;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Classe principale du client.
 */
public class MainClient {
    /**
     * Point d'entrée du programme client.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        System.out.println("--- CLIENT ---");

        new Client("localhost", 4000);
    }
}
