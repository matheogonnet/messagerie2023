package client.model;
/**
 * Classe représentant un message dans une conversation.
 */
public class Message {
    private int id;
    private String author;
    private String timestamp;
    private String content;

    /**
     * Constructeur pour un message.
     *
     * @param pseudo    Le pseudonyme de l'auteur du message.
     * @param timestamp La date et l'heure du message.
     * @param content   Le contenu du message.
     */
    public Message(String pseudo, String timestamp, String content) {
        this.author = pseudo;
        this.timestamp = timestamp; // Utiliser la clock du lancement du programme
        this.content = content;
    }

    /**
     * Obtient l'ID du message.
     *
     * @return L'ID du message.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtient la date et l'heure du message.
     *
     * @return La date et l'heure du message.
     */
    public String getTimeStamp() {
        return timestamp;
    }

    /**
     * Obtient le contenu du message.
     *
     * @return Le contenu du message.
     */
    public String getContent() {
        return content;
    }

    /**
     * Obtient l'auteur du message.
     *
     * @return Le pseudonyme de l'auteur du message.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Modifie le contenu du message.
     *
     * @param content Le nouveau contenu du message.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du message.
     *
     * @return Une chaîne de caractères représentant le message.
     */
    @Override
    public String toString() {
        return "User{" +
                "message_ID=" + id +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
