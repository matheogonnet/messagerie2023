//package
package server.dataAccesModule;

//class
import client.model.Grades;
import client.model.Status;
import client.model.User;

//libraries
import java.sql.*;
import java.sql.SQLException;


public class DaoUser extends Dao<User> {
    public DaoUser(String url, String user, String password) {
        super(url, user, password);
    }


    public void add(User user) throws SQLException {
        String query = "INSERT INTO user (last_name, first_name, pseudo, password, last_connection, grade, ban) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Ajoute l'identifiant unique de l'utilisateur
            statement.setString(1, user.getLast_name());
            statement.setString(2, user.getFirst_name());
            statement.setString(3, user.getPseudo());
            user.encryptPassword();
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getLast_connection());
            statement.setInt(6, user.getGrade().ordinal());
            statement.setBoolean(7, user.isBan());
            statement.executeUpdate();

        }
    }

    public void update(User user) throws SQLException {
        String query = "UPDATE user SET last_connection = ?, status = ?, grade = ?, ban = ? WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getLast_connection());
            statement.setInt(2, user.getStatus().ordinal());
            statement.setInt(3, user.getGrade().ordinal());
            statement.setBoolean(4, user.isBan());
            statement.setString(5, user.getPseudo()); // Ajouter le pseudo de l'utilisateur à la requête
            statement.executeUpdate();
        }

    }

    public User find(int user_ID) throws SQLException {
        String query = "SELECT * FROM user WHERE user_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user_ID); // Ajouter le pseudo de l'utilisateur à la requête
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = new User();
                    user.setUser_ID(result.getInt("user_id"));
                    user.setLast_name(result.getString("last_name"));
                    user.setFirst_name(result.getString("first_name"));
                    user.setPseudo(result.getString("pseudo"));
                    user.setPassword(result.getString("password"));
                    user.setLast_connection(result.getString("last_connection"));

                    // a mettre dans une fonction
                    int statusInt = result.getInt("status");
                    if (statusInt < 0 || statusInt >= Status.values().length) {
                        throw new IllegalArgumentException("Index invalide pour l'enum Statut");
                    }
                    Status status = Status.values()[statusInt];
                    user.setStatus(status);

                    int gradeInt = result.getInt("grade");
                    if (gradeInt < 0 || gradeInt >= Status.values().length) {
                        throw new IllegalArgumentException("Index invalide pour l'enum Statut");
                    }
                    Grades grade = Grades.values()[statusInt];
                    user.setGrade(grade);
                    user.setBan(result.getBoolean("ban"));
                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    public User findByPseudo(String pseudo) throws SQLException {
        String query = "SELECT * FROM user WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pseudo); // Ajouter le pseudo de l'utilisateur à la requête
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = new User();
                    user.setUser_ID(result.getInt("user_id"));
                    user.setLast_name(result.getString("last_name"));
                    user.setFirst_name(result.getString("first_name"));
                    user.setPseudo(result.getString("pseudo"));
                    user.setPassword(result.getString("password"));
                    user.setLast_connection(result.getString("last_connection"));

                    // Conversion de l'entier "status" en l'enum correspondant
                    int statusInt = result.getInt("status");
                    if (statusInt < 0 || statusInt >= Status.values().length) {
                        throw new IllegalArgumentException("Index invalide pour l'enum Statut");
                    }
                    Status status = Status.values()[statusInt];
                    user.setStatus(status);

                    // Conversion de l'entier "grade" en l'enum correspondant
                    int gradeInt = result.getInt("grade");
                    if (gradeInt < 0 || gradeInt >= Grades.values().length) {
                        throw new IllegalArgumentException("Index invalide pour l'enum Grade");
                    }
                    Grades grade = Grades.values()[gradeInt];
                    user.setGrade(grade);

                    user.setBan(result.getBoolean("ban"));
                    return user;
                } else {
                    return null;
                }
            }
        }
    }

    public void delete(User user) throws SQLException {
        String query = "DELETE FROM user WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getPseudo()); // Ajouter le pseudo de l'utilisateur à la requête
            statement.executeUpdate(); // Exécuter la requête SQL
        }
    }


}



