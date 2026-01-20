package dao;

import model.Gestionnaire;
import model.Pharmacien;
import model.Role;
import model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO responsable de l'authentification des utilisateurs
 * (pharmaciens et gestionnaires).
 */
public class UtilisateurDAO {

    /**
     * Tente de s'authentifier avec un login et un mot de passe.
     *
     * @param cx    connexion JDBC ouverte
     * @param login login saisi
     * @param pwd   mot de passe saisi
     * @return un objet Pharmacien ou Gestionnaire si succès, sinon null
     */
    public Utilisateur login(Connection cx, String login, String pwd) throws SQLException {

        // 1) Vérifier d'abord dans la table Pharmacien
        String sqlPh = "SELECT id_pharmacien, nom, prenom, login, pwd " +
                       "FROM Pharmacien WHERE login = ? AND pwd = ?";

        try (PreparedStatement ps = cx.prepareStatement(sqlPh)) {
            ps.setString(1, login);
            ps.setString(2, pwd);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Authentification comme pharmacien
                    return new Pharmacien(
                            rs.getInt("id_pharmacien"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("login"),
                            rs.getString("pwd")
                    );
                }
            }
        }

        // 2) Sinon, vérifier dans la table Gestionnaire
        String sqlGest = "SELECT id_gestionnaire, nom, prenom, login, pwd " +
                         "FROM Gestionnaire WHERE login = ? AND pwd = ?";

        try (PreparedStatement ps = cx.prepareStatement(sqlGest)) {
            ps.setString(1, login);
            ps.setString(2, pwd);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Authentification comme gestionnaire
                    return new Gestionnaire(
                            rs.getInt("id_gestionnaire"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("login"),
                            rs.getString("pwd")
                    );
                }
            }
        }

        // 3) Si rien trouvé : identifiants invalides
        return null;
    }
}
