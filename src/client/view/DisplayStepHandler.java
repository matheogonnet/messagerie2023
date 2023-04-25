package client.view;

import server.clientConnectionManagementModule.ThreadListenClient;
import client.model.Conversation;
import client.model.User;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe qui permet d'afficher toute la view
 */
public class DisplayStepHandler {
    /**
     * constructeur privé pour ne pas instancier cette classe
     */
    private DisplayStepHandler() {
    }

    private static ThreadListenClient threadListenClient;

    public static int level = 0;
    public static boolean isDisplay = false;

    /**
     * Change le niveau de l'affichage et indique qu'aucune fenêtre n'est actuellement affichée.
     *
     * @param stage le nouveau niveau d'affichage
     */
    public static void setDisplay(int stage) {
        level = stage;
        isDisplay = false;
    }

    /**
     * Méthode utilisée pour déterminer quel affichage entre la fenêtre
     * de menu, la fenêtre de connexion, la fenêtre d'inscription, la fenêtre de discussion,
     * ou une fenêtre d'erreur en cas de bannissement.
     *
     * @param client       l'objet client utilisé pour envoyer et recevoir des messages du serveur
     * @param conversation la conversation actuelle du client
     * @param userList     la liste des utilisateurs connectés au serveur
     * @throws IOException si une erreur survient lors de la communication avec le serveur
     */
    public static void view(Client client, Conversation conversation, ArrayList<User> userList) throws IOException {

        Login login = new Login();
        SignUp signUp = new SignUp();
        Chat chat = new Chat();
        Menu menu = new Menu();
        boolean run = true;

        while (run) {
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
                        login.display(client);
                    }
                    System.out.print("");
                }
                case 2 -> { //affichage de la fenetre de sign up
                    if (!isDisplay) {
                        isDisplay = true;
                        signUp.display(client, userList);
                    }
                    System.out.print("");
                }
                case 3 -> { //affichage du salon
                    if (!isDisplay) {
                        isDisplay = true;
                        chat.display(client, conversation, userList);
                    }
                    System.out.print("");
                }

                case 4 -> { //affichage de la fenêtre d'erreur en cas de bannissement
                    JFrame banFrame = new JFrame();
                    JOptionPane.showMessageDialog(banFrame, "Vous avez été banni !");
                    Window.closeWindow(banFrame);
                    run = false;

                }
                case 5 -> { //inscription d'un nouvel utilisateur dans la BDD
                    level = 0;
                }
                case 6 -> { //écriture d'un nouveau message dans la conversation
                    level = 3;

                }
                default -> {
                }
                // Si la valeur de "level" ne correspond à aucun des cas ci-dessus, faire quelque chose ici
            }

        }
    }
}

