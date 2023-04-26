package server.clientConnectionManagementModule;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.PieDataset;
import org.jfree.data.category.DefaultCategoryDataset;


import client.model.Grades;
import client.model.Status;
import client.model.User;
import server.dataAccesModule.DaoMessage;
import server.dataAccesModule.DaoUser;

import java.sql.SQLException;



/**
 * Classe de reporting pour générer des graphiques.
 */
public class Reporting {
    private List<User> userList;

    /**
     * Constructeur de Reporting.
     *
     * @param userList liste des utilisateurs
     */
    public Reporting(List<User> userList) {
        this.userList = userList;
    }

    /**
     * Génère un graphique circulaire montrant la répartition des grades des utilisateurs.
     */
    public void GraphGrades() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Compter le nombre d'utilisateurs pour chaque grade
        Map<Grades, Integer> gradeCounts = new HashMap<>();
        for (User user : userList) {
            Grades grade = user.getGrade();
            gradeCounts.put(grade, gradeCounts.getOrDefault(grade, 0) + 1);
        }

        // Ajouter les données au dataset
        for (Map.Entry<Grades, Integer> entry : gradeCounts.entrySet()) {
            dataset.setValue(entry.getKey().toString(), entry.getValue());
        }

        // Créer le graphique
        JFreeChart chart = ChartFactory.createPieChart("Percantage of users' grades", dataset, true, true, false);

        // Afficher le graphique
        ChartFrame frame = new ChartFrame("Users by grade", chart);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Génère un graphique circulaire montrant la répartition des statuts des utilisateurs.
     */
    public void GraphStatus() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Compter le nombre d'utilisateurs pour chaque statut
        Map<Status, Integer> statusCounts = new HashMap<>();
        for (User user : userList) {
            Status status = user.getStatus();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
        }

        // Ajouter les données au dataset
        for (Map.Entry<Status, Integer> entry : statusCounts.entrySet()) {
            dataset.setValue(entry.getKey().toString(), entry.getValue());
        }

        // Créer le graphique
        JFreeChart chart = ChartFactory.createPieChart("Users' status", dataset, true, true, false);

        // Afficher le graphique
        ChartFrame frame = new ChartFrame("Percentage of status", chart);
        frame.pack();
        frame.setVisible(true);
    }
    /**
     * Génère un graphique en bâtons montrant le nombre de messages par utilisateur.
     *
     * @param daoMessage instance de DaoMessage pour accéder à la base de données
     * @throws SQLException si une erreur de base de données se produit
     */
    public void GraphTopUser(DaoMessage daoMessage) throws SQLException {
        Map<String, Integer> messageCounts = new HashMap<>();
        for (User user : userList) {
            int count = daoMessage.getMessageCount(user.getPseudo());
            messageCounts.put(user.getPseudo(), count);
        }

        // Créer un dataset pour représenter les données
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : messageCounts.entrySet()) {
            dataset.setValue(entry.getValue(), "Number of messages", entry.getKey());
        }

        // Créer un graphique en bâtons pour représenter les données
        JFreeChart chart = ChartFactory.createBarChart(
                "User's number of messages", // Titre du graphique
                "User", // Axe x
                "Number of messages", // Axe y
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Orientation
                false, // Inclure la légende
                true, // Afficher les tooltips
                false // Afficher les URLs
        );

        // Créer une fenêtre pour afficher le graphique
        ChartFrame frame = new ChartFrame("Users' stats", chart);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Génère un graphique en bâtons montrant le nombre de messages envoyés par jour.
     *
     * @param daoMessage instance de DaoMessage pour accéder à la base de données
     */
    public void GraphMessageTime(DaoMessage daoMessage) {
        try {
            Map<LocalDate, Integer> messageCountsByDate = daoMessage.getMessageTime();
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<LocalDate, Integer> entry : messageCountsByDate.entrySet()) {
                dataset.addValue(entry.getValue(), "Messages sent", entry.getKey());
            }
            JFreeChart chart = ChartFactory.createBarChart(
                    "Number of message per day",
                    "Date",
                    "Number of message",
                    dataset,
                    PlotOrientation.VERTICAL,
                    false,
                    true,
                    false
            );
            ChartFrame frame = new ChartFrame("Message's stats", chart);
            frame.pack();
            frame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // INUTILISABLE POUR L INSTANT A CAUSE DE LAST_CONNECTION QUI EST NULL
  /*  public void GraphLoginTime(DaoUser daoUser) throws SQLException {
        // Récupérer les données à partir du DAO
        Map<LocalDate, Integer> loginTime = daoUser.getLoginTime();

        // Créer un dataset pour stocker les données
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Ajouter les données au dataset
        for (Map.Entry<LocalDate, Integer> entry : loginTime.entrySet()) {
            dataset.setValue(entry.getValue(), "Number of connections", entry.getKey().toString());
        }

        // Créer un graphique en bâtons pour représenter les données
        JFreeChart chart = ChartFactory.createBarChart(
                "Number of user's connections per day", // Titre du graphique
                "Date", // Axe x
                "Number of connections", // Axe y
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Orientation
                false, // Inclure la légende
                true, // Afficher les tooltips
                false // Afficher les URLs
        );

        // Créer une fenêtre pour afficher le graphique
        ChartFrame frame = new ChartFrame("Users' connections' stats", chart);
        frame.pack();
        frame.setVisible(true);
    } */



}
