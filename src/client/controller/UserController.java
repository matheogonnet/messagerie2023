package client.controller;

import client.model.Grades;
import client.model.Status;
import client.model.User;
import server.dataAccesModule.DaoUser;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class UserController {

    private DaoUser userDao;
    private User user;

    public UserController(DaoUser userDao) {
        //this.user = user;
        this.userDao = userDao;
    }


    ///REGISTER////
    public void registerUser(String firstName, String lastName, String password, String pseudo) throws SQLException {
        //créer un user classic
        this.user = new User(lastName,firstName,pseudo, password);
        userDao.add(user);
        userDao.update(user);
    }

    ///LOGIN////
    public void loginUser(String pseudo, String password) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;
        do {
            assert password != null;
            password = User.hashPassword(password);

            User userFind = userDao.findByPseudo(pseudo);


            if (userFind != null && userFind.getPassword().equals(password)) {
                System.out.println("Login successful" + userFind);
                //on met à jour son statut : online
                userFind.log_in();
                this.user = userFind;
                userDao.update(user);
                ok = false;
            } else {
                System.out.println("Incorrect username or password");
            }
        }while(ok);
    }

    ///BAN////
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

    public void setStatus(String status) throws SQLException {
        if (status.equals("Online")){
            user.setStatus(Status.Online);
        }
        else if(status.equals("Offline")){
            user.setStatus(Status.Offline);
        }
        else {
            user.setStatus(Status.Away);
        }
        userDao.update(user);

    }



}
