package client.controller;

import client.model.Grades;
import client.model.User;
import server.dataAccesModule.DaoUser;
import java.sql.SQLException;
import java.util.Scanner;

public class UserController {

    private DaoUser userDao;
    private User user;

    public UserController(DaoUser userDao) {
        //this.user = user;
        this.userDao = userDao;
    }


    public void registerUser() throws SQLException {
        //créer un user classic
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your pseudo: ");
        String pseudo = scanner.nextLine();

        this.user = new User(lastName,firstName,pseudo, password);

        userDao.add(user);
        userDao.update(user);
    }

    public void loginUser() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;
        do {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();


            password = User.hashPassword(password);

            User userFind = userDao.findByPseudo(username);


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



}
