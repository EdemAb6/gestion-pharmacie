package dao;

import model.Commande;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des commandes de réapprovisionnement.
 */
public class CommandeDAO {

    /**
     * Crée une nouvelle commande dans la base.
     * On suppose que id_commande est AUTO_INCREMENT.
     */
    public void creerCommande(Connection cx, Commande commande) throws SQLException {

        String sql = "INSERT INTO Commande(id_gestionnaire, id_medicament, quantite, `date`) " +
                     "VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, commande.getIdGestionnaire());
            ps.setInt(2, commande.getIdMedicament());
            ps.setInt(3, commande.getQuantite());
            ps.setTimestamp(4, Timestamp.valueOf(commande.getDate()));

            ps.executeUpdate();
        }
    }

    /**
     * Récupère toutes les commandes.
     */
    public List<Commande> findAll(Connection cx) throws SQLException {

        String sql = "SELECT id_commande, id_gestionnaire, id_medicament, quantite, `date` " +
                     "FROM Commande ORDER BY `date` DESC";

        try (PreparedStatement ps = cx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Commande> liste = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id_commande");
                int idGest = rs.getInt("id_gestionnaire");
                int idMed = rs.getInt("id_medicament");
                int quantite = rs.getInt("quantite");
                LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                Commande c = new Commande(id, idGest, idMed, quantite, date);
                liste.add(c);
            }

            return liste;
        }
    }

    /**
     * Récupère les commandes pour un médicament donné.
     */
    public List<Commande> findByMedicament(Connection cx, int idMedicament) throws SQLException {

        String sql = "SELECT id_commande, id_gestionnaire, id_medicament, quantite, `date` " +
                     "FROM Commande WHERE id_medicament = ? ORDER BY `date` DESC";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idMedicament);

            try (ResultSet rs = ps.executeQuery()) {
                List<Commande> liste = new ArrayList<>();

                while (rs.next()) {
                    int id = rs.getInt("id_commande");
                    int idGest = rs.getInt("id_gestionnaire");
                    int idMed = rs.getInt("id_medicament");
                    int quantite = rs.getInt("quantite");
                    LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                    Commande c = new Commande(id, idGest, idMed, quantite, date);
                    liste.add(c);
                }

                return liste;
            }
        }
    }
}
