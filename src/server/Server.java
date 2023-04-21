package server;

import server.clientConnectionManagementModule.ChatServer;

import java.io.IOException;
import java.sql.SQLException;


public class Server {
    public static void main(String[] args) throws IOException, SQLException {
        new ChatServer(4000);

    }
}