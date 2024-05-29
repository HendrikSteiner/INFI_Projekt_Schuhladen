package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Schuhe
{
    public static void erstelleTabelleSchuhe(Connection c) throws SQLException
    {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS schuhe (" +
                "ID INT AUTO_INCREMENT PRIMARY KEY , " +
                "menge INTEGER, " +
                "marke char(50), " +
                "modell char(50), " +
                "schuhgroesse INTEGER, " +
                "preis DOUBLE, " +
                "farbe char(50))";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Schuhe table created successfully");
    }

    public static void werteEintragen(String marke, String modell, int schuhgroesse, double preis, int menge, String farbe, Connection c) throws SQLException {
        String sql = "INSERT INTO schuhe (marke, modell, schuhgroesse, preis, menge, farbe) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, marke);
            pstmt.setString(2, modell);
            pstmt.setInt(3, schuhgroesse);
            pstmt.setDouble(4, preis);
            pstmt.setInt(5, menge);
            pstmt.setString(6, farbe);
            pstmt.executeUpdate();
            System.out.println("Schuhe erfolgreich eingetragen!");
        }
    }
}
