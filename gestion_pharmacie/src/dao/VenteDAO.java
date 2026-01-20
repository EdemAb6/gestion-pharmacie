package dao;

import model.Vente;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la gestion des ventes.
 */
public class VenteDAO {

    /**
     * Enregistre une vente :
     *  - insère dans la table Vente
     *  - met à jour le stock dans Medicament
     *  - ajoute une ligne dans StockHistorique
     *
     * Tout est fait dans une même transaction.
     */
    public void enregistrerVente(Connection cx, Vente vente) throws SQLException {

        boolean ancienAutoCommit = cx.getAutoCommit();
        cx.setAutoCommit(false);

        try {
            // 1) Insérer la vente
            String sqlInsertVente =
                    "INSERT INTO Vente(id_pharmacien, id_client, id_medicament, quantite, `date`) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement ps = cx.prepareStatement(sqlInsertVente)) {
                ps.setInt(1, vente.getIdPharmacien());
                ps.setInt(2, vente.getIdClient());
                ps.setInt(3, vente.getIdMedicament());
                ps.setInt(4, vente.getQuantite());
                ps.setTimestamp(5, Timestamp.valueOf(vente.getDate()));

                ps.executeUpdate();
            }

            // 2) Mettre à jour le stock du médicament
            String sqlUpdateStock =
                    "UPDATE Medicament SET stock = stock - ? WHERE id_medicament = ?";

            try (PreparedStatement ps = cx.prepareStatement(sqlUpdateStock)) {
                ps.setInt(1, vente.getQuantite());
                ps.setInt(2, vente.getIdMedicament());
                ps.executeUpdate();
            }

            // 3) Récupérer le stock actuel
            int stockActuel = 0;
            String sqlSelectStock =
                    "SELECT stock FROM Medicament WHERE id_medicament = ?";

            try (PreparedStatement ps = cx.prepareStatement(sqlSelectStock)) {
                ps.setInt(1, vente.getIdMedicament());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stockActuel = rs.getInt("stock");
                    }
                }
            }

            // 4) Insérer dans l'historique
            String sqlInsertHist =
                    "INSERT INTO StockHistorique(id_medicament, quantite, `date`) " +
                    "VALUES (?, ?, ?)";

            try (PreparedStatement ps = cx.prepareStatement(sqlInsertHist)) {
                ps.setInt(1, vente.getIdMedicament());
                ps.setInt(2, stockActuel);
                ps.setTimestamp(3, Timestamp.valueOf(vente.getDate()));
                ps.executeUpdate();
            }

            cx.commit();

        } catch (SQLException ex) {
            cx.rollback();
            throw ex;
        } finally {
            cx.setAutoCommit(ancienAutoCommit);
        }
    }

    /**
     * Récupère toutes les ventes.
     */
    public List<Vente> findAll(Connection cx) throws SQLException {
        String sql = "SELECT id_vente, id_pharmacien, id_client, id_medicament, quantite, `date` " +
                     "FROM Vente ORDER BY `date` DESC";

        try (PreparedStatement ps = cx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Vente> liste = new ArrayList<>();

            while (rs.next()) {
                liste.add(mapVente(rs));
            }
            return liste;
        }
    }

    /**
     * Récupère les ventes pour un client donné.
     */
    public List<Vente> findByClient(Connection cx, int idClient) throws SQLException {
        String sql = "SELECT id_vente, id_pharmacien, id_client, id_medicament, quantite, `date` " +
                     "FROM Vente WHERE id_client = ? ORDER BY `date` DESC";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idClient);

            try (ResultSet rs = ps.executeQuery()) {
                List<Vente> liste = new ArrayList<>();
                while (rs.next()) {
                    liste.add(mapVente(rs));
                }
                return liste;
            }
        }
    }

    /**
     * Récupère les ventes pour un médicament donné.
     */
    public List<Vente> findByMedicament(Connection cx, int idMedicament) throws SQLException {
        String sql = "SELECT id_vente, id_pharmacien, id_client, id_medicament, quantite, `date` " +
                     "FROM Vente WHERE id_medicament = ? ORDER BY `date` DESC";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idMedicament);

            try (ResultSet rs = ps.executeQuery()) {
                List<Vente> liste = new ArrayList<>();
                while (rs.next()) {
                    liste.add(mapVente(rs));
                }
                return liste;
            }
        }
    }

    /**
     * Méthode utilitaire pour transformer une ligne du ResultSet en objet Vente.
     */
    private Vente mapVente(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_vente");
        int idPharmacien = rs.getInt("id_pharmacien");
        int idClient = rs.getInt("id_client");
        int idMedicament = rs.getInt("id_medicament");
        int quantite = rs.getInt("quantite");
        Timestamp ts = rs.getTimestamp("date");
        LocalDateTime date = ts.toLocalDateTime();

        return new Vente(id, idPharmacien, idClient, idMedicament, quantite, date);
    }
}
