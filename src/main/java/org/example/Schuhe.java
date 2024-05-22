package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Schuhe
{
    public static void erstelleTabelleSchuhe(Connection c) throws SQLException
    {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS schuhe (" +
                "ID INTEGER PRIMARY KEY, " +
                "menge INTEGER, " +
                "marke char(50), " +
                "modell char(50), " +
                "schuhgroesse INTEGER, " +
                "preis DOUBLE, " +
                "farbe char(50))";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Schuhe table created successfully");
    }
}
