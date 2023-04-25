package client.view;

import javax.swing.*;
/**
 *  Cette classe contient des méthodes pour gérer les fenêtres swing.
 */
public class Window {

    /**
     *  Ferme la fenêtre spécifiée.
     *  @param frame la fenêtre à fermer
     */
    static public void closeWindow(JFrame frame ){
        frame.dispose();
        frame.setVisible(false);
    }

    /**
     * Affiche la fenêtre spécifiée.
     * @param frame la fenêtre à afficher
     */
    static public void displayWindow(JFrame frame){
        frame.setVisible(true);
    }

}
