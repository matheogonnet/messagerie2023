package client.model;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> conversation;
    private String title;
    private int id;

    public Conversation(String title) {
        this.conversation = new ArrayList<Message>();
        this.title = title;
    }

    public void setConversation(ArrayList<Message> conversation) {
        this.conversation = conversation;
    }

    public ArrayList<Message> getConversation() {
        return conversation;
    }

    public Message getMessageById(int id){
        for (Message message : conversation) {
            if (message.getId() == id )
            {
                return message;
            }
        }
        return null;
    }

    public void addMessage(Message message) {
        conversation.add(message);
    }

    public void clear_all_messages() {
        conversation.clear();
    }


    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

