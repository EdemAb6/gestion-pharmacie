package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//  Classe utilitaire pour la connexion JDBC à MySQL.
 
public class DbConnection {

    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String URL = System.getenv().getOrDefault(
            "PHARMACIE_DB_URL",
            "jdbc:mysql://localhost:3306/pharmacie_db?serverTimezone=UTC"
    );
    private static final String USER = System.getenv().getOrDefault(
            "PHARMACIE_DB_USER",
            "root"
    );
    private static final String PASSWORD = requiredEnvironmentVariable(
            "PHARMACIE_DB_PASSWORD"
    );

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "La variable d'environnement " + name + " est obligatoire."
            );
        }
        return value;
    }

    
    static {
        try {
          
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement du driver JDBC : " + e.getMessage());
        }
    }

   
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
