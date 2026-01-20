package model;

import java.time.LocalDateTime;

/**
 * Représente une commande de réapprovisionnement (table Commande).
 */
public class Commande {

    private int id;
    private int idGestionnaire;
    private int idMedicament;
    private int quantite;
    private LocalDateTime date;

    public Commande(int id, int idGestionnaire, int idMedicament,
                    int quantite, LocalDateTime date) {
        this.id = id;
        this.idGestionnaire = idGestionnaire;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.date = date;
    }

    public int getId() { return id; }
    public int getIdGestionnaire() { return idGestionnaire; }
    public int getIdMedicament() { return idMedicament; }
    public int getQuantite() { return quantite; }
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return "Commande { id=" + id +
                ", idGestionnaire=" + idGestionnaire +
                ", idMedicament=" + idMedicament +
                ", quantite=" + quantite +
                ", date=" + date +
                " }";
    }
}
