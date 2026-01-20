package model;

import java.time.LocalDateTime;

/**
 * Représente une vente (table Vente).
 */
public class Vente {

    private int id;
    private int idPharmacien;
    private int idClient;
    private int idMedicament;
    private int quantite;
    private LocalDateTime date;

    public Vente(int id, int idPharmacien, int idClient,
                 int idMedicament, int quantite, LocalDateTime date) {
        this.id = id;
        this.idPharmacien = idPharmacien;
        this.idClient = idClient;
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.date = date;
    }

    public int getId() { return id; }
    public int getIdPharmacien() { return idPharmacien; }
    public int getIdClient() { return idClient; }
    public int getIdMedicament() { return idMedicament; }
    public int getQuantite() { return quantite; }
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return "Vente { id=" + id +
                ", idPharmacien=" + idPharmacien +
                ", idClient=" + idClient +
                ", idMedicament=" + idMedicament +
                ", quantite=" + quantite +
                ", date=" + date +
                " }";
    }
}
