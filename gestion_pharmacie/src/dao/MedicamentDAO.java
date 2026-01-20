package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Medicament;

/**
 * DAO : classe qui contient le SQL (accès à la BD) pour Medicament.
 */
public class MedicamentDAO {

    /**
     * Récupère tous les médicaments.
     * IMPORTANT : on NE ferme PAS la Connection ici (elle est gérée dans App.java).
     */
    public List<Medicament> findAll(Connection cx) throws SQLException {

        List<Medicament> liste = new ArrayList<>();

        Statement st = null;
        ResultSet rs = null;

        try {
            st = cx.createStatement();
            rs = st.executeQuery(
                "SELECT id_medicament, nom, dosage, stock, prix_unitaire FROM Medicament"
            );

            while (rs.next()) {
                Medicament m = new Medicament(
                        rs.getInt("id_medicament"),
                        rs.getString("nom"),
                        rs.getString("dosage"),
                        rs.getInt("stock"),
                        rs.getDouble("prix_unitaire")
                );
                liste.add(m);
            }

        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }

        return liste;
    }

    /**
     * Recherche un médicament par son identifiant.
     *
     * @param cx           connexion JDBC ouverte
     * @param idMedicament identifiant du médicament
     * @return le Medicament trouvé ou null si aucun résultat
     */
    public Medicament findById(Connection cx, int idMedicament) throws SQLException {

        String sql = "SELECT id_medicament, nom, dosage, stock, prix_unitaire " +
                     "FROM Medicament WHERE id_medicament = ?";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idMedicament);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicament(
                            rs.getInt("id_medicament"),
                            rs.getString("nom"),
                            rs.getString("dosage"),
                            rs.getInt("stock"),
                            rs.getDouble("prix_unitaire")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    /**
     * Insère un nouveau médicament dans la base.
     * On suppose que id_medicament est AUTO_INCREMENT.
     */
    public void insert(Connection cx, Medicament m) throws SQLException {

        String sql = "INSERT INTO Medicament(nom, dosage, stock, prix_unitaire) " +
                     "VALUES(?, ?, ?, ?)";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, m.getNom());
            ps.setString(2, m.getDosage());
            ps.setInt(3, m.getStock());
            ps.setDouble(4, m.getPrixUnitaire());

            ps.executeUpdate();
        }
    }

    /**
     * Met à jour le stock d'un médicament (valeur absolue).
     */
    public void updateStock(Connection cx, int idMedicament, int newStock) throws SQLException {

        String sql = "UPDATE Medicament SET stock = ? WHERE id_medicament = ?";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, newStock);
            ps.setInt(2, idMedicament);

            ps.executeUpdate();
        }
    }

    /**
     * Récupère les médicaments dont le stock est inférieur au seuil donné.
     *
     * @param cx     connexion JDBC ouverte
     * @param seuil  valeur maximale de stock pour être considéré comme critique
     * @return liste des médicaments en stock critique
     */
    public List<Medicament> findStockCritique(Connection cx, int seuil) throws SQLException {

        String sql = "SELECT id_medicament, nom, dosage, stock, prix_unitaire " +
                     "FROM Medicament WHERE stock < ? ORDER BY stock ASC";

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, seuil);

            try (ResultSet rs = ps.executeQuery()) {
                List<Medicament> liste = new ArrayList<>();

                while (rs.next()) {
                    Medicament m = new Medicament(
                            rs.getInt("id_medicament"),
                            rs.getString("nom"),
                            rs.getString("dosage"),
                            rs.getInt("stock"),
                            rs.getDouble("prix_unitaire")
                    );
                    liste.add(m);
                }

                return liste;
            }
        }
    }
}
