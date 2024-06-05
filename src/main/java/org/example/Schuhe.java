package org.example;

import java.sql.*;

public class Schuhe {
    public static void erstelleTabelleSchuhe(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS schuhe (" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "marke CHAR(50), " +
                "modell CHAR(50), " +
                "schuhgroesse INTEGER, " +
                "preis DOUBLE, " +
                "farbe CHAR(50))";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Schuhe table created successfully");
    }

    public static void werteEintragen(String marke, String modell, int schuhgroesse, double preis, int menge, String farbe, Connection c) throws SQLException {
        String sql = "INSERT INTO schuhe (marke, modell, schuhgroesse, preis, farbe) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, marke);
            pstmt.setString(2, modell);
            pstmt.setInt(3, schuhgroesse);
            pstmt.setDouble(4, preis);
            pstmt.setString(5, farbe);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int schuhId = generatedKeys.getInt(1);
                    Lager.erhoeheLagerbestand(schuhId, menge, c);
                } else {
                    throw new SQLException("Creating shoe failed, no ID obtained.");
                }
            }
            System.out.println("Schuhe erfolgreich eingetragen!");
        }
    }

    public static void zeigeSchuhe(Connection c) throws SQLException {
        String query = "SELECT * FROM schuhe";
        try (Statement stmt = c.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Schuhe-Tabelle:");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String marke = resultSet.getString("marke");
                String modell = resultSet.getString("modell");
                int schuhgroesse = resultSet.getInt("schuhgroesse");
                double preis = resultSet.getDouble("preis");
                String farbe = resultSet.getString("farbe");
                System.out.printf("SchuhID: %d, Marke: %s, Modell: %s, Schuhgröße: %d, Preis: %.2f, Farbe: %s%n",
                        id, marke, modell, schuhgroesse, preis, farbe);
            }
        }
    }

    public static double getSchuhPreis(int schuhId, Connection c) throws SQLException {
        String sql = "SELECT preis FROM schuhe WHERE id = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, schuhId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("preis");
            }
        }
        return 0.0;
    }

}
