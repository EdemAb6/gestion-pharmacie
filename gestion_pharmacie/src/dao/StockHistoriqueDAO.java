package dao;

import model.StockHistorique;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de la consultation de l'historique de stock.
 */
public class StockHistoriqueDAO {

    /**
     * Récupère l'historique de stock pour un médicament donné.
     *
     * @param cx           connexion JDBC ouverte
     * @param idMedicament identifiant du médicament
     * @return liste ordonnée par date croissante
     */
    public List<StockHistorique> findByMedicament(Connection cx, int idMedicament) throws SQLException {

        String sql = "SELECT id_medicament, quantite, `date` " +
                     "FROM StockHistorique " +
                     "WHERE id_medicament = ? " +
                     "ORDER BY `date` ASC";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idMedicament);

            try (ResultSet rs = ps.executeQuery()) {
                List<StockHistorique> liste = new ArrayList<>();

                while (rs.next()) {
                    int idMed = rs.getInt("id_medicament");
                    int quantite = rs.getInt("quantite");
                    LocalDateTime date = rs.getTimestamp("date").toLocalDateTime();

                    StockHistorique sh = new StockHistorique(idMed, quantite, date);
                    liste.add(sh);
                }

                return liste;
            }
        }
    }
}
