package client;

import client.controller.Client;

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
