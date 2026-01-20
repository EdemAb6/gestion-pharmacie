package app;

import config.DbConnection;
import dao.CommandeDAO;
import dao.MedicamentDAO;
import dao.StockHistoriqueDAO;
import dao.UtilisateurDAO;
import dao.VenteDAO;
import model.Commande;
import model.Medicament;
import model.Role;
import model.StockHistorique;
import model.Utilisateur;
import model.Vente;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Connection cx = null;
        Scanner sc = new Scanner(System.in);

        try {
            cx = DbConnection.getConnection();
            System.out.println("Connexion à la BD réussie ✅");

            UtilisateurDAO userDao = new UtilisateurDAO();

            Utilisateur utilisateurConnecte = null;
            while (utilisateurConnecte == null) {
                System.out.println("\n=== Authentification ===");
                System.out.print("Login : ");
                String login = sc.nextLine();
                System.out.print("Mot de passe : ");
                String pwd = sc.nextLine();

                utilisateurConnecte = userDao.login(cx, login, pwd);

                if (utilisateurConnecte == null) {
                    System.out.println("Login ou mot de passe incorrect. Veuillez réessayer.");
                }
            }

            System.out.println("\nBienvenue " + utilisateurConnecte.getPrenom() + " " +
                    utilisateurConnecte.getNom() + " (" + utilisateurConnecte.getRole() + ")");

            MedicamentDAO medicamentDao = new MedicamentDAO();
            VenteDAO venteDao = new VenteDAO();
            CommandeDAO commandeDao = new CommandeDAO();
            StockHistoriqueDAO histDao = new StockHistoriqueDAO();

            boolean quitter = false;
            while (!quitter) {
                System.out.println("\n=== Menu Pharmacie ===");
                System.out.println("1. Lister les médicaments");
                System.out.println("2. Ajouter un médicament");
                System.out.println("3. Modifier le stock d'un médicament");
                System.out.println("4. Enregistrer une vente");
                System.out.println("5. Consulter les ventes");
                System.out.println("6. Créer une commande");
                System.out.println("7. Consulter les commandes");
                System.out.println("8. Consulter le stock critique");
                System.out.println("9. Consulter l'historique de stock");
                System.out.println("0. Quitter");
                System.out.print("Votre choix : ");

                int choix = sc.nextInt();
                sc.nextLine();

                switch (choix) {
                    case 1:
                        listerMedicaments(medicamentDao, cx);
                        break;
                    case 2:
                        if (utilisateurConnecte.getRole() == Role.GESTIONNAIRE) {
                            ajouterMedicament(medicamentDao, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux gestionnaires.");
                        }
                        break;
                    case 3:
                        if (utilisateurConnecte.getRole() == Role.GESTIONNAIRE) {
                            modifierStock(medicamentDao, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux gestionnaires.");
                        }
                        break;
                    case 4:
                        if (utilisateurConnecte.getRole() == Role.PHARMACIEN) {
                            enregistrerVente(venteDao, utilisateurConnecte, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux pharmaciens.");
                        }
                        break;
                    case 5:
                        consulterVentes(venteDao, cx, sc);
                        break;
                    case 6:
                        if (utilisateurConnecte.getRole() == Role.GESTIONNAIRE) {
                            creerCommande(commandeDao, utilisateurConnecte, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux gestionnaires.");
                        }
                        break;
                    case 7:
                        if (utilisateurConnecte.getRole() == Role.GESTIONNAIRE) {
                            consulterCommandes(commandeDao, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux gestionnaires.");
                        }
                        break;
                    case 8:
                        if (utilisateurConnecte.getRole() == Role.GESTIONNAIRE) {
                            consulterStockCritique(medicamentDao, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux gestionnaires.");
                        }
                        break;
                    case 9:
                        if (utilisateurConnecte.getRole() == Role.GESTIONNAIRE) {
                            consulterHistoriqueStock(histDao, cx, sc);
                        } else {
                            System.out.println("Accès refusé : option réservée aux gestionnaires.");
                        }
                        break;
                    case 0:
                        quitter = true;
                        break;
                    default:
                        System.out.println("Choix invalide.");
                }
            }

        } catch (SQLException ex) {
            System.err.println("Erreur JDBC / SQL : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (cx != null) cx.close();
            } catch (SQLException ex) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + ex.getMessage());
            }
            sc.close();
        }
    }

    // ================== Méthodes utilitaires ==================

    private static void listerMedicaments(MedicamentDAO dao, Connection cx) throws SQLException {
        List<Medicament> meds = dao.findAll(cx);
        System.out.println("=== Liste des médicaments ===");
        if (meds.isEmpty()) {
            System.out.println("Aucun médicament trouvé.");
        } else {
            for (Medicament m : meds) {
                System.out.println(m);
            }
        }
    }

    private static void ajouterMedicament(MedicamentDAO dao, Connection cx, Scanner sc) throws SQLException {
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("Dosage : ");
        String dosage = sc.nextLine();
        System.out.print("Stock initial : ");
        int stock = sc.nextInt();
        System.out.print("Prix unitaire : ");
        double prix = sc.nextDouble();
        sc.nextLine();

        Medicament m = new Medicament(0, nom, dosage, stock, prix);
        dao.insert(cx, m);

        System.out.println("Médicament ajouté avec succès.");
    }

    private static void modifierStock(MedicamentDAO dao, Connection cx, Scanner sc) throws SQLException {
        System.out.print("ID du médicament : ");
        int id = sc.nextInt();
        System.out.print("Nouveau stock : ");
        int newStock = sc.nextInt();
        sc.nextLine();

        dao.updateStock(cx, id, newStock);
        System.out.println("Stock mis à jour.");
    }

    private static void enregistrerVente(VenteDAO venteDao, Utilisateur utilisateurConnecte,
                                         Connection cx, Scanner sc) throws SQLException {

        System.out.println("=== Enregistrer une vente ===");

        System.out.print("ID du client : ");
        int idClient = sc.nextInt();

        System.out.print("ID du médicament : ");
        int idMedicament = sc.nextInt();

        System.out.print("Quantité vendue : ");
        int quantite = sc.nextInt();
        sc.nextLine();

        LocalDateTime maintenant = LocalDateTime.now();

        Vente vente = new Vente(
                0,
                utilisateurConnecte.getId(),
                idClient,
                idMedicament,
                quantite,
                maintenant
        );

        venteDao.enregistrerVente(cx, vente);

        System.out.println("Vente enregistrée avec succès.");
    }

    private static void consulterVentes(VenteDAO venteDao, Connection cx, Scanner sc) throws SQLException {

        System.out.println("\n=== Consultation des ventes ===");
        System.out.println("1. Toutes les ventes");
        System.out.println("2. Par client");
        System.out.println("3. Par médicament");
        System.out.print("Votre choix : ");

        int choix = sc.nextInt();
        sc.nextLine();

        List<Vente> ventes;

        switch (choix) {
            case 1:
                ventes = venteDao.findAll(cx);
                break;
            case 2:
                System.out.print("ID du client : ");
                int idClient = sc.nextInt();
                sc.nextLine();
                ventes = venteDao.findByClient(cx, idClient);
                break;
            case 3:
                System.out.print("ID du médicament : ");
                int idMedicament = sc.nextInt();
                sc.nextLine();
                ventes = venteDao.findByMedicament(cx, idMedicament);
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }

        System.out.println("=== Résultat des ventes ===");
        if (ventes.isEmpty()) {
            System.out.println("Aucune vente trouvée.");
        } else {
            for (Vente v : ventes) {
                System.out.println(v);
            }
        }
    }

    private static void creerCommande(CommandeDAO commandeDao, Utilisateur utilisateurConnecte,
                                      Connection cx, Scanner sc) throws SQLException {

        System.out.println("=== Créer une commande ===");

        System.out.print("ID du médicament : ");
        int idMedicament = sc.nextInt();

        System.out.print("Quantité à commander : ");
        int quantite = sc.nextInt();
        sc.nextLine();

        LocalDateTime maintenant = LocalDateTime.now();

        Commande commande = new Commande(
                0,
                utilisateurConnecte.getId(),
                idMedicament,
                quantite,
                maintenant
        );

        commandeDao.creerCommande(cx, commande);

        System.out.println("Commande créée avec succès.");
    }

    private static void consulterCommandes(CommandeDAO commandeDao, Connection cx, Scanner sc) throws SQLException {

        System.out.println("\n=== Consultation des commandes ===");
        System.out.println("1. Toutes les commandes");
        System.out.println("2. Par médicament");
        System.out.print("Votre choix : ");

        int choix = sc.nextInt();
        sc.nextLine();

        List<Commande> commandes;

        switch (choix) {
            case 1:
                commandes = commandeDao.findAll(cx);
                break;
            case 2:
                System.out.print("ID du médicament : ");
                int idMedicament = sc.nextInt();
                sc.nextLine();
                commandes = commandeDao.findByMedicament(cx, idMedicament);
                break;
            default:
                System.out.println("Choix invalide.");
                return;
        }

        System.out.println("=== Résultat des commandes ===");
        if (commandes.isEmpty()) {
            System.out.println("Aucune commande trouvée.");
        } else {
            for (Commande c : commandes) {
                System.out.println(c);
            }
        }
    }

    private static void consulterStockCritique(MedicamentDAO medicamentDao,
                                               Connection cx, Scanner sc) throws SQLException {

        System.out.print("Seuil de stock critique (ex : 5) : ");
        int seuil = sc.nextInt();
        sc.nextLine();

        List<Medicament> meds = medicamentDao.findStockCritique(cx, seuil);

        System.out.println("=== Médicaments en stock critique (stock < " + seuil + ") ===");
        if (meds.isEmpty()) {
            System.out.println("Aucun médicament en stock critique.");
        } else {
            for (Medicament m : meds) {
                System.out.println(m);
            }
        }
    }

    private static void consulterHistoriqueStock(StockHistoriqueDAO histDao,
                                                 Connection cx, Scanner sc) throws SQLException {

        System.out.print("ID du médicament : ");
        int idMedicament = sc.nextInt();
        sc.nextLine();

        List<StockHistorique> hist = histDao.findByMedicament(cx, idMedicament);

        System.out.println("=== Historique de stock pour le médicament " + idMedicament + " ===");
        if (hist.isEmpty()) {
            System.out.println("Aucune entrée d'historique trouvée.");
        } else {
            for (StockHistorique sh : hist) {
                System.out.println(sh);
            }
        }
    }
}
