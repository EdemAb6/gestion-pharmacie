package model;

/**
 * Représente un client (table Client).
 */
public class Client {

    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;

    public Client(int id, String nom, String prenom,
                  String email, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }

    public void setAdresse(String adresse) { this.adresse = adresse; }

    @Override
    public String toString() {
        return "Client { id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                " }";
    }
}
