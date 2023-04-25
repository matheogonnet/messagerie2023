package server.dataAccesModule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe abstraite Dao générique pour interagir avec la base de données.
 *
 * @param <T> le type d'objet pour lequel le DAO est créé
 */
public abstract class Dao<T> {

    protected Connection connection;
    private final String url;
    private final String user;
    private final String password;

    /**
     * Constructeur du DAO.
     *
     * @param url      l'URL de la base de données
     * @param user     le nom d'utilisateur pour se connecter à la base de données
     * @param password le mot de passe pour se connecter à la base de données
     */
    public Dao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Ouvre la connexion à la base de données.
     *
     * @throws SQLException si une erreur de base de données se produit
     */
    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connection to the data base : OPEN");
    }

    /**
     * Ferme la connexion à la base de données.
     *
     * @throws SQLException si une erreur de base de données se produit
     */
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        System.out.println("Connection to the data base : CLOSE");
    }

    /**
     * Trouve un objet T par son identifiant (id) dans la base de données.
     *
     * @param id l'identifiant de l'objet à trouver
     * @return l'objet T trouvé
     * @throws SQLException si une erreur de base de données se produit
     */
    public abstract T find(int id) throws SQLException;

    /**
     * Ajoute un objet T à la base de données.
     *
     * @param obj l'objet à ajouter
     * @throws SQLException si une erreur de base de données se produit
     */
    public abstract void add(T obj) throws SQLException;

    /**
     * Met à jour un objet T existant dans la base de données.
     *
     * @param obj l'objet à mettre à jour
     * @throws SQLException si une erreur de base de données se produit
     */
    public abstract void update(T obj) throws SQLException;

    /**
     * Supprime un objet T de la base de données.
     *
     * @param obj l'objet à supprimer
     * @throws SQLException si une erreur de base de données se produit
     */
    public abstract void delete(T obj) throws SQLException;
}
