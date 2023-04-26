package server.clientConnectionManagementModule;

import client.controller.Reporting;
import client.model.Grades;
import client.model.Status;
import client.model.User;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Cette classe sert à contrôler les actions de l'utilisateur et les données d'utilisateur
 */
public class UserController {

    private DaoUser userDao;

    private DaoMessage daomessage;

    /**
     * Getter de l'utilisateur actuellement connecté
     *
     * @return l'utilisateur actuellement connecté
     */

    public User getUser() {
        return user;
    }

    private User user;

    /**
     * Constructeur de la classe UserController
     *
     * @param userDao Objet DaoUser pour interagir avec la base de données des utilisateurs
     */
    public UserController(DaoUser userDao) {
        //this.user = user;
        this.userDao = userDao;
    }


    ///REGISTER////
    /**
     * Enregistre un nouvel utilisateur dans la base de données
     * @param firstName le prénom de l'utilisateur à enregistrer
     * @param lastName le nom de famille de l'utilisateur à enregistrer
     * @param password le mot de passe de l'utilisateur à enregistrer
     * @param pseudo le pseudo de l'utilisateur à enregistrer
     * @throws SQLException si une erreur SQL se produit lors de l'enregistrement de l'utilisateur
     */

    ///LOGIN////

    /**
     * Connecte un utilisateur à l'application en vérifiant ses informations de connexion
     *
     * @param pseudo   le pseudo de l'utilisateur à connecter
     * @param password le mot de passe de l'utilisateur à connecter
     * @return true si la connexion est réussie, false sinon
     * @throws SQLException si une erreur SQL se produit lors de la vérification des informations de connexion
     */
    public boolean loginUser(String pseudo, String password) throws SQLException {
        assert password != null;
        password = User.hashPassword(password);
        User userFind = userDao.findByPseudo(pseudo);


        if (userFind != null && userFind.getPassword().equals(password)) {
            System.out.println("Login successful");
            //on met à jour son statut : online
            userFind.log_in();
            this.user = userFind;
            userDao.update(user);
            return true;
        } else {
            System.out.println("Incorrect username or password");
            return false;
        }
    }
}