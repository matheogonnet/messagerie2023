package client.model;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public User(String last_name, String first_name, String pseudo, String password, Grades grade) {

        this.last_name = last_name;
        this.first_name = first_name;
        this.pseudo = pseudo;
        this.password = password;
        this.grade =grade;
        this.ban = false;
        this.status = Status.Offline;
    }

    public User(){
    }

    //s'enregistrer comme user classic
    public User(String last_name, String first_name, String pseudo, String password){
        this.last_name = last_name;
        this.first_name = first_name;
        this.pseudo = pseudo;
        this.password = password;
        this.grade = Grades.Classic;
        this.ban = false;
        this.status = Status.Offline;
    }

    //Getters et Setters
    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLast_connection() {
        return last_connection;
    }

    public void setLast_connection(String last_connection) {
        this.last_connection = last_connection;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Grades getGrade() {
        return grade;
    }

    public void setGrade(Grades grade) {
        this.grade = grade;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }


    //CONNECTION
    public void log_in() {
        setStatus(status.Online);

        // A developper et dire que la fenetre s'ouvre
    }
    //DECONNECTION
    public void log_out() {
        setStatus(status.Offline);

        // A developper et dire que la fenetre se ferme
    }



    ///BANISSEMENT////
    public void ban(User user) {
        user.ban =true;
        user.log_out();
    }


    ///MESSAGE///
    public Message write_message(String content) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("  [HH:mm]");
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        //retourne un message qui sera stocké dans la conversation (une liste de messages)
        return new Message(this.pseudo,timestamp, content);
    }





    ///REPORTING///
    // avec la librairie Java JFreeChart
    public void reporting(List<User> userList){
        if (this.grade==Grades.Administrator){
            // Number (Status)
            int nb_online_users;
            int nb_offline_users;
            int nb_away_users;

            //Liste de users (faire des listes et pas des int)
            int nb_modo = list_moderator(userList); //renvoie un nb (int)
            int nb_user = list_users(userList);
            int nb_admin = list_admin(userList);

            //TOP
            int top_users; //3 qui sont le plus actif (plus long temps de connexion + plus grand nb envoie de message)
            int nb_of_connexion;
        }

    }


    public static int list_users(List<User> userList){
        for (User user : userList) {
            //affichage de la liste de tous les utilisateurs connectés ou non
            System.out.println(user.getPseudo() + " - " + user.getUser_ID());
        }
        return userList.size();
    }

    public static int list_moderator(List<User> userList){
        int size =0;
        for (User user : userList) {
            if (user.getGrade()== Grades.Moderator){
                //nb de modérateurs
                size+=1;
                //affichage de la liste de tous les utilisateurs grade modérateur connectés ou non
                System.out.println(user.getPseudo() + " - " + user.getUser_ID());
            }
        }
        return size;
    }

    public static int list_admin(List<User> userList){
        int size =0;
        for (User user : userList) {
            if (user.getGrade()== Grades.Administrator){
                // nb d'administrateurs
                size+=1;
                //affichage de la liste de tous les utilisateurs grade modérateur connectés ou non
                System.out.println(user.getPseudo() + " - " + user.getUser_ID());
            }
        }
        return size;
    }


    public boolean checkPassword(String password) {
        String hashedPassword = hashPassword(password);
        return this.password.equals(hashedPassword);
    }

    public void encryptPassword() {
        this.password = hashPassword(this.password);
    }

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




    @Override
    public int hashCode() {
        return Objects.hash(user_ID, last_name, first_name, pseudo, password, last_connection, status, grade);
    }

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


    /*    REPORTING CHAT GPT

    // Récupérer les données utilisateur pertinentes depuis la base de données
    List<User> users = getUsersFromDatabase();

    // Stocker le nombre d'utilisateurs par statut et type dans une structure de données appropriée
    Map<String, Integer> userCounts = new HashMap<String, Integer>();
for (User user : users) {
        String status = user.get_status().toString();
        String grade = user.get_grade().toString();
        String key = status + " " + grade;
        Integer count = userCounts.get(key);
        userCounts.put(key, (count == null) ? 1 : count + 1);
    }

    // Générer un graphique en camembert pour représenter le nombre d'utilisateurs par statut et type
    DefaultPieDataset dataset = new DefaultPieDataset();
for (Map.Entry<String, Integer> entry : userCounts.entrySet()) {
        dataset.setValue(entry.getKey(), entry.getValue());
    }
    JFreeChart chart = ChartFactory.createPieChart("Nombre d'utilisateurs par statut et type", dataset, true, true, false);

    // Afficher le graphique
    ChartFrame frame = new ChartFrame("Statistiques utilisateur", chart);
frame.pack();
frame.setVisible(true);

*/







