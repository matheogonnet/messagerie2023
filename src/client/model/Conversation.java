package client.model;

import java.util.ArrayList;
/**
 *  Cette classe représente une conversation.
 */
public class Conversation {
    private ArrayList<Message> conversation; // Liste des messages échangés dans la conversation
    private String title; // Titre de la conversation
    private int id;  // Identifiant unique de la conversation

    /**
     * Constructeur de la classe Conversation.
     *
     * @param title Titre de la conversation.
     */
    public Conversation(String title) {
        this.conversation = new ArrayList<Message>();
        this.title = title;
    }

    /**
     * Modifie la liste des messages échangés dans la conversation.
     *
     * @param conversation Nouvelle liste des messages échangés dans la conversation.
     */
    public void setConversation(ArrayList<Message> conversation) {
        this.conversation = conversation;
    }

    /**
     * Obtient la liste des messages échangés dans la conversation.
     *
     * @return Liste des messages échangés dans la conversation.
     */
    public ArrayList<Message> getConversation() {
        return conversation;
    }

    /**
     * Obtient un message de la conversation à partir de son identifiant.
     *
     * @param id Identifiant du message recherché.
     * @return Le message correspondant à l'identifiant, ou null s'il n'existe pas.
     */
    public Message getMessageById(int id) {
        for (Message message : conversation) {
            if (message.getId() == id) {
                return message;
            }
        }
        return null;
    }

    /**
     * Ajoute un message à la conversation.
     *
     * @param message Le message à ajouter à la conversation.
     */
    public void addMessage(Message message) {
        conversation.add(message);
    }

    /**
     * Supprime tous les messages de la conversation.
     */
    public void clear_all_messages() {
        conversation.clear();
    }

    /**
     * Obtient le titre de la conversation.
     *
     * @return Titre de la conversation.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Modifie l'identifiant de la conversation.
     *
     * @param id Nouvel identifiant de la conversation.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtient l'identifiant de la conversation.
     *
     * @return Identifiant de la conversation.
     */
    public int getId() {
        return id;
    }
}