package server.dataAccesModule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public abstract class Dao<T> {

    protected Connection connection;
    private final String url;
    private final String user;
    private final String password;

    public Dao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connection to the data base : OPEN");
    }

    // Fermer la connexion à la base de données
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        System.out.println("Connection to the data base : CLOSE");
    }
    public abstract T find(int id) throws SQLException;


    public abstract void add(T obj) throws SQLException;


    public abstract void update(T obj) throws SQLException;


    public abstract void delete(T obj) throws SQLException;
}

