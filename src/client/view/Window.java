package client.view;

import javax.swing.*;

public class Window {

    static public void closeWindow(JFrame frame ){
        frame.dispose();
        frame.setVisible(false);
    }

    static public void displayWindow(JFrame frame){
        frame.setVisible(true);
    }

}
