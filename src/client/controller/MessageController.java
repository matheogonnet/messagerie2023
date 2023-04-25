package client.controller;

import client.model.Conversation;
import client.model.Message;
import client.model.User;
import server.dataAccesModule.DaoMessage;

import java.sql.SQLException;

/**
 * Contrôleur de gestion des messages.
 */
public class MessageController {

    private User user;
    private DaoMessage daoMessage;
    private Conversation conversation;

    /**
     * Constructeur du contrôleur de gestion des messages.
     *
     * @param user         l'utilisateur actuel
     * @param daoMessage   accès aux données des messages
     * @param conversation la conversation en cours
     */
    public MessageController(User user, DaoMessage daoMessage, Conversation conversation) {
        this.user = user;
        this.daoMessage = daoMessage;
        this.conversation = conversation;
    }

    /**
     * Envoie un message.
     *
     * @param content contenu du message
     * @throws SQLException en cas d'erreur SQL
     */
    public void send(String content) throws SQLException {
        Message message = user.write_message(content);
        conversation.addMessage(message);
        daoMessage.add(message);
        daoMessage.update(message);
    }

    /**
     * Modifie un message.
     *
     * @param messageId  identifiant du message à modifier
     * @param newContent nouveau contenu du message
     * @throws SQLException en cas d'erreur SQL
     */
    public void editMessage(int messageId, String newContent) throws SQLException {
        Message messageToEdit = conversation.getMessageById(messageId);

        if (messageToEdit == null) {
            throw new IllegalArgumentException("Le message spécifié n'existe pas dans la conversation");
        }

        if (!messageToEdit.getAuthor().equals(user.getPseudo())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à modifier ce message");
        }

        int index = conversation.getConversation().indexOf(messageToEdit);
        conversation.getConversation().remove(messageToEdit);
        daoMessage.delete(messageToEdit);

        messageToEdit.setContent(newContent);
        conversation.getConversation().add(index, messageToEdit);

        daoMessage.update(messageToEdit);
    }

    /**
     * Supprime un message.
     *
     * @param messageId identifiant du message à supprimer
     * @throws SQLException en cas d'erreur SQL
     */
    public void deleteMessage(int messageId) throws SQLException {
        Message messageToDelete = conversation.getMessageById(messageId);

        if (messageToDelete == null) {
            throw new IllegalArgumentException("Le message spécifié n'existe pas dans la conversation");
        }

        if (!messageToDelete.getAuthor().equals(user.getPseudo())) {
            throw new IllegalArgumentException("Vous n'êtes pas autorisé à supprimer ce message");
        }

        int index = conversation.getConversation().indexOf(messageToDelete);
        conversation.getConversation().remove(messageToDelete);
        daoMessage.delete(messageToDelete);

        daoMessage.update(messageToDelete);
    }
}
