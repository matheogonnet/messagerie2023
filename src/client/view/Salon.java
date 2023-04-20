package client.view;
import client.model.Status;
import client.model.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Salon extends JFrame implements ActionListener{

    static JFrame salonFrame;
    static JButton sendButton;
    static JTextField messageBox;
    static JPanel convoPanel;
    User logUser;

    public Salon(User user){
        logUser = user;

        // Créer la fenêtre du Salon
        //lui donner une userlist
        JList<String> userList = new JList<>(new String[]{logUser.getPseudo(), "Matheo", "Kaito", "Arthur"});

        salonFrame = new JFrame("Salon");
        salonFrame.setSize(1200, 800);
        salonFrame.setLocationRelativeTo(null);
        salonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel de droite pour la conversation
        convoPanel = new JPanel();
        convoPanel.setLayout(new BoxLayout(convoPanel, BoxLayout.Y_AXIS));
        convoPanel.setBackground(Color.WHITE);
        convoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        convoPanel.setPreferredSize(new Dimension(1000, 720));
        JScrollPane convoPane = new JScrollPane(convoPanel);

        // Panel de gauche pour les utilisateurs
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBackground(Color.WHITE);
        userPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        userPanel.setPreferredSize(new Dimension(200, 800));

        // Ajout d'un label "Utilisateurs" en haut du panel
        JLabel usersLabel = new JLabel(" Utilisateurs :");
        userPanel.add(usersLabel, BorderLayout.NORTH);

        // Personnalisation de l'affichage des cellules de la JList userList
        userList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = setBackgroundForUserStatus(value, isSelected);
                return renderer;
            }
        });

        // Ajout d'une barre de défilement à la JList des utilisateurs
        JScrollPane userScroll = new JScrollPane(userList);

        // Ajout d'un écouteur d'événements à la liste des utilisateurs
        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {

                // Vérification du nombre de clics effectués par l'utilisateur : ici on veut un double clique
                if (event.getClickCount() == 2) {

                    // Récupération de l'utilisateur sélectionné
                    String selectedUser = userList.getSelectedValue();

                    // Vérification que l'utilisateur sélectionné n'est pas nul
                    if (selectedUser != null) {

                        // Création d'un JLabel pour afficher le nom de l'utilisateur sélectionné
                        JLabel usernameLabel = new JLabel(selectedUser);
                        usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));

                        // Création d'un JLabel pour afficher le grade de l'utilisateur sélectionné
                        JLabel gradeLabel = new JLabel("(Administrator)");
                        gradeLabel.setFont(new Font("Arial", Font.PLAIN, 12));

                        // Création d'un JPanel pour afficher les informations de l'utilisateur
                        JPanel userPanel = new JPanel(new GridLayout(3, 1));
                        userPanel.add(usernameLabel);
                        userPanel.add(gradeLabel);

                        // Création d'un JPanel pour afficher le statut de l'utilisateur
                        JPanel statusPanel = new JPanel(new FlowLayout());
                        JLabel currentStatusLabel = new JLabel("Current status: " + logUser.getStatus().toString());
                        JButton setStatusButton = new JButton("Set status");
                        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Online", "Offline", "Away"});
                        statusPanel.add(currentStatusLabel);
                        statusPanel.add(setStatusButton);
                        statusPanel.add(statusDropdown);

                        // Ajout d'un écouteur d'événements au bouton "Set status"
                        setStatusButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent event) {
                                // Récupération du statut sélectionné dans le menu déroulant
                                String selectedStatus = (String) statusDropdown.getSelectedItem();
                                // Convertir la chaîne en une valeur de l'enum Status
                                Status newStatus = null;
                                switch (selectedStatus) {
                                    case "Online":
                                        newStatus = Status.Online;
                                        break;
                                    case "Offline":
                                        newStatus = Status.Offline;
                                        break;
                                    case "Away":
                                        newStatus = Status.Away;
                                        break;
                                }

                                // Mettre à jour le statut de logUser
                                logUser.setStatus(newStatus);
                                currentStatusLabel.setText("Current status: " + logUser.getStatus().toString());
                                JOptionPane.showMessageDialog(null, "Status set to " + logUser.getStatus());
                            }
                        });

                        // Ajout du JPanel "statusPanel" au JPanel "userPanel"
                        userPanel.add(statusPanel);
                        // Ajout d'une bordure vide au JPanel "userPanel"
                        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

                        // Création de deux boutons pour "Ban" et "Upgrade" l'utilisateur sélectionné
                        JButton banButton = new JButton("Ban");
                        JButton upgradeButton = new JButton("Upgrade");
                        JPanel buttonPanel = new JPanel();
                        buttonPanel.add(banButton);
                        buttonPanel.add(upgradeButton);

                        // Ajout d'une bordure vide au JPanel "buttonPanel"
                        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));

                        // Création d'un JPanel principal pour afficher toutes les informations de l'utilisateur
                        JPanel mainPanel = new JPanel(new BorderLayout());
                        mainPanel.add(userPanel, BorderLayout.NORTH);
                        mainPanel.add(buttonPanel, BorderLayout.CENTER);

                        // Création d'une nouvelle fenêtre pour afficher les informations de l'utilisateur
                        JFrame userFrame = new JFrame(selectedUser);
                        userFrame.add(mainPanel);
                        userFrame.pack();
                        userFrame.setLocationRelativeTo(null); // centrer la fenêtre sur l'écran

                        // Déplacer la fenêtre un peu plus bas et un peu plus à droite
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        int x = (int) (screenSize.getWidth() * 0.225);
                        int y = (int) (screenSize.getHeight() * 0.104);
                        userFrame.setLocation(x, y);

                        userFrame.setVisible(true);
                    }
                }
            }
        });


        userPanel.add(userScroll, BorderLayout.CENTER);

        // Ajout du logo en bas de la fenetre
        ImageIcon icon = new ImageIcon("logo.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newImg);
        JLabel logoLabel = new JLabel(newIcon);
        userPanel.add(logoLabel, BorderLayout.SOUTH);


        // Panel de bas pour envoyer les messages
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(new Color(230, 230, 230, 210));
        messagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        messagePanel.setPreferredSize(new Dimension(1000, 80));

        // Rajouter les panels dans leur emplacement dans le frame
        salonFrame.add(convoPane, BorderLayout.CENTER);
        salonFrame.add(userPanel, BorderLayout.WEST);
        salonFrame.add(messagePanel, BorderLayout.SOUTH);

        // Rajouter le boite pour écrire le message et le boutton send
        messageBox = new JTextField(50);
        messageBox.setPreferredSize(new Dimension(400, 30));
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        JButton reportButton = new JButton("Reporting");
        reportButton.setPreferredSize(new Dimension(100, 30));
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(salonFrame, "Vous n'avez pas les droits");
            }
        });

        messagePanel.add(messageBox);
        messagePanel.add(sendButton);
        messagePanel.add(reportButton);

        salonFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event2) {
        // List des messages, c'est la conversation
        List<String> conversation = new ArrayList<String>();

        // On récupère le texte que l'on veut envoyer
        String message = messageBox.getText();

        // On vérifie si le message est bon, on le stock dans la conversation et on l'affiche
        if (!message.equals("") && (message.length() < 500)) {
            conversation.add(message);

            printConversation(conversation, convoPanel);

        } else if (message.equals("")){
            JOptionPane.showMessageDialog(salonFrame, "Veuillez écrire un message", "Erreur", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(salonFrame, "Veuillez modifier votre message", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper function to get the status for a given user (replace with your actual logic)
    private String getStatusForUser(String user) {
        return "Online";
    }
    public void printConversation(List<String> conversation, JPanel convoPanel) {
        // On chope le nombre de messages dans la conversation
        int numMessages = conversation.size();

        // On crée l'emplacement du pseudo
        JLabel usernameLabel = new JLabel(logUser.getPseudo() + " : ");
        usernameLabel.setForeground(Color.LIGHT_GRAY);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 3, 3));

        for (int i = 0; i < numMessages; i++) {
            // On récupère le message que l'on veut afficher de la conversation
            String message = conversation.get(i);

            JLabel messageLabel = new JLabel(message);
            messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 3));

            // Et on affiche le pseudo + message
            convoPanel.add(usernameLabel);
            convoPanel.add(messageLabel);

            usernameLabel = new JLabel();
        }

        // On réactualise la page pour chaque message envoyé
        convoPanel.revalidate();
        convoPanel.repaint();
        messageBox.setText("");
    }

    private JLabel setBackgroundForUserStatus(Object value, boolean isSelected) {
        JLabel renderer = new JLabel(value.toString());
        renderer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Modifier la couleur de fond de chaque user en fonction de leur statut
        Status status = logUser.getStatus();
        if (status == Status.Online) {
            renderer.setBackground(new Color(200, 255, 200)); // Vert : Online
        } else if (status == Status.Away) {
            renderer.setBackground(new Color(255, 220, 150)); // Orange : Away
        } else {
            renderer.setBackground(new Color(255, 200, 200)); // Rouge : Offline
        }

        // Mettre en gras le prénom du user connecté sur la fenetre parmis les autres : le premier de la liste users
        if (value.equals(logUser.getPseudo())) {
            renderer.setFont(renderer.getFont().deriveFont(Font.BOLD));
        }

        renderer.setOpaque(true);
        renderer.setForeground(isSelected ? Color.WHITE : Color.BLACK);

        return renderer;
    }

}