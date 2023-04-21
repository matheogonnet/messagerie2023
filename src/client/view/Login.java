package client.view;

import javax.swing.*;
import java.awt.*;

public class Login {

    private JFrame loginFrame;
    private String password = null, pseudo = null;
    private int hasAccess = 2;


    public void setHasAccess(int hasAccess) {
        this.hasAccess = hasAccess;
    }

    public String getPassword() {
        return password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void display(){

        loginFrame = new JFrame("Log In");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Créer les panels pour le Log In
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel logInPanel = new JPanel(new GridBagLayout());
        logInPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));

        // Ajouter les labels et les champs de texte pour le Log In
        JLabel labelPseudo = new JLabel("Pseudo :");
        JTextField pseudoField = new JTextField("",15);

        JLabel labelMDP = new JLabel("Mot de Passe :");
        JPasswordField passwordField = new JPasswordField("",15);


        // Ajouter les éléments au panel avec des contraintes pour les centrer
        // On utilise une grille GBC pour bien pouvoir placer les éléments
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(15, 5, 15, 5); // add vertical spacing
        grid.gridx = 0;
        grid.gridy = 0;
        grid.anchor = GridBagConstraints.CENTER;
        logInPanel.add(labelPseudo, grid);
        grid.gridx = 1;
        logInPanel.add(pseudoField, grid);

        grid.gridx = 0;
        grid.gridy = 1;
        logInPanel.add(labelMDP, grid);
        grid.gridx = 1;
        logInPanel.add(passwordField, grid);


        // On crée les boutons, on les rajoute sur leur propre panel
        JPanel buttonPannel = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        buttonPannel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        JButton logInButton2 = new JButton("Log In");
        logInButton2.setPreferredSize(new Dimension(100, 30));
        buttonPannel.add(logInButton2);
        JButton retourButton = new JButton("Retour");
        retourButton.setPreferredSize(new Dimension(100, 30));
        buttonPannel.add(retourButton);

        // Et on rajoute les 2 panels sur le main panel
        mainPanel.add(logInPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPannel, BorderLayout.SOUTH);




        // Ajouter le panel au frame et afficher la fenêtre
        loginFrame.add(mainPanel);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null); // center the frame
        if (hasAccess==1){
            JOptionPane.showMessageDialog(loginFrame, "LogIn complet", "LogIn Complet", JOptionPane.INFORMATION_MESSAGE);
            DisplayStepHandler.setDisplay(3);
            Window.closeWindow(loginFrame);
        }
        else if(hasAccess==0){
            JOptionPane.showMessageDialog(loginFrame, "LogIn incorrect\nMerci de réessayer ou de Sign Up", "LogIn Complet", JOptionPane.ERROR_MESSAGE);
            setHasAccess(2);
            DisplayStepHandler.setDisplay(0);
            Window.closeWindow(loginFrame);
        }

        else Window.displayWindow(loginFrame);


        // Action quand on clique le bouton log In
        logInButton2.addActionListener(event -> {

            // On récupère le mdp et cmdp pour comparer
            password = passwordField.getText().trim();
            pseudo = pseudoField.getText().trim();

            // On vérifie que les cases sont remplies
            if (password.equals("") || pseudoField.getText().equals("")) {
                JOptionPane.showMessageDialog(loginFrame, "Veuillez remplir toutes les cases", "Remplir", JOptionPane.WARNING_MESSAGE);
            } else {
                DisplayStepHandler.setDisplay(4);
                Window.closeWindow(loginFrame);
            }
        });

        // Action quand on clique le bouton retour
        retourButton.addActionListener(event -> {
            DisplayStepHandler.setDisplay(0);
            Window.closeWindow(loginFrame); // méthode pour afficher à nouveau le menuFrame de base
        });
    }
}
