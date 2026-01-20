package model;

/**
 * Représente un gestionnaire (table Gestionnaire).
 * Hérite de la classe Utilisateur.
 */
public class Gestionnaire extends Utilisateur {

    /**
     * Constructeur du gestionnaire.
     *
     * @param id      identifiant du gestionnaire
     * @param nom     nom du gestionnaire
     * @param prenom  prénom du gestionnaire
     * @param login   login de connexion
     * @param pwd     mot de passe
     */
    public Gestionnaire(int id, String nom, String prenom,
                        String login, String pwd) {
        // On appelle le constructeur de la classe mère (Utilisateur)
        super(id, nom, prenom, login, pwd, Role.GESTIONNAIRE);
    }
}
