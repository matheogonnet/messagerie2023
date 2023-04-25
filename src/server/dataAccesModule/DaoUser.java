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
 * Classe DaoUser étendant la classe Dao générique pour gérer les utilisateurs.
 */
public class DaoUser extends Dao<User> {

    /**
     * Constructeur de la classe DaoUser.
     *
     * @param url l'URL de la base de données
     * @param user le nom d'utilisateur de la base de données
     * @param password le mot de passe de la base de données
     */
    public DaoUser(String url, String user, String password) {
        super(url, user, password);
    }

    /**
     * Méthode pour ajouter un nouvel utilisateur à la base de données.
     *
     * @param user l'utilisateur à ajouter
     * @throws SQLException si une erreur de base de données se produit
     */
    @Override
    public void add(User user) throws SQLException {
        // Requête SQL pour insérer un nouvel utilisateur dans la table "user"
        String query = "INSERT INTO user (last_name, first_name, pseudo, password, last_connection, grade, ban) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        // Utilisation d'un PreparedStatement pour éviter les injections SQL et faciliter la manipulation des données
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Remplissage des champs de la requête SQL avec les données de l'objet "user"
            statement.setString(1, user.getLast_name());
            statement.setString(2, user.getFirst_name());
            statement.setString(3, user.getPseudo());
            user.encryptPassword(); // Chiffrement du mot de passe avant de l'insérer dans la base de données
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getLast_connection());
            statement.setInt(6, user.getGrade().ordinal());
            statement.setBoolean(7, user.isBan());

            // Exécution de la requête d'insertion
            statement.executeUpdate();
        }
    }

    /**
     * Méthode pour mettre à jour les informations d'un utilisateur existant dans la base de données.
     *
     * @param user l'utilisateur à mettre à jour
     * @throws SQLException si une erreur de base de données se produit
     */
    @Override
    public void update(User user) throws SQLException {
        // Requête SQL pour mettre à jour un utilisateur dans la table "user"
        String query = "UPDATE user SET last_connection = ?, status = ?, grade = ?, ban = ? WHERE pseudo = ?";
        // Utilisation d'un PreparedStatement pour éviter les injections SQL et faciliter la manipulation des données
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getLast_connection());
            statement.setInt(2, user.getStatus().ordinal());
            statement.setInt(3, user.getGrade().ordinal());
            statement.setBoolean(4, user.isBan());
            statement.setString(5, user.getPseudo());

            // Exécution de la requête de mise à jour
            statement.executeUpdate();
        }
    }
    /**
     * Méthode pour trouver un utilisateur par son ID.
     *
     * @param user_ID l'ID de l'utilisateur à rechercher
     * @return un objet User correspondant à l'utilisateur trouvé, ou null si aucun utilisateur n'a été trouvé
     * @throws SQLException si une erreur de base de données se produit
     */
    @Override
    public User find(int user_ID) throws SQLException {
        String query = "SELECT * FROM user WHERE user_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user_ID); // Ajouter l'ID de l'utilisateur à la requête
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = new User();
                    user.setUser_ID(result.getInt("user_id"));
                    user.setLast_name(result.getString("last_name"));
                    user.setFirst_name(result.getString("first_name"));
                    user.setPseudo(result.getString("pseudo"));
                    user.setPassword(result.getString("password"));
                    user.setLast_connection(result.getString("last_connection"));

                    // Récupération du statut de l'utilisateur
                    String status = result.getString("status");
                    user.setStatus(status);

                    // Conversion de l'entier "grade" en l'énumération correspondante
                    int gradeInt = result.getInt("grade");
                    if (gradeInt < 0 || gradeInt >= Status.values().length) {
                        throw new IllegalArgumentException("Index invalide pour l'enum Statut");
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
     * Méthode pour trouver un utilisateur par son pseudo.
     *
     * @param pseudo le pseudo de l'utilisateur à rechercher
     * @return un objet User correspondant à l'utilisateur trouvé, ou null si aucun utilisateur n'a été trouvé
     * @throws SQLException si une erreur de base de données se produit
     */
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

                    // Conversion de l'entier "status" en l'énumération correspondante
                    int status = result.getInt("status");
                    user.setStatus(status);

                    // Conversion de l'entier "grade" en l'énumération correspondante
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

    /**
     * Méthode pour mettre à jour le statut d'un utilisateur en fonction de son pseudo.
     *
     * @param status le statut à définir pour l'utilisateur
     * @param pseudo le pseudo de l'utilisateur à mettre à jour
     * @throws SQLException si une erreur de base de données se produit
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
            statement.setString(2, pseudo); // Ajouter le pseudo de l'utilisateur à la requête
            statement.executeUpdate();
        }
    }

    /**
     * Méthode pour récupérer la liste de tous les utilisateurs.
     *
     * @return une liste d'objets User contenant les informations de tous les utilisateurs
     * @throws SQLException si une erreur de base de données se produit
     */
    public ArrayList<User> findAll() throws SQLException {
        String query = "SELECT * FROM user";
        ArrayList<User> users = new ArrayList<>();
        if (connection == null) {
            throw new SQLException("La connexion à la base de données est nulle.");
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
                    throw new IllegalArgumentException("Index invalide pour l'enum Grade");
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
     * Méthode pour récupérer le nombre de connexions par date.
     *
     * @return une Map avec la date en tant que clé et le nombre de connexions en tant que valeur
     * @throws SQLException si une erreur de base de données se produit
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
     * Méthode pour supprimer un utilisateur de la base de données en fonction de son pseudo.
     *
     * @param user l'objet User contenant les informations de l'utilisateur à supprimer
     * @throws SQLException si une erreur de base de données se produit
     */
    @Override
    public void delete(User user) throws SQLException {
        String query = "DELETE FROM user WHERE pseudo = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getPseudo()); // Ajouter le pseudo de l'utilisateur à la requête
            statement.executeUpdate(); // Exécuter la requête SQL
        }
    }


}



