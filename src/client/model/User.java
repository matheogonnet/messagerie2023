package client.model;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe représentant un utilisateur du système.
 */
public class User {
    private int user_ID; // Identifiant unique de l'utilisateur
    private String last_name;
    private String first_name;
    private String pseudo;
    private String password;
    private String last_connection;
    private Status status;
    private Grades grade;
    private boolean ban;

    /**
     * Constructeur pour un utilisateur avec un grade spécifique.
     *
     * @param last_name  Le nom de famille de l'utilisateur.
     * @param first_name Le prénom de l'utilisateur.
     * @param pseudo     Le pseudonyme de l'utilisateur.
     * @param password   Le mot de passe de l'utilisateur.
     * @param grade      Le grade de l'utilisateur.
     */
    public User(String last_name, String first_name, String pseudo, String password, Grades grade) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.pseudo = pseudo;
        this.password = password;
        this.grade = grade;
        this.ban = false;
        this.status = Status.Offline;
    }

    /**
     * Constructeur vide pour un utilisateur.
     */
    public User() {
    }

    /**
     * Constructeur pour un utilisateur avec des paramètres spécifiques.
     *
     * @param pseudo     Le pseudonyme de l'utilisateur.
     * @param last_name  Le nom de famille de l'utilisateur.
     * @param first_name Le prénom de l'utilisateur.
     * @param grade      Le grade de l'utilisateur sous forme de chaîne de caractères.
     * @param status     Le statut de l'utilisateur sous forme de chaîne de caractères.
     * @param ban        Le statut d'interdiction de l'utilisateur sous forme de chaîne de caractères.
     */
    public User(String pseudo, String last_name, String first_name, String grade, String status, String ban) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.pseudo = pseudo;

        this.ban = ban.equals("1");
        this.grade = Grades.valueOf(grade);
        this.status = Status.valueOf(status);
    }

    /**
     * Constructeur pour un utilisateur s'enregistrant en tant qu'utilisateur classique.
     *
     * @param last_name  Le nom de famille de l'utilisateur.
     * @param first_name Le prénom de l'utilisateur.
     * @param pseudo     Le pseudonyme de l'utilisateur.
     * @param password   Le mot de passe de l'utilisateur.
     */
    public User(String last_name, String first_name, String pseudo, String password) {
        this(last_name, first_name, pseudo, password, Grades.Classic);
    }

    /**
     * Constructeur pour la création d'un utilisateur classique en local.
     *
     * @param last_name  Le nom de famille de l'utilisateur.
     * @param first_name Le prénom de l'utilisateur.
     * @param pseudo     Le pseudonyme de l'utilisateur.
     */
    public User(String last_name, String first_name, String pseudo) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.pseudo = pseudo;
        this.grade = Grades.Classic;
        this.ban = false;
        this.status = Status.Offline;
    }
    // Getters et Setters

    /**
     * Retourne l'identifiant unique de l'utilisateur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public int getUser_ID() {
        return user_ID;
    }

    /**
     * Modifie l'identifiant unique de l'utilisateur.
     *
     * @param user_ID Le nouvel identifiant de l'utilisateur.
     */
    /**
     * Modifie l'identifiant unique de l'utilisateur.
     *
     * @param user_ID Le nouvel identifiant de l'utilisateur.
     */
    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    /**
     * Retourne le nom de famille de l'utilisateur.
     *
     * @return Le nom de famille de l'utilisateur.
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Modifie le nom de famille de l'utilisateur.
     *
     * @param last_name Le nouveau nom de famille de l'utilisateur.
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Retourne le prénom de l'utilisateur.
     *
     * @return Le prénom de l'utilisateur.
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Modifie le prénom de l'utilisateur.
     *
     * @param first_name Le nouveau prénom de l'utilisateur.
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Retourne le pseudonyme de l'utilisateur.
     *
     * @return Le pseudonyme de l'utilisateur.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Modifie le pseudonyme de l'utilisateur.
     *
     * @param pseudo Le nouveau pseudonyme de l'utilisateur.
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return Le mot de passe de l'utilisateur.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Modifie le mot de passe de l'utilisateur.
     *
     * @param password Le nouveau mot de passe de l'utilisateur.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retourne la dernière connexion de l'utilisateur.
     *
     * @return La date et l'heure de la dernière connexion de l'utilisateur.
     */
    public String getLast_connection() {
        return last_connection;
    }

    /**
     * Modifie la dernière connexion de l'utilisateur.
     *
     * @param last_connection La nouvelle date et heure de la dernière connexion de l'utilisateur.
     */
    public void setLast_connection(String last_connection) {
        this.last_connection = last_connection;
    }

    /**
     * Retourne le statut de l'utilisateur.
     *
     * @return Le statut de l'utilisateur.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Modifie le statut de l'utilisateur en fonction d'une chaîne de caractères.
     *
     * @param status Le nouveau statut de l'utilisateur sous forme de chaîne de caractères.
     */
    public void setStatus(String status) {
        switch (status) {
            case "Offline" -> this.status = Status.Offline;
            case "Away" -> this.status = Status.Away;
            case "Online" -> this.status = Status.Online;
        }
    }

    /**
     * Modifie le statut de l'utilisateur en fonction d'une valeur d'énumération.
     *
     * @param status Le nouveau statut de l'utilisateur sous forme d'énumération.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Modifie le statut de l'utilisateur en fonction d'un entier.
     *
     * @param status Le nouveau statut de l'utilisateur sous forme d'entier.
     * @throws IllegalArgumentException si la valeur de 'status' est invalide.
     */
    public void setStatus(int status) {
        switch (status) {
            case 0:
                this.status = Status.Offline;
                break;
            case 1:
                this.status = Status.Online;
                break;
            case 2:
                this.status = Status.Away;
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    /**
     * Retourne le grade de l'utilisateur.
     *
     * @return le grade de l'utilisateur
     */
    public Grades getGrade() {
        return grade;
    }

    /**
     * Modifie le grade de l'utilisateur.
     *
     * @param grade le nouveau grade de l'utilisateur
     */
    public void setGrade(Grades grade) {
        this.grade = grade;
    }

    /**
     * Retourne si l'utilisateur est banni.
     *
     * @return true si l'utilisateur est banni, false sinon
     */
    public boolean isBan() {
        return ban;
    }

    /**
     * Modifie l'état de bannissement de l'utilisateur.
     *
     * @param ban true si l'utilisateur est banni, false sinon
     */
    public void setBan(boolean ban) {
        this.ban = ban;
    }

    // Méthodes de connexion et déconnexion

    /**
     * Connecte l'utilisateur et met à jour son statut.
     * À développer : ouverture de la fenêtre.
     */
    public void log_in() {
        setStatus("Online");
        // À développer : ouverture de la fenêtre
    }

    /**
     * Déconnecte l'utilisateur et met à jour son statut.
     * À développer : fermeture de la fenêtre.
     */
    public void log_out() {
        setStatus("Offline");
        // À développer : fermeture de la fenêtre
    }


    /**
     * Bannir un utilisateur en mettant son statut à "ban" et en le déconnectant
     *
     * @param user l'utilisateur à bannir
     */
    public void ban(User user) {
        user.setBan(true);
        user.log_out();
    }

    /**
     * Écrire un message en créant une nouvelle instance de Message avec le contenu et l'horodatage courants.
     *
     * @param content le contenu du message
     * @return une nouvelle instance de Message avec le contenu et l'horodatage courants.
     */
    public Message write_message(String content) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("  [HH:mm]");
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        return new Message(this.pseudo, timestamp, content);
    }

    /**
     * Créer un rapport avec les statistiques des utilisateurs.
     * Si l'utilisateur courant est un administrateur, le rapport inclura le nombre d'utilisateurs connectés et déconnectés, le nombre d'utilisateurs en mode "away",
     * le nombre de modérateurs, d'utilisateurs classiques et d'administrateurs.
     *
     * @param userList la liste des utilisateurs
     */
    public void reporting(List<User> userList) {
        if (this.grade == Grades.Administrator) {
            // Nombre d'utilisateurs connectés et déconnectés
            int nb_online_users;
            int nb_offline_users;
            // Nombre d'utilisateurs en mode "away"
            int nb_away_users;

            //Nombre de modérateurs, d'utilisateurs classiques et d'administrateurs
            int nb_modo = list_moderator(userList);
            int nb_user = list_users(userList);
            int nb_admin = list_admin(userList);

            //TOP : Les 3 utilisateurs les plus actifs (cela peut être calculé à partir du plus long temps de connexion et du plus grand nombre de messages envoyés)
            int top_users;
            int nb_of_connexion;
        }
    }

    /**
     * Afficher la liste de tous les utilisateurs et renvoyer le nombre total d'utilisateurs.
     *
     * @param userList la liste des utilisateurs
     * @return le nombre total d'utilisateurs
     */
    public static int list_users(List<User> userList) {
        for (User user : userList) {
            //affichage de la liste de tous les utilisateurs connectés ou non
            System.out.println(user.getPseudo() + " - " + user.getUser_ID());
        }
        return userList.size();
    }

    /**
     * Afficher la liste de tous les utilisateurs modérateurs et renvoyer le nombre total de modérateurs.
     *
     * @param userList la liste des utilisateurs
     * @return le nombre total de modérateurs
     */
    public static int list_moderator(List<User> userList) {
        int size = 0;
        for (User user : userList) {
            if (user.getGrade() == Grades.Moderator) {
                //nb de modérateurs
                size += 1;
                //affichage de la liste de tous les utilisateurs grade modérateur connectés ou non
                System.out.println(user.getPseudo() + " - " + user.getUser_ID());
            }
        }
        return size;
    }

    /**
     * Retourne la taille de la liste d'utilisateurs ayant le grade d'administrateur
     *
     * @param userList la liste d'utilisateurs à examiner
     * @return la taille de la liste d'utilisateurs ayant le grade d'administrateur
     */
    public static int list_admin(List<User> userList) {
        int size = 0;
        for (User user : userList) {
            if (user.getGrade() == Grades.Administrator) {
                // nb d'administrateurs
                size += 1;
                //affichage de la liste de tous les utilisateurs grade modérateur connectés ou non
                System.out.println(user.getPseudo() + " - " + user.getUser_ID());
            }
        }
        return size;
    }

    /**
     * Vérifie si le mot de passe fourni correspond au mot de passe de l'utilisateur actuel
     *
     * @param password le mot de passe à vérifier
     * @return true si le mot de passe fourni correspond au mot de passe de l'utilisateur actuel, false sinon
     */
    public boolean checkPassword(String password) {
        String hashedPassword = hashPassword(password);
        return this.password.equals(hashedPassword);
    }

    /**
     * Chiffre le mot de passe actuel de l'utilisateur
     */
    public void encryptPassword() {
        this.password = hashPassword(this.password);
    }

    /**
     * Chiffre le mot de passe fourni en utilisant SHA-1
     *
     * @param password le mot de passe à chiffrer
     * @return le mot de passe chiffré
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * Vérifie si cet utilisateur est égal à un autre objet en comparant tous les champs de l'objet
     *
     * @param o l'objet à comparer à cet utilisateur
     * @return true si les deux objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;
        return user_ID == user.user_ID &&
                Objects.equals(last_name, user.last_name) &&
                Objects.equals(first_name, user.first_name) &&
                Objects.equals(pseudo, user.pseudo) &&
                Objects.equals(password, user.password) &&
                grade == user.grade &&
                Objects.equals(last_connection, user.last_connection) &&
                status == user.status;
    }

    /**
     * Retourne un code de hachage pour cet utilisateur en utilisant tous les champs de l'objet
     *
     * @return un code de hachage pour cet utilisateur
     */
    @Override
    public int hashCode() {
        return Objects.hash(user_ID, last_name, first_name, pseudo, password, last_connection, status, grade);
    }

    /**
     * Retourne une chaîne représentant l'état de cet utilisateur. La chaîne est un JSON contenant les informations de l'utilisateur.
     *
     * @return une chaîne représentant l'état de cet utilisateur
     */
    @Override
    public String toString() {
        return "User{" +
                "user_ID=" + user_ID +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", last_connection='" + last_connection + '\'' +
                ", status=" + status +
                ", grade=" + grade +
                ", ban=" + ban +
                '}';

    }

}
