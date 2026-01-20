package model;

/**
 * Classe abstraite qui représente un utilisateur de l'application.
 * Un utilisateur peut être un pharmacien ou un gestionnaire.
 */
public abstract class Utilisateur {

    /** Identifiant unique dans la base de données. */
    protected int id;

    /** Nom de l'utilisateur. */
    protected String nom;

    /** Prénom de l'utilisateur. */
    protected String prenom;

    /** Login utilisé pour se connecter à l'application. */
    protected String login;

    /** Mot de passe de connexion (à ne jamais afficher). */
    protected String pwd;

    /** Rôle de l'utilisateur (PHARMACIEN ou GESTIONNAIRE). */
    protected Role role;

    /**
     * Constructeur principal.
     *
     * @param id      identifiant de l'utilisateur
     * @param nom     nom de l'utilisateur
     * @param prenom  prénom de l'utilisateur
     * @param login   login de connexion
     * @param pwd     mot de passe
     * @param role    rôle de l'utilisateur
     */
    public Utilisateur(int id, String nom, String prenom,
                       String login, String pwd, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.login = login;
        this.pwd = pwd;
        this.role = role;
    }

    /** @return l'identifiant de l'utilisateur. */
    public int getId() { return id; }

    /** @return le nom de l'utilisateur. */
    public String getNom() { return nom; }

    /** @return le prénom de l'utilisateur. */
    public String getPrenom() { return prenom; }

    /** @return le login de l'utilisateur. */
    public String getLogin() { return login; }

    /** @return le mot de passe (en pratique, à manipuler avec précaution). */
    public String getPwd() { return pwd; }

    /** @return le rôle de l'utilisateur. */
    public Role getRole() { return role; }

    /**
     * Modifie le mot de passe.
     * @param pwd nouveau mot de passe
     */
    public void setPwd(String pwd) { this.pwd = pwd; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " { " +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                " }";
    }
}
