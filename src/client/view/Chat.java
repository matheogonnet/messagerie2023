package client.view;

import client.model.*;

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
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe qui gère l'affichage de la fenêtre de chat.
 */
public class Chat {

    // Attributs
    static JFrame chatFrame;
    static JButton sendButton;
    static JTextField messageBox;
    static JPanel convoPanel;
    User logUser;

    /**
     * Affiche la fenêtre de chat.
     *
     * @param client          Le client qui utilise l'application : il envoit les messages au server et les réceptionne pour actualiser la BDD
     * @param conversation    La conversation en cours.
     * @param listUsers       La liste des utilisateurs connectés.
     */
    public void display(Client client, Conversation conversation, List<User> listUsers) {
        logUser = client.getUser();
        // Créer la JList avec le tableau de pseudos
        JList<String> userList = new JList<>();
        updateUserList(listUsers,userList);


        //fentre salon
        chatFrame = new JFrame("Chat :"+client.getUser().getPseudo());
        chatFrame.setSize(1200, 800);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        // Ajout d'une barre de défilement à la JList des utilisateurs
        JScrollPane userScroll = new JScrollPane(userList);
        userPanel.add(userScroll, BorderLayout.CENTER);



        // Créer une instance de la classe Timer
        Timer timer = new Timer(1000, event -> {
            logUser = client.getUser();
            updateUserList(listUsers,userList);
            for (User user : listUsers){
                if (user.getPseudo().equals(logUser.getPseudo())){
                    logUser = user;
                }
            }
            if(logUser.isBan()){
                DisplayStepHandler.setDisplay(4);
                Window.closeWindow(chatFrame);
            } else {

                // Mettre à jour les panneaux de conversation et d'utilisateurs
                printConversation(conversation.getConversation(), convoPanel, convoPane);
                // Personnalisation de l'affichage des cellules de la JList userList
                userList.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        return setBackgroundForUserStatus(value, isSelected,listUsers);}});

                userList.revalidate();
                userList.repaint(); // revoir cette partie
            }
        });

        // Démarrer le timer
        timer.start();

        // Arrêter le timer lorsque la fenêtre de salon est fermée
        chatFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
                client.send("USER_REQUEST::log_out::" + logUser.getPseudo());
                client.send("/disconnected");
            }
        });


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
        chatFrame.add(convoPane, BorderLayout.CENTER);
        chatFrame.add(userPanel, BorderLayout.WEST);
        chatFrame.add(messagePanel, BorderLayout.SOUTH);

        // Rajouter le boite pour écrire le message et le boutton send
        messageBox = new JTextField(50);
        messageBox.setPreferredSize(new Dimension(400, 30));
        sendButton = new JButton("Send");
        sendButton.addActionListener(event -> {
            try {
                envoyerMessageFinal(messageBox, convoPanel, chatFrame, logUser.getPseudo(), client, conversation);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        JButton reportButton = new JButton("Reporting");
        reportButton.setPreferredSize(new Dimension(100, 30));
        reportButton.addActionListener(event -> {
            if (logUser.getGrade()== Grades.Moderator ||logUser.getGrade() ==Grades.Classic){
                JOptionPane.showMessageDialog(chatFrame, "Vous n'avez pas les droits");
            }
            else if (logUser.getGrade()==Grades.Administrator){
                client.send("USER_REQUEST::reporting");
            }
        });


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
                        JLabel usernameLabel = new JLabel("  "+selectedUser);
                        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 20));

                        // Récupération de l'utilisateur correspondant à l'utilisateur sélectionné
                        User selectedUserObj = null;
                        for (User user : listUsers) {
                            if (user.getPseudo().equals(selectedUser)) {
                                selectedUserObj = user;
                                break;
                            }
                        }

                        // Création d'un JLabel pour afficher le grade de l'utilisateur sélectionné
                        JLabel gradeLabel = new JLabel("Grade : " + selectedUserObj.getGrade());
                        gradeLabel.setFont(new Font("Verdana", Font.PLAIN, 15));

                        // Création d'un JPanel pour afficher les informations de l'utilisateur
                        JPanel userPanel = new JPanel(new GridLayout(3, 1));
                        userPanel.add(usernameLabel);
                        userPanel.add(gradeLabel);

                        // Création d'un JPanel pour afficher le statut de l'utilisateur
                        JPanel statusPanel = new JPanel(new FlowLayout());
                        JLabel currentStatusLabel = new JLabel("Current status: " + selectedUserObj.getStatus().toString());
                        JComboBox<String> statusDropdown = new JComboBox<>(new String[]{"Online","Offline","Away"});
                        statusDropdown.setSelectedItem(selectedUserObj.getStatus());

                        if (selectedUser.equals(logUser.getPseudo())) {
                            // Si l'utilisateur sélectionné est l'utilisateur connecté, on ajoute un bouton pour modifier son statut
                            JButton setStatusButton = new JButton("Set status");
                            statusPanel.add(currentStatusLabel);
                            statusPanel.add(setStatusButton);
                            statusPanel.add(statusDropdown);

                            // Ajout d'un écouteur d'événements au bouton "Set status"
                            setStatusButton.addActionListener(event1 -> {
                                // Récupération du statut sélectionné dans le menu déroulant
                                String selectedStatus = (String) statusDropdown.getSelectedItem();
                                assert selectedStatus != null;
                                for(User user : listUsers){
                                    if (user.getPseudo().equals(logUser.getPseudo())){
                                        user.setStatus(selectedStatus);
                                        logUser.setStatus(selectedStatus);
                                    }
                                }

                                client.send("USER_REQUEST::setStatus::"+selectedStatus+"::"+logUser.getPseudo());
                                currentStatusLabel.setText("Current status: " + logUser.getStatus().toString());
                                JOptionPane.showMessageDialog(null, "Status set to " + logUser.getStatus().toString());
                            });
                        } else {

                            // Si l'utilisateur sélectionné n'est pas l'utilisateur connecté, on ajoute des boutons pour le bannir ou le promouvoir
                            JButton banButton = new JButton("Ban");
                            JButton upgradeButton = new JButton("Upgrade");
                            statusPanel.add(currentStatusLabel);
                            statusPanel.add(banButton);
                            statusPanel.add(upgradeButton);

                            // Ajout d'un écouteur d'événements au bouton "Ban"
                            banButton.addActionListener(event1 -> {
                                // Récupération de l'utilisateur correspondant à l'utilisateur sélectionné
                                User userToBan = null;
                                for (User user : listUsers) {
                                    if (user.getPseudo().equals(selectedUser)) {
                                        userToBan = user;
                                        break;
                                    }
                                }
                                if (logUser.getGrade()==Grades.Administrator){
                                    if (userToBan.getGrade()==Grades.Administrator){
                                        JOptionPane.showMessageDialog(null, "vous ne pouvez pas bannir un administrateur.");
                                    }
                                    else { // Bannir l'utilisateur sélectionné
                                        logUser.ban(userToBan);
                                        client.send("USER_REQUEST::ban_user::" + userToBan.getPseudo());
                                        JOptionPane.showMessageDialog(null, selectedUser + " a été banni.");
                                    }
                                } else if (logUser.getGrade()==Grades.Moderator && userToBan.getGrade()==Grades.Classic) {
                                    // Bannir l'utilisateur sélectionné
                                    logUser.ban(userToBan);
                                    client.send("USER_REQUEST::ban_user::"+userToBan.getPseudo());
                                    JOptionPane.showMessageDialog(null, selectedUser + " a été banni.");
                                }
                                else {
                                    JOptionPane.showMessageDialog(chatFrame, "Vous n'avez pas les droits");
                                }

                            });

                            // Ajout d'un écouteur d'événements au bouton "Upgrade"
                            upgradeButton.addActionListener(event1 -> {
                                /// Récupération de l'utilisateur correspondant à l'utilisateur sélectionné
                                User userToUpgrade = null;
                                for (User user : listUsers) {
                                    if (user.getPseudo().equals(selectedUser)) {
                                        userToUpgrade = user;
                                        break;
                                    }
                                }
                                if (logUser.getGrade()==Grades.Administrator&& userToUpgrade.getGrade()==Grades.Moderator){
                                    // Upgrade l'utilisateur sélectionné
                                    client.send("USER_REQUEST::upgrade_user::"+userToUpgrade.getPseudo()+"::Administrator");
                                    userToUpgrade.setGrade(Grades.Administrator);
                                    JOptionPane.showMessageDialog(null, selectedUser + " est passé : Administrateur");
                                } else if (logUser.getGrade()==Grades.Administrator && userToUpgrade.getGrade()==Grades.Classic) {
                                    // Upgrade l'utilisateur sélectionné
                                    client.send("USER_REQUEST::upgrade_user::"+userToUpgrade.getPseudo()+"::Moderator");
                                    userToUpgrade.setGrade(Grades.Moderator);
                                    JOptionPane.showMessageDialog(null, selectedUser + " est passé : Modérateur");}
                                else {
                                    JOptionPane.showMessageDialog(chatFrame, "Vous n'avez pas les droits");
                                }
                            });
                        }

                        // Ajout du JPanel "statusPanel" au JPanel "userPanel"
                        userPanel.add(statusPanel);
                        // Ajout d'une bordure vide au JPanel "userPanel"
                        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

                        // Création d'un JPanel principal pour afficher toutes les informations de l'utilisateur
                        JPanel mainPanel = new JPanel(new BorderLayout());
                        mainPanel.add(userPanel, BorderLayout.NORTH);
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

                    }
                }
            }
        });


        //envoyer le message aussi quand la key enter est pressée
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent enter) {
                if (enter.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        envoyerMessageFinal(messageBox, convoPanel, chatFrame, logUser.getPseudo(),client,conversation);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        messagePanel.add(messageBox);
        messagePanel.add(sendButton);
        messagePanel.add(reportButton);

        chatFrame.setVisible(true);
    }





    /**
     * Modèle de la bulle de message dans la conversation
     */
    public static class MessageBubble extends JPanel {
        private JLabel usernameLabel;
        private JLabel messageLabel;
        private JLabel timeLabel;

        /**
         * Constructeur pour créer une bulle de message
         * @param author l'auteur du message
         * @param message le contenu du message
         * @param timestamp l'horodatage du message
         */
        public MessageBubble(String author, String message, String timestamp, User user) {
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
            timeLabel.setText(timestamp);

            // Création bulle de message
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            add(usernameLabel);
            add(messageLabel);
            add(timeLabel);

            setOpaque(true);
            if (author.equals(user.getPseudo())) {
                setBackground(new Color(155, 155, 155, 255));
            } else {
                setBackground(new Color(67, 192, 0, 255));
            }

            setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 10));
        }
    }

    /**
     * Méthode pour vérifier si le nouveau message est valide
     *
     * @param messageBox champ de saisie de message
     * @param convoPanel panel de la conversation
     * @param salonFrame frame de la conversation
     * @param author auteur du message
     * @param client instance du client
     * @param conversation instance de la conversation
     * @throws SQLException si une erreur se produit lors de l'envoi du message
     */    public void envoyerMessageFinal(JTextField messageBox, JPanel convoPanel, JFrame salonFrame, String author, Client client, Conversation conversation) throws SQLException {
        String message = messageBox.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date now = new Date();

        if (!message.equals("") && (message.length() < 500)) {
            client.send("MESSAGE::sendMessage::"+logUser.getPseudo()+"::"+sdf.format(now)+"::"+message);
            Message newMessage = new Message(logUser.getPseudo(), sdf.format(now),message);
            conversation.getConversation().add(newMessage);
            messageBox.setText(""); // Réinitialisation du champ de saisie de message

        } else if (message.equals("")) {
            JOptionPane.showMessageDialog(salonFrame, "Veuillez écrire un message", "Erreur", JOptionPane.ERROR_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(salonFrame, "Veuillez modifier votre message", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }



    /**
     * Méthode pour afficher la conversation sur le panel au moment du login
     *
     * @param conversation liste des messages de la conversation
     * @param convoPanel panel de la conversation
     */
    public void printConversation(ArrayList<Message> conversation, JPanel convoPanel, JScrollPane scrollPane) {

        convoPanel.removeAll();
        // On chope le nombre de messages dans la conversation
        int numMessages = conversation.size();

        int height = 0;

        convoPanel.setLayout(new BoxLayout(convoPanel, BoxLayout.Y_AXIS));
        convoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // On prend le dernier message écrit
        for (int i = 0; i < numMessages; i++) {

            // On récupère le message que l'on veut afficher de la conversation
            Message message = conversation.get(i);
            String content = message.getContent();
            String author = message.getAuthor();
            String timeStamp = message.getTimeStamp();
            MessageBubble messageBubble = new MessageBubble(author, content, timeStamp,logUser);
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

        // On défile vers le bas pour afficher le dernier message
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        Rectangle rect = vertical.getVisibleRect();
        rect.y = vertical.getMaximum() - rect.height;
        vertical.setValue(vertical.getMaximum());
        scrollPane.scrollRectToVisible(rect);

    }

    /**
     * Méthode pour définir la couleur d'arrière-plan en fonction du statut de l'utilisateur
     *
     * @param value la valeur de la cellule
     * @param isSelected si la cellule est sélectionnée
     * @param listUsers liste des utilisateurs connectés
     * @return JLabel avec la couleur d'arrière-plan appropriée
     */
    private JLabel setBackgroundForUserStatus(Object value, boolean isSelected, List<User> listUsers) {
        JLabel renderer = new JLabel(value.toString());
        renderer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Parcourir la liste des utilisateurs pour obtenir le statut de chaque utilisateur
        for (User user : listUsers) {
            if (value.equals(user.getPseudo())) {
                Status status = user.getStatus();

                // Modifier la couleur de fond de chaque utilisateur en fonction de leur statut
                if (status == Status.Online) {
                    renderer.setBackground(new Color(200, 255, 200)); // Vert : Online
                } else if (status == Status.Away) {
                    renderer.setBackground(new Color(255, 220, 150)); // Orange : Away
                } else {
                    renderer.setBackground(new Color(255, 200, 200)); // Rouge : Offline
                }

                if (value.equals(logUser.getPseudo())) {
                    renderer.setFont(renderer.getFont().deriveFont(Font.BOLD));
                }

                renderer.setOpaque(true);
                renderer.setForeground(isSelected ? Color.WHITE : Color.BLACK);

                return renderer;
            }
        }
        return renderer;
    }


    /**
     * Méthode pour update les utilisateurs dans le pannel
     * @param userList Jlist des users pour la view
     * @param listUsers liste des utilisateurs connectés
     */
    public void updateUserList(List<User> listUsers, JList<String> userList) {
        String[] pseudos = new String[listUsers.size()];
        // Remplir le tableau de pseudos avec les pseudos des utilisateurs
        for (int i = 0; i < listUsers.size(); i++) {
            pseudos[i] = listUsers.get(i).getPseudo();
        }
        // Mettre à jour la JList avec la nouvelle liste d'utilisateurs
        userList.setListData(pseudos);

    }
}