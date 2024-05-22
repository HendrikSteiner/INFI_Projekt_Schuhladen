package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Bestellung
{

    public static void erstelleTabelleBestellungen(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String createBestellungTable = "CREATE TABLE IF NOT EXISTS bestellung (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "kundenId INT," +
                "schuhid INT," +
                "anzahl INT," +
                "bestellzeit TIMESTAMP," +
                "FOREIGN KEY (kundenId) REFERENCES kunden(id)," +
                "FOREIGN KEY (schuhid) REFERENCES schuhe(id)" +
                ");";
        stmt.executeUpdate(createBestellungTable);
        System.out.println("Bestellungen table created successfully");
    }
}
