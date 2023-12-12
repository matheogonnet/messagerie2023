package server;

// Import necessary classes for server functionality
import server.clientConnectionManagementModule.Server;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main class for the server application.
 */
public class MainServer {

    /**
     * Main method to start the server.
     *
     * @param args Command-line arguments
     * @throws IOException  if an I/O error occurs
     * @throws SQLException if a database error occurs
     */
    public static void main(String[] args) throws IOException, SQLException {
        // Create an instance of DaoUser to manage users with a database connection
        DaoUser daoUser = new DaoUser("jdbc:mysql://localhost:3306/pi2023", "root", "");

        // Create an instance of DaoMessage to manage messages with a database connection
        DaoMessage daoMessage = new DaoMessage("jdbc:mysql://localhost:3306/pi2023", "root", "");

        // Open database connections for daoUser and daoMessage
        daoMessage.openConnection();
        daoUser.openConnection();

        // Create and start an instance of Server with a listening port, user controller, daoMessage, and daoUser
        new Server(4000, daoMessage, daoUser);

        // Close database connections for daoUser and daoMessage
        daoUser.closeConnection();
        daoMessage.closeConnection();
    }
}
