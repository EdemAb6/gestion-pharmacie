package model;

import java.time.LocalDateTime;

/**
 * Représente une ligne d'historique de stock (table StockHistorique).
 */
public class StockHistorique {

    private int idMedicament;
    private int quantite;
    private LocalDateTime date;

    public StockHistorique(int idMedicament, int quantite, LocalDateTime date) {
        this.idMedicament = idMedicament;
        this.quantite = quantite;
        this.date = date;
    }

    public int getIdMedicament() { return idMedicament; }
    public int getQuantite() { return quantite; }
    public LocalDateTime getDate() { return date; }

    @Override
    public String toString() {
        return "StockHistorique { idMedicament=" + idMedicament +
                ", quantite=" + quantite +
                ", date=" + date +
                " }";
    }
}
