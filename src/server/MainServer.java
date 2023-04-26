package server;

// Importation des classes nécessaires pour le fonctionnement du serveur
import server.clientConnectionManagementModule.Server;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Classe principale du serveur.
 */
public class MainServer {

    /**
     * Méthode principale pour démarrer le serveur.
     *
     * @param args les arguments de la ligne de commande
     * @throws IOException si une erreur d'E/S se produit
     * @throws SQLException si une erreur de base de données se produit
     */
    public static void main(String[] args) throws IOException, SQLException {
        // Création d'une instance de DaoUser pour gérer les utilisateurs avec une connexion à la base de données
        DaoUser daoUser = new DaoUser("jdbc:mysql://localhost:3306/pi2023", "root", "");

        // Création d'une instance de DaoMessage pour gérer les messages avec une connexion à la base de données
        DaoMessage daoMessage = new DaoMessage("jdbc:mysql://localhost:3306/pi2023", "root", "");

        // Ouverture des connexions à la base de données pour daoUser et daoMessage
        daoMessage.openConnection();
        daoUser.openConnection();


        // Création et démarrage d'une instance de Server avec un port d'écoute, userController, daoMessage et daoUser
        new Server(4000, daoMessage, daoUser);

        // Fermeture des connexions à la base de données pour daoUser et daoMessage
        daoUser.closeConnection();
        daoMessage.closeConnection();
    }
}
