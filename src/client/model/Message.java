package client.model;


public class Message {
    private int id;
    private String author;
    private String timestamp;
    private String content;

    public Message(String pseudo,String timestamp, String content) {


        this.author = pseudo;
        this.timestamp = timestamp; //utliser la clock du lancement du programme
        this.content = content;
    }




    public int getId() {
        return id;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void setContent(String content) {

        this.content = content;
    }

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