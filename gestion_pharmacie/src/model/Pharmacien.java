package model;

/**
 * Représente un pharmacien (table Pharmacien).
 * Hérite de la classe Utilisateur.
 */
public class Pharmacien extends Utilisateur {

    /**
     * Constructeur du pharmacien.
     *
     * @param id      identifiant du pharmacien
     * @param nom     nom du pharmacien
     * @param prenom  prénom du pharmacien
     * @param login   login de connexion
     * @param pwd     mot de passe
     */
    public Pharmacien(int id, String nom, String prenom,
                      String login, String pwd) {
        // On appelle le constructeur de la classe mère (Utilisateur)
        super(id, nom, prenom, login, pwd, Role.PHARMACIEN);
    }
}
