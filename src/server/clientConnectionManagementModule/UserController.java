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

     Getter de l'utilisateur actuellement connecté
     @return l'utilisateur actuellement connecté
     */

    public User getUser() {
        return user;
    }

    private User user;

    /**
     * Constructeur de la classe UserController
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
    public void registerUser(String firstName, String lastName, String password, String pseudo) throws SQLException {
        //créer un user classic
        this.user = new User(lastName,firstName,pseudo, password);
        userDao.add(user);
        //userDao.update(user);
    }

    ///LOGIN////

    /**
     * Connecte un utilisateur à l'application en vérifiant ses informations de connexion
     * @param pseudo le pseudo de l'utilisateur à connecter
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

    ///BAN////
    /**
     * Bannit un utilisateur en modifiant son statut dans la base de données
     * @param userToBan l'utilisateur à bannir
     * @throws SQLException si une erreur SQL se produit lors de la modification du statut de l'utilisateur
     */
    public void ban_user(User userToBan) throws SQLException {

        // Vérifier que l'utilisateur cible n'est pas déjà banni
        if (userToBan.isBan()) {
            System.out.println("L'utilisateur " + userToBan.getPseudo() + " est déjà banni.");


        }
        else if (user.getGrade() == Grades.Moderator && userToBan.getGrade() == Grades.Classic){
            user.ban(userToBan);
            userDao.update(userToBan);
            System.out.println("L'utilisateur " + userToBan.getPseudo() + " a été banni par " + user.getPseudo());
            // Code à écrire pour la méthode ban_user()
            //+ interface
        }
        else if(user.getGrade() == Grades.Administrator && (userToBan.getGrade() == Grades.Classic || userToBan.getGrade() == Grades.Moderator)){
            user.ban(userToBan);
            userDao.update(userToBan);
            System.out.println("L'utilisateur " + userToBan.getPseudo() + " a été banni par " + user.getPseudo());
            // Code à écrire pour la méthode ban_user()
            //+ interface
        }
        else {
            System.out.println("Vous n'avez pas les droits");
            //ecrire le code +interface
        }
    }

    ///UPGRADE////

    /**
     * Met à jour le statut d'un utilisateur en modifiant son grade dans la base de données
     * @param userToUpgrade l'utilisateur à mettre à jour
     * @throws SQLException si une erreur SQL se produit lors de la modification du grade de l'utilisateur
     */
    public void upgrade_user(User userToUpgrade) throws SQLException {

        if (userToUpgrade.isBan()) {
            System.out.println("L'utilisateur " + userToUpgrade.getPseudo() + " est banni.");
        }
        else if (user.getGrade() == Grades.Administrator){

            if (userToUpgrade.getGrade() == Grades.Administrator) {
                System.out.println("L'utilisateur est déjà un administrateur.");
            }
            else if (userToUpgrade.getGrade() == Grades.Moderator) {
                //afficher un message de demande confirmation
                //le grade est mit a jour
                userToUpgrade.setGrade(Grades.Administrator);
                userDao.update(userToUpgrade);
                System.out.println("L'utilisateur " + userToUpgrade.getPseudo() + " est passé Administrateur ");

            }
            else { //user est un user normal
                //le grade est mit à jour
                userToUpgrade.setGrade(Grades.Moderator);
                userDao.update(userToUpgrade);
                System.out.println("L'utilisateur " + userToUpgrade.getPseudo() + " est passé Modérateur ");

            }
        }
        else {
            System.out.println("Vous n'avez pas les droits");
        }

    }

    /**
     * Signale un utilisateur pour un comportement inapproprié
     * @param reporting l'objet Reporting qui contient les informations sur l'utilisateur signalé et le signaleur
     * @throws SQLException si une erreur SQL se produit lors de l'enregistrement du signalement
     */
    public void reportingUser(Reporting reporting) throws SQLException{
        if (user.getGrade() == Grades.Administrator) {
            reporting.GraphGrades();
            reporting.GraphStatus();
            reporting.GraphTopUser(daomessage);
            reporting.GraphMessageTime(daomessage);
            //reporting.GraphLoginTime(userDao);
        }
    }


}
