package client.view;
import client.model.Status;
import client.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;

public class Menu extends JFrame implements ActionListener {

    // Attributs
    public static JFrame menuFrame;
    static JButton loginButton, signUpButton;
    static JLabel logoLabel;
    User logUser;
    public String fname, lname, mail, mdp, cmdp;
    boolean login = false;

    // Getter & Setter
    public JFrame getMenuFrame() {
        return menuFrame;
    }
    public void setLogin(boolean login) {
        this.login = login;
    }
    public boolean isLogin() {
        return login;
    }


    public Menu(User user){
        user.setStatus(Status.Online);

        // Initaliser la page du menu
        this.logUser = user;
        final int WIDTH = 800;
        final int HEIGHT = 500;
        // Définissez le frame
        menuFrame = new JFrame("SWIFT CHAT - Menu");
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
        loginButton.addActionListener(this);

        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(100, 30));
        signUpButton.addActionListener(this);

        // Ajouter les boutons au frame
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        // Ajouter label et panel au frame
        menuFrame.setLayout(new BorderLayout());
        menuFrame.add(logoLabel, BorderLayout.CENTER);
        menuFrame.add(buttonPanel, BorderLayout.SOUTH);

        menuFrame.setVisible(true);
    }

    // Reconnaître les cliques des boutons logIn et signUp de la page du menu
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == loginButton){
            // Appeler la fonction afficherLogin
            afficherLogIn();
        } else if(event.getSource() == signUpButton){
            afficherSignUp();
        }
    }

    public void afficherLogIn(){

        // On enlève les composants du menuFrame et on le renomme pour modéliser la page de LogIn
        menuFrame.setTitle("Log In");
        menuFrame.getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Créer le panel pour le Log In
        JPanel logInPanel = new JPanel(new GridBagLayout());
        logInPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 0, 30));

        // Ajouter les labels et les champs de texte pour le Log In
        JLabel labelPseudo2 = new JLabel("Pseudo :");
        JTextField pseudoField2 = new JTextField(15);

        JLabel labelMDP2 = new JLabel("Mot de Passe :");
        JPasswordField passwordField2 = new JPasswordField(15);

        JLabel labelConfirmMDP2 = new JLabel("Confirmer le MDP :");
        JPasswordField confirmPasswordField2 = new JPasswordField(15);

        // Ajouter les éléments au panel avec des contraintes pour les centrer
        // On utilise une grille GBC pour bien pouvoir placer les éléments
        GridBagConstraints grid = new GridBagConstraints();
        grid.insets = new Insets(15, 5, 15, 5); // add vertical spacing
        grid.gridx = 0;
        grid.gridy = 0;
        grid.anchor = GridBagConstraints.CENTER;
        logInPanel.add(labelPseudo2, grid);
        grid.gridx = 1;
        logInPanel.add(pseudoField2, grid);

        grid.gridx = 0;
        grid.gridy = 1;
        logInPanel.add(labelMDP2, grid);
        grid.gridx = 1;
        logInPanel.add(passwordField2, grid);

        grid.gridx = 0;
        grid.gridy = 2;
        logInPanel.add(labelConfirmMDP2, grid);
        grid.gridx = 1;
        logInPanel.add(confirmPasswordField2, grid);

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
        menuFrame.add(mainPanel);
        menuFrame.pack();
        menuFrame.setLocationRelativeTo(null); // center the frame
        menuFrame.setVisible(true);

        // Action quand on clique le bouton log In
        logInButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent logIn2) {
                // On récupère le mdp et cmdp pour comparer
                logUser.setPassword(passwordField2.getText().trim());
                String confirmPassword = new String(confirmPasswordField2.getPassword());
                logUser.setPseudo(pseudoField2.getText());

                // On compare les mdp, et on vérifie que les cases sont remplies
                if (!logUser.getPassword().equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(menuFrame, "Les deux mots de passe ne sont pas identiques", "Mismatch", JOptionPane.ERROR_MESSAGE);
                } else if (logUser.getPassword().equals("") || pseudoField2.getText().equals("")) {
                    JOptionPane.showMessageDialog(menuFrame, "Veuillez remplir toutes les cases", "Remplir", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(menuFrame, "LogIn complet", "LogIn Complet", JOptionPane.INFORMATION_MESSAGE);
                    setLogin(true);
                    menuFrame.dispose();
                    ouvrirSalon();
                }
            }
        });

        // Action quand on clique le bouton retour
        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent retour) {
                menuFrame.dispose();
                Menu menu = new Menu(logUser); // méthode pour afficher à nouveau le menuFrame de base
            }
        });
    }

    public void afficherSignUp() {

        // Même principe que pour la page log in
        menuFrame.setTitle("Sign Up");
        menuFrame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        setContentPane(panel);

        // Définir le panel pour le Sign Up
        JPanel signUpPanel1 = new JPanel(new BorderLayout(30, 30));
        signUpPanel1.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        menuFrame.add(signUpPanel1);

        // Ajouter les labels et les champs de texte pour le Sign Up
        JLabel labelPseudo = new JLabel("Pseudo :");
        JTextField pseudoField = new JTextField();

        JLabel labelFirstName = new JLabel("Prénom :");
        JTextField firstNameField = new JTextField();

        JLabel labelLastName = new JLabel("Nom :");
        JTextField lastNameField = new JTextField();

        JLabel labelMail = new JLabel("E-mail :");
        JTextField mailField = new JTextField();

        JLabel labelMDP = new JLabel("Mot de Passe :");
        JPasswordField passwordField = new JPasswordField();

        JLabel labelConfirmMDP = new JLabel("Confirmer votre MDP :");
        JPasswordField confirmPasswordField = new JPasswordField();

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

        menuFrame.setVisible(true);

        signUpButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent signUP2) {
                // Même principe que pour les cases du log in, on vérifie pour chaque élément
                logUser.setPseudo(pseudoField.getText().trim());
                fname = firstNameField.getText().trim();
                lname = lastNameField.getText().trim();
                mail = mailField.getText().trim();
                mdp = passwordField.getText().trim();
                cmdp = confirmPasswordField.getText().trim();

                if(logUser.getPseudo().equals("") || fname.equals("") || lname.equals("") || mail.equals("") || mdp.equals("") || cmdp.equals("")){
                    JOptionPane.showMessageDialog(menuFrame, "Veuillez remplir toutes les cases.", "Sign Up incomplet", JOptionPane.ERROR_MESSAGE);
                } else if(!mdp.equals(cmdp)){
                    JOptionPane.showMessageDialog(menuFrame, "Les mots de passe ne sont pas identiques.", "Sign Up incomplet", JOptionPane.WARNING_MESSAGE);
                } else{
                    JOptionPane.showMessageDialog(menuFrame, "Bravo, le Sign Up est complet.", "Sign Up complet", JOptionPane.INFORMATION_MESSAGE);
                    menuFrame.dispose();
                    Menu menu = new Menu(logUser);
                }
            }
        });

        retourButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent retour) {
                menuFrame.dispose();
                Menu menu = new Menu(logUser); // méthode pour afficher à nouveau le menuFrame de base
            }
        });
    }

    public void ouvrirSalon() {
        Salon salon = new Salon(logUser);
    }
}


