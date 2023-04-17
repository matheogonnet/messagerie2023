package client.controller;

import client.model.Conversation;
import client.model.Message;
import client.model.User;
import server.dataAccesModule.DaoMessage;

import java.sql.SQLException;


public class MessageController {

    private User user;
    private DaoMessage daoMessage;
    private Conversation conversation;

    public MessageController(User user, DaoMessage daoMessage, Conversation conversation){
        this.user = user;
        this.daoMessage = daoMessage;
        this.conversation = conversation;
    }

    public void send(String content) throws SQLException {
        Message message = user.write_message(content);
        conversation.addMessage(message);
        daoMessage.add(message);
        daoMessage.update(message);
    }

    public void editMessage(int messageId, String newContent) throws SQLException {
        // Récupération du message à modifier à partir de son identifiant
        Message messageToEdit = conversation.getMessageById(messageId); //return un type : null ou Message

        if (messageToEdit == null) {
            //faire l'affichage graphique
            throw new IllegalArgumentException("Le message spécifié n'existe pas dans la conversation");
        }

        // Vérification que l'utilisateur courant est l'auteur du message
        if (!messageToEdit.getAuthor().equals(user.getPseudo())) {
            //faire l'affichage graphique
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier ce message");
        }

        // Suppression de l'ancien message
        int index = conversation.getConversation().indexOf(messageToEdit);
        conversation.getConversation().remove(messageToEdit);
        daoMessage.delete(messageToEdit);

        // Modification du contenu du message
        messageToEdit.setContent(newContent);
        conversation.getConversation().add(index,messageToEdit);

        // Enregistrement du message modifié en base de données
        daoMessage.update(messageToEdit);
    }

    public void deleteMessage(int messageId) throws SQLException {
        // Récupération du message à modifier à partir de son identifiant
        Message messageToDelete = conversation.getMessageById(messageId); //return un type : null ou Message

        if (messageToDelete == null) {
            //faire l'affichage graphique
            throw new IllegalArgumentException("Le message spécifié n'existe pas dans la conversation");
        }

        // Vérification que l'utilisateur courant est l'auteur du message
        if (!messageToDelete.getAuthor().equals(user.getPseudo())) {
            //faire l'affichage graphique
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer ce message");
        }

        // suppression  du message
        int index = conversation.getConversation().indexOf(messageToDelete);
        conversation.getConversation().remove(messageToDelete);
        daoMessage.delete(messageToDelete); //suppression de l'ancien


        // Enregistrement du message modifié en base de données
        daoMessage.update(messageToDelete);
    }




}



