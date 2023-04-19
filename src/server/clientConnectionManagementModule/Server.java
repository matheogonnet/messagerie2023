package server.clientConnectionManagementModule;

import server.dataAccesModule.DaoUser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 3306;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ExecutorService executorService = Executors.newCachedThreadPool();

        System.out.println("Serveur en écoute sur le port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nouvelle connexion cliente établie : " + clientSocket);

            // Créer un thread de connexion client pour gérer cette connexion
            ConnectionThread connectionThread = new ConnectionThread(clientSocket);
            executorService.submit(connectionThread);
        }
    }

    private static class ConnectionThread implements Runnable {
        private final Socket clientSocket;
        private DaoUser userDao; // Exemple avec la table d'utilisateurs

        public ConnectionThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            String url = "jdbc:mysql://localhost:3306/pi2023";
            String username = "root";
            String password = "";
            userDao = new DaoUser(url, username, password);
        }

        @Override
        public void run() {
            try {
                userDao.openConnection();

                // Ici, vous pouvez ajouter la logique pour gérer les échanges
                // avec le client à travers la connexion clientSocket, en utilisant
                // les méthodes de UserDao pour accéder à la base de données

            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ouverture de la connexion à la base de données : " + e.getMessage());
            } finally {
                try {
                    userDao.closeConnection();
                    clientSocket.close();
                    System.out.println("Connexion cliente fermée : " + clientSocket);
                } catch (IOException | SQLException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion cliente : " + e.getMessage());
                }
            }
        }
    }
}
