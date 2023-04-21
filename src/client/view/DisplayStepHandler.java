package client.view;

import client.controller.UserController;
import client.model.User;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.sql.SQLException;

public class DisplayStepHandler {

    //constructeur privé pour ne pas instancier cette class
    private DisplayStepHandler(){}

    public static int level = 0;
    public static boolean isDisplay = false;

    public static void setDisplay(int stage){
        level = stage;
        isDisplay = false;
    }


    public static void run(UserController userController, DaoUser daoUser, DaoMessage daoMessage) throws SQLException {
        User user= new User();
        Login login = new Login();
        SignUp signUp = new SignUp();
        Salon salon = new Salon();
        Menu menu = new Menu();
        boolean close =true;

        do{
            switch (level) {
                case 0 -> { //affichage de la fenetre de menu (démarrage)
                    if (!isDisplay) {
                        isDisplay = true;
                        menu.display();
                    }
                    System.out.print("");
                }
                case 1 -> { //affichage de la fenetre de login
                    if (!isDisplay) {
                        isDisplay = true;
                        login.display();
                    }
                    System.out.print("");
                }
                case 2 -> { //affichage de la fenetre de sign up
                    if (!isDisplay) {
                        isDisplay = true;
                        signUp.display();
                    }
                    System.out.print("");
                }
                case 3 -> { //affichage du salon
                    if (!isDisplay) {
                        isDisplay = true;
                        System.out.println("--- OUVERTURE DU SALON ---");
                        salon.display(user, daoUser, daoMessage, userController);
                    }
                    System.out.print("");
                }

                // Appeler la fonction userController.logIn() pour connecter l'utilisateur
                case 4 -> { //verifier les données du login dans la BDD
                    if (userController.loginUser(login.getPseudo(), login.getPassword())) {
                        login.setHasAccess(1);
                        user = userController.getUser();
                        level = 1;
                    } else {
                        login.setHasAccess(0);
                        level = 1;
                    }
                }
                case 5 -> { //inscrire un nouvel user dans la BDD
                    userController.registerUser(signUp.getFirstName(), signUp.getLastName(), signUp.getPassword(), signUp.getPseudo());
                    level = 0;
                }
                case 6 -> {

                }
                default -> {
                }
                // Si la valeur de "level" ne correspond à aucun des cas ci-dessus, faire quelque chose ici
            }
        }
        while (close);
    }
}
