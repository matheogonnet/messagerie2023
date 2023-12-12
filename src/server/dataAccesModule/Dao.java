package server.dataAccesModule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract generic Dao class for interacting with the database.
 *
 * @param <T> the type of object for which the DAO is created.
 */

public abstract class Dao<T> {

    protected Connection connection;
    private final String url;
    private final String user;
    private final String password;

    /**
     * Constructor for the DAO class.
     *
     * @param url      The URL of the database.
     * @param user     The username for connecting to the database.
     * @param password The password for connecting to the database.
     */
    public Dao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Opens a connection to the database.
     *
     * @throws SQLException if a database error occurs.
     */
    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Database Connection: OPEN");
    }

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException if a database error occurs.
     */
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        System.out.println("Database Connection: CLOSE");
    }

    /**
     * Finds an object of type T by its identifier (id) in the database.
     *
     * @param id the identifier of the object to find.
     * @return the found object of type T.
     * @throws SQLException if a database error occurs.
     */
    public abstract T find(int id) throws SQLException;

    /**
     * Adds an object of type T to the database.
     *
     * @param obj the object to add.
     * @throws SQLException if a database error occurs.
     */
    public abstract void add(T obj) throws SQLException;

    /**
     * Updates an existing object of type T in the database.
     *
     * @param obj the object to update.
     * @throws SQLException if a database error occurs.
     */
    public abstract void update(T obj) throws SQLException;

    /**
     * Deletes an object of type T from the database.
     *
     * @param obj the object to delete.
     * @throws SQLException if a database error occurs.
     */
    public abstract void delete(T obj) throws SQLException;

}
