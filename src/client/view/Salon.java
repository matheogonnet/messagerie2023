package client.view;
import client.controller.MessageController;

import client.controller.Reporting;
import client.controller.UserController;
import client.model.*;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.sql.SQLException;

// Temps
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Salon{

    static JFrame salonFrame;
    static JButton sendButton;
    static JTextField messageBox;
    static JPanel convoPanel;
    User logUser;
    Conversation conversation = new Conversation("Salon");
    MessageController messageController;


    public void display(User user, DaoUser userDao, DaoMessage messageDao, UserController userController) throws SQLException {
        conversation.setConversation(messageDao.findAll());
        logUser = user;
        messageController = new MessageController(logUser,messageDao,conversation);
        List<User> listUsers = userDao.findAll();
        String[] pseudos = new String[listUsers.size()];

        // Remplir le tableau de pseudos avec les pseudos des utilisateurs
        for (int i = 0; i < listUsers.size(); i++) {
            pseudos[i] = listUsers.get(i).getPseudo();
        }
        // Créer la JList avec le tableau de pseudos
        JList<String> userList = new JList<>(pseudos);

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
        printConversation(conversation.getConversation(), convoPanel);

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
                    if (selectedUser.equals(logUser.getPseudo())) {

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
                        Window.displayWindow(userFrame);


                        // Ajout d'un écouteur d'événements au bouton "Set status"
                        setStatusButton.addActionListener(event1 -> {
                            // Récupération du statut sélectionné dans le menu déroulant
                            String selectedStatus = (String) statusDropdown.getSelectedItem();
                            /*try {
                                assert selectedStatus != null;
                                controller.setStatus(selectedStatus);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }*/
                            currentStatusLabel.setText("Current status: " + logUser.getStatus().toString());
                            JOptionPane.showMessageDialog(null, "Status set to " + logUser.getStatus());
                        });

                        banButton.addActionListener(event2 ->{
                            JOptionPane.showMessageDialog(salonFrame, "Vous n'avez pas les droits");
                        });
                    }
                }
            }
        });


        userPanel.add(userScroll, BorderLayout.CENTER);

        // Ajout du logo en bas de la fenetre
        ImageIcon icon = new ImageIcon("logo.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
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
        sendButton.addActionListener(event -> {
            try {
                envoyerMessageFinal(messageBox, convoPanel, salonFrame, logUser.getPseudo());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        JButton reportButton = new JButton("Reporting");
        reportButton.setPreferredSize(new Dimension(100, 30));
        reportButton.addActionListener(event -> {
            if (logUser.getGrade()== Grades.Moderator ||logUser.getGrade() ==Grades.Classic){
                JOptionPane.showMessageDialog(salonFrame, "Vous n'avez pas les droits");
            }
            else if (logUser.getGrade()==Grades.Administrator){
                Reporting reporting = new Reporting(listUsers);
                try {
                    userController.reportingUser(reporting);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent enter) {
                if (enter.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        actionPerformed(null);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        messagePanel.add(messageBox);
        messagePanel.add(sendButton);
        messagePanel.add(reportButton);

        salonFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event2) throws SQLException {
        envoyerMessageFinal(messageBox, convoPanel, salonFrame, logUser.getPseudo());
    }
    // Template pour la bulle de message
    public static class MessageBubble extends JPanel {
        private JLabel usernameLabel;
        private JLabel messageLabel;
        private JLabel timeLabel;

        public MessageBubble(String author, String message) {
            // Définition boite username + message
            usernameLabel = new JLabel(author);
            usernameLabel.setForeground(Color.WHITE);
            usernameLabel.setFont(usernameLabel.getFont().deriveFont(Font.BOLD));
            usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

            messageLabel = new JLabel(message);
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            timeLabel = new JLabel();
            timeLabel.setForeground(Color.BLACK);
            int a = message.length() * 7;
            int b = author.length() * 7;
            // On décale l'heure à droite en fonction de la longueur du username ou message
            if (a < b) {
                timeLabel.setBorder(BorderFactory.createEmptyBorder(0, b, 5, 0));
            } else {
                timeLabel.setBorder(BorderFactory.createEmptyBorder(0, a, 5, 0));
            }
            timeLabel.setHorizontalAlignment(JLabel.RIGHT);
            updateTimeLabel();

            // Création bulle de message
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(usernameLabel);
            add(messageLabel);
            add(timeLabel);

            setOpaque(true);
            setBackground(new Color(0, 192, 0));

            setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 10));
        }

        private void updateTimeLabel() {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date now = new Date();
            timeLabel.setText(sdf.format(now));
        }
    }

    // Méthode pour vérifier si le nouveau est message est valide, puis l'affiche
    public void envoyerMessageFinal(JTextField messageBox, JPanel convoPanel, JFrame salonFrame, String author) throws SQLException {
        String message = messageBox.getText();

        if (!message.equals("") && (message.length() < 500)) {
            //on ajoute le message à la BDD
            messageController.send(message);

            //On affiche le dernier message
            printLastMessage(message, convoPanel, author);

        } else if (message.equals("")) {
            JOptionPane.showMessageDialog(salonFrame, "Veuillez écrire un message", "Erreur", JOptionPane.ERROR_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(salonFrame, "Veuillez modifier votre message", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour envoyer le nouveau message
    public void printLastMessage(String sNewMessage, JPanel convoPanel, String author){
        int height = 0;

        convoPanel.setLayout(new BoxLayout(convoPanel, BoxLayout.Y_AXIS));
        convoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // On prend le dernier message écrit

        MessageBubble newMessageBubble = new MessageBubble(author, sNewMessage);
        convoPanel.add(newMessageBubble);

        height = newMessageBubble.getPreferredSize().height;

        convoPanel.add(Box.createVerticalStrut(10));

        int numComponents = convoPanel.getComponentCount()/2; // numComponents testé avec un println et il vaut toujours le double du nombre de messages

        // Trouvé après essais successifs, défini la taille de l'ecran en fonction du nombre de messages envoyés
        int convoPanelHeight = numComponents * (height + 10) + 20;
        convoPanel.setPreferredSize(new Dimension(convoPanel.getWidth(), convoPanelHeight));

        // On réactualise la page pour chaque message envoyé
        convoPanel.revalidate();
        convoPanel.repaint();
        messageBox.setText("");
    }

    // Méthode pour imprimer toute la conversation au moment du logIn
     public void printConversation(ArrayList<Message> conversation, JPanel convoPanel) {

        // On chope le nombre de messages dans la conversation
         int numMessages = conversation.size();

         int height = 0;

         convoPanel.setLayout(new BoxLayout(convoPanel, BoxLayout.Y_AXIS));
         convoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

         // On prend le dernier message écrit
         for (int i = 0; i < numMessages; i++) {
             // On récupère le message que l'on veut afficher de la conversation
             Message message = conversation.get(i);

             String sMessage = message.getContent();
             String author = message.getAuthor();

             MessageBubble messageBubble = new MessageBubble(author, sMessage);
             convoPanel.add(messageBubble);

             height = messageBubble.getPreferredSize().height;

             convoPanel.add(Box.createVerticalStrut(10));
         }

         int numComponents = convoPanel.getComponentCount()/2; // numComponents testé avec un println et il vaut toujours le double du nombre de messages

         // Trouvé après essais successifs, défini la taille de l'ecran en fonction du nombre de messages envoyés
         int convoPanelHeight = numComponents * (height + 10) + 20;
         convoPanel.setPreferredSize(new Dimension(convoPanel.getWidth(), convoPanelHeight));

         // On réactualise la page pour chaque message envoyé
        convoPanel.revalidate();
        convoPanel.repaint();
        //messageBox.setText("");
    }

    // Permet d'actualiser le fond des utilisateurs en fonction de leur status
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