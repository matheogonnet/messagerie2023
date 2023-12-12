package server.dataAccesModule;

import client.model.Message;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.sql.SQLException;
import java.util.Map;
/**
 * DaoMessage class to manage interactions between Message objects and the database.
 */
public class DaoMessage extends Dao<Message> {

    /**
     * Constructor for DaoMessage.
     *
     * @param url      the database URL
     * @param user     the username for connecting to the database
     * @param password the password for connecting to the database
     */
    public DaoMessage(String url, String user, String password) {
        super(url, user, password);
    }

    /**
     * Adds a message to the database.
     *
     * @param message the message to add
     * @throws SQLException if a database error occurs
     */
    @Override
    public void add(Message message) throws SQLException {
        String query = "INSERT INTO message (id, author, timestamp, content) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, message.getId());
            statement.setString(2, message.getAuthor());
            statement.setString(3, message.getTimeStamp());
            statement.setString(4, message.getContent());
            statement.executeUpdate();
        }
    }

    /**
     * Updates an existing message in the database.
     *
     * @param message the message to update
     * @throws SQLException if a database error occurs
     */
    @Override
    public void update(Message message) throws SQLException {
        String query = "UPDATE message SET content = ? WHERE author = ? AND timestamp = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, message.getContent());
            statement.setString(2, message.getAuthor());
            statement.setString(3, message.getTimeStamp());
            statement.executeUpdate();
        }
    }

    /**
     * Finds a message by its identifier (id) in the database.
     *
     * @param id the identifier of the message to find
     * @return the found message
     * @throws SQLException if a database error occurs
     */
    @Override
    public Message find(int id) throws SQLException {
        String query = "SELECT * FROM message WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    String author = result.getString("author");
                    String timestamp = result.getString("timestamp");
                    String content = result.getString("content");
                    return new Message(author, timestamp, content);
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Deletes a message from the database.
     *
     * @param message the message to delete
     * @throws SQLException if a database error occurs
     */
    @Override
    public void delete(Message message) throws SQLException {
        String query = "DELETE FROM message WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, message.getId());
            statement.executeUpdate();
        }
    }

    /**
     * Retrieves all messages from the database.
     *
     * @return a list of all messages
     * @throws SQLException if a database error occurs
     */
    public ArrayList<Message> findAll() throws SQLException {
        String query = "SELECT * FROM message";
        ArrayList<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                String author = result.getString("author");
                String timestamp = result.getString("timestamp");
                String content = result.getString("content");
                Message message = new Message(author, timestamp, content);
                messages.add(message);
            }
        }
        return messages;
    }

    /**
     * Retrieves the dates and the number of messages for each date in the database.
     *
     * @return a Map with the date as the key and the number of messages as the value
     * @throws SQLException if a database error occurs
     */
    public Map<LocalDate, Integer> getMessageTime() throws SQLException {
        String query = "SELECT DATE(timestamp) AS date, COUNT(*) AS message_count FROM message GROUP BY DATE(timestamp) ORDER BY DATE(timestamp)";
        Map<LocalDate, Integer> messageTime = new LinkedHashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    LocalDate date = result.getDate("date").toLocalDate();
                    int count = result.getInt("message_count");
                    messageTime.put(date, count);
                }
            }
        }
        return messageTime;
    }

    /**
     * Retrieves the number of messages from a specific author.
     *
     * @param author the author for whom to count messages
     * @return the number of messages by the author
     * @throws SQLException if a database error occurs
     */
    public int getMessageCount(String author) throws SQLException {
        String query = "SELECT COUNT(*) FROM message WHERE author = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, author);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getInt(1);
                } else {
                    return 0;
                }
            }
        }
    }
}


