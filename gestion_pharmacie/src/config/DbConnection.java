package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//  Classe utilitaire pour la connexion JDBC à MySQL.
 
public class DbConnection {

    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

   
    private static final String URL = "jdbc:mysql://localhost:3306/pharmacie_db?serverTimezone=UTC";

   
    private static final String USER = "root";
    private static final String PASSWORD = "123456789Sherlocked";

    
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
