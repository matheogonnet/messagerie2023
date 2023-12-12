// Package pour le module d'accès aux données
package server.dataAccesModule;

// Importation des classes du modèle client
import client.model.Grades;
import client.model.Status;
import client.model.User;

// Importation des bibliothèques nécessaires
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * DaoUser class extending the generic Dao class to manage users.
 */
public class DaoUser extends Dao<User> {

    /**
     * Constructor for the DaoUser class.
     *
     * @param url      the database URL
     * @param user     the database username
     * @param password the database password
     */
    public DaoUser(String url, String user, String password) {
        super(url, user, password);
    }

    /**
     * Method to add a new user to the database.
     *
     * @param user the user to add
     * @throws SQLException if a database error occurs
     */
    @Override
    public void add(User user) throws SQLException {
        // SQL query to insert a new user into the "user" table
        String query = "INSERT INTO user (last_name, first_name, pseudo, password, last_connection, status, grade, ban) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // Using a PreparedStatement to prevent SQL injection and simplify data manipulation
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Filling in the query fields with data from the "user" object
            statement.setString(1, user.getLast_name());
            statement.setString(2, user.getFirst_name());
            statement.setString(3, user.getPseudo());
            user.encryptPassword(); // Encrypting the password before inserting it into the database
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getLast_connection());
            statement.setInt(6, user.getStatus().ordinal());
            statement.setInt(7, user.getGrade().ordinal());
            statement.setBoolean(8, user.isBan());

            // Executing the insertion query
            statement.executeUpdate();
        }
    }

    /**
     * Method to update the information of an existing user in the database.
     *
     * @param user the user to update
     * @throws SQLException if a database error occurs
     */
    @Override
    public void update(User user) throws SQLException {
        // SQL query to update a user in the "user" table
        String query = "UPDATE user SET last_connection = ?, status = ?, grade = ?, ban = ? WHERE pseudo = ?";
        // Using a PreparedStatement to prevent SQL injection and simplify data manipulation
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getLast_connection());
            statement.setInt(2, user.getStatus().ordinal());
            statement.setInt(3, user.getGrade().ordinal());
            statement.setBoolean(4, user.isBan());
            statement.setString(5, user.getPseudo());

            // Executing the update query
            statement.executeUpdate();
        }
    }

    /**
     * Method to find a user by their ID.
     *
     * @param user_ID the ID of the user to search for
     * @return a User object corresponding to the found user, or null if no user is found
     * @throws SQLException if a database error occurs
     */
    @Override
    public User find(int user_ID) throws SQLException {
        String query = "SELECT * FROM user WHERE user_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user_ID); // Adding the user's ID to the query
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = new User();
                    user.setUser_ID(result.getInt("user_id"));
                    user.setLast_name(result.getString("last_name"));
                    user.setFirst_name(result.getString("first_name"));
                    user.setPseudo(result.getString("pseudo"));
                    user.setPassword(result.getString("password"));
                    user.setLast_connection(result.getString("last_connection"));

                    // Retrieving the user's status
                    int status = result.getInt("status");
                    user.setStatus(status);

                    // Converting the "grade" integer to the corresponding enumeration
                    int gradeInt = result.getInt("grade");
                    if (gradeInt < 0 || gradeInt >= Grades.values().length) {
                        throw new IllegalArgumentException("Invalid index for the Grade enum");
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

    /**
     * Method to find a user by their pseudo.
     *
     * @param pseudo the pseudo of the user to search for
     * @return a User object corresponding to the found user, or null if no user is found
     * @throws SQLException if a database error occurs
     */
    public User findByPseudo(String pseudo) throws SQLException {
        String query = "SELECT * FROM user WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pseudo); // Adding the user's pseudo to the query
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = new User();
                    user.setUser_ID(result.getInt("user_id"));
                    user.setLast_name(result.getString("last_name"));
                    user.setFirst_name(result.getString("first_name"));
                    user.setPseudo(result.getString("pseudo"));
                    user.setPassword(result.getString("password"));
                    user.setLast_connection(result.getString("last_connection"));

                    // Converting the "status" integer to the corresponding enumeration
                    int status = result.getInt("status");
                    user.setStatus(status);

                    // Converting the "grade" integer to the corresponding enumeration
                    int gradeInt = result.getInt("grade");
                    if (gradeInt < 0 || gradeInt >= Grades.values().length) {
                        throw new IllegalArgumentException("Invalid index for the Grade enum");
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

    /**
     * Method to update a user's status based on their pseudo.
     *
     * @param status the status to set for the user
     * @param pseudo the user's pseudo to update
     * @throws SQLException if a database error occurs
     */
    public void setDaoStatus(String status, String pseudo) throws SQLException {
        String query = "UPDATE user SET status = ? WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int statusInt = 0;
            switch (status) {
                case "Offline":
                    statusInt = 0;
                    break;
                case "Online":
                    statusInt = 1;
                    break;
                case "Away":
                    statusInt = 2;
                    break;
            }

            statement.setInt(1, statusInt);
            statement.setString(2, pseudo); // Adding the user's pseudo to the query
            statement.executeUpdate();
        }
    }

    /**
     * Method to retrieve a list of all users.
     *
     * @return a list of User objects containing information about all users
     * @throws SQLException if a database error occurs
     */
    public ArrayList<User> findAll() throws SQLException {
        String query = "SELECT * FROM user";
        ArrayList<User> users = new ArrayList<>();
        if (connection == null) {
            throw new SQLException("Database connection is null.");
        }
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                User user = new User();
                user.setUser_ID(result.getInt("user_id"));
                user.setLast_name(result.getString("last_name"));
                user.setFirst_name(result.getString("first_name"));
                user.setPseudo(result.getString("pseudo"));
                user.setPassword(result.getString("password"));
                user.setLast_connection(result.getString("last_connection"));

                int status = result.getInt("status");
                user.setStatus(status);

                int gradeInt = result.getInt("grade");
                if (gradeInt < 0 || gradeInt >= Grades.values().length) {
                    throw new IllegalArgumentException("Invalid index for the Grade enum");
                }
                Grades grade = Grades.values()[gradeInt];
                user.setGrade(grade);

                user.setBan(result.getBoolean("ban"));
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Method to retrieve the number of logins per date.
     *
     * @return a Map with the date as the key and the number of logins as the value
     * @throws SQLException if a database error occurs
     */
    public Map<LocalDate, Integer> getLoginTime() throws SQLException {
        String query = "SELECT DATE(last_connection) AS date, COUNT(*) AS login_count FROM user GROUP BY DATE(last_connection) ORDER BY DATE(last_connection)";
        Map<LocalDate, Integer> loginCountsByDate = new LinkedHashMap<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    LocalDate date = result.getDate("date").toLocalDate();
                    int loginCount = result.getInt("login_count");
                    loginCountsByDate.put(date, loginCount);
                }
            }
        }
        return loginCountsByDate;
    }

    /**
     * Method to delete a user from the database based on their pseudo.
     *
     * @param user the User object containing information about the user to delete
     * @throws SQLException if a database error occurs
     */
    @Override
    public void delete(User user) throws SQLException {
        String query = "DELETE FROM user WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getPseudo()); // Adding the user's pseudo to the query
            statement.executeUpdate(); // Executing the SQL query
        }
    }
}




