package server.dataAccesModule;

import client.model.Message;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.sql.SQLException;
import java.util.Map;


public class DaoMessage extends Dao<Message> {

    public DaoMessage(String url, String user, String password) {
        super(url, user, password);
    }

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

    @Override
    public Message find(int id) throws SQLException {
        String query = "SELECT * FROM message WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id); // Ajouter l'id du message à la requête
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

    @Override
    public void delete(Message message) throws SQLException {
        String query = "DELETE FROM message WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, message.getId());
            statement.executeUpdate();
        }
    }

    public List<Message> findAll() throws SQLException {
        String query = "SELECT * FROM message";
        List<Message> messages = new ArrayList<>();
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

    // RECUPERE LES DATES DES MESSAGES DANS LA BDD
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
