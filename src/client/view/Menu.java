package client.view;
import client.model.Status;
import client.model.User;
import client.controller.UserController;
import server.dataAccesModule.DaoUser;

import javax.swing.*;
import java.awt.*;
import javax.swing.ImageIcon;


public class Menu{

    // Attributs
    public static JFrame menuFrame = new JFrame("Menu");
    static JButton loginButton, signUpButton;
    final int WIDTH = 800;
    final int HEIGHT = 500;


    public void display(){
        // Définitinon de la frame
        menuFrame.setSize(WIDTH, HEIGHT);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);

        // Définir l'image et le label du logo
        ImageIcon icon = new ImageIcon("logo.png");
        JLabel logoLabel = new JLabel(icon, JLabel.CENTER);

        // Définir le panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));

        // Définir les boutons
        loginButton = new JButton("Log In");
        loginButton.setPreferredSize(new Dimension(100, 30));


        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(100, 30));


        // Ajouter les boutons au frame
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        // Ajouter label et panel au frame
        menuFrame.setLayout(new BorderLayout());
        menuFrame.add(logoLabel, BorderLayout.CENTER);
        menuFrame.add(buttonPanel, BorderLayout.SOUTH);

        Window.displayWindow(menuFrame);

        //Listener sur les boutons login et sign up
        loginButton.addActionListener(event -> {
            DisplayStepHandler.setDisplay(1);
            Window.closeWindow(menuFrame);
        });

        signUpButton.addActionListener(event -> {
            DisplayStepHandler.setDisplay(2);
            Window.closeWindow(menuFrame);
        });


    }







}


