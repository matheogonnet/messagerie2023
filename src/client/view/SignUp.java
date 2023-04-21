package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SignUp {

    private JFrame signUpFrame = new JFrame("Sign Up");
    private final int WIDTH = 500;
    private final int HEIGHT = 300;
    private String pseudo;
    private String firstName;
    private String lastName;
    private String password;

    public String getPseudo() {
        return pseudo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void display() {

        signUpFrame.setSize(WIDTH, HEIGHT);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        signUpFrame.setContentPane(panel);

        // Définir le panel pour le Sign Up
        JPanel signUpPanel1 = new JPanel(new BorderLayout(30, 30));
        signUpPanel1.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        signUpFrame.add(signUpPanel1);

        // Ajouter les labels et les champs de texte pour le Sign Up
        JLabel labelPseudo = new JLabel("Pseudo :");
        JTextField pseudoField = new JTextField("");

        JLabel labelFirstName = new JLabel("Prénom :");
        JTextField firstNameField = new JTextField("");

        JLabel labelLastName = new JLabel("Nom :");
        JTextField lastNameField = new JTextField("");

        JLabel labelMail = new JLabel("E-mail :");
        JTextField mailField = new JTextField("");

        JLabel labelMDP = new JLabel("Mot de Passe :");
        JPasswordField passwordField = new JPasswordField("");

        JLabel labelConfirmMDP = new JLabel("Confirmer votre MDP :");
        JPasswordField confirmPasswordField = new JPasswordField("");

        // Ajouter nom des info + boite de texte au deuxième panel
        JPanel signUpPanel2 = new JPanel(new GridLayout(0, 2));

        signUpPanel2.add(labelPseudo);
        signUpPanel2.add(pseudoField);

        signUpPanel2.add(labelFirstName);
        signUpPanel2.add(firstNameField);

        signUpPanel2.add(labelLastName);
        signUpPanel2.add(lastNameField);

        signUpPanel2.add(labelMail);
        signUpPanel2.add(mailField);

        signUpPanel2.add(labelMDP);
        signUpPanel2.add(passwordField);

        signUpPanel2.add(labelConfirmMDP);
        signUpPanel2.add(confirmPasswordField);

        signUpPanel1.add(signUpPanel2, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton signUpButton2 = new JButton("Sign Up");
        signUpButton2.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(signUpButton2);
        JButton retourButton = new JButton("Retour");
        retourButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(retourButton);

        signUpPanel1.add(buttonPanel, BorderLayout.SOUTH);

        Window.displayWindow(signUpFrame);

        signUpButton2.addActionListener(event -> {
            // Même principe que pour les cases du log in, on vérifie pour chaque élément
            pseudo = pseudoField.getText().trim();
            firstName = firstNameField.getText().trim();
            lastName = lastNameField.getText().trim();
            String mail = mailField.getText().trim();
            password = passwordField.getText().trim();
            String confirmedPassword = confirmPasswordField.getText().trim();

            if(pseudo.equals("") || firstName.equals("") || lastName.equals("") || mail.equals("") || password.equals("") || confirmedPassword.equals("")){
                JOptionPane.showMessageDialog(signUpFrame, "Veuillez remplir toutes les cases.", "Sign Up incomplet", JOptionPane.ERROR_MESSAGE);
            } else if(!password.equals(confirmedPassword)){
                JOptionPane.showMessageDialog(signUpFrame, "Les mots de passe ne sont pas identiques.", "Sign Up incomplet", JOptionPane.WARNING_MESSAGE);
            } else{
                JOptionPane.showMessageDialog(signUpFrame, "Le Sign Up est complet.", "Sign Up complet", JOptionPane.INFORMATION_MESSAGE);
                DisplayStepHandler.setDisplay(5);
                Window.closeWindow(signUpFrame);
            }
        });

        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent retour) {
                DisplayStepHandler.setDisplay(0);
                Window.closeWindow(signUpFrame);
            }
        });
    }
}
