package client;
import client.model.Conversation;
import client.model.Grades;
import client.model.Message;
import client.model.User;
import client.view.Menu;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world : graphique, test");
        User user = new User();
        Menu menu = new Menu(user);
    }
}



