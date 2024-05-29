package org.example;

import java.sql.*;

public class Lager
{
    public static void erstelleTabelleLager(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS lager (" +
                "ID INTEGER PRIMARY KEY, " +
                "schuhID INTEGER REFERENCES schuhe(ID), " +
                "menge INTEGER, " +
                "eingangsdatum DATE)";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Lager table created successfully");
    }

    public static void lagerbestandanzeigen(Connection c) throws SQLException {
        String querySQL = "SELECT l.ID, l.menge, l.eingangsdatum, s.marke, s.modell, s.schuhgroesse, s.farbe, s.preis " +
                "FROM lager l " +
                "JOIN schuhe s ON l.schuhID = s.ID";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(querySQL);

        while (rs.next()) {
            int id = rs.getInt("ID");
            int menge = rs.getInt("menge");
            Date eingangsdatum = rs.getDate("eingangsdatum");
            String marke = rs.getString("marke");
            String modell = rs.getString("modell");
            int schuhgroesse = rs.getInt("schuhgroesse");
            String farbe = rs.getString("farbe");
            double preis = rs.getDouble("preis");

            System.out.printf("ID: %d, Menge: %d, Eingangsdatum: %s, Marke: %s, Modell: %s, Schuhgröße: %d, Farbe: %s, Preis: %.2f%n",
                    id, menge, eingangsdatum, marke, modell, schuhgroesse, farbe, preis);
        }
    }
    public static void erhoeheLagerbestand(int schuhId, int anzahl, Connection c) throws SQLException {
        String checkSchuhSQL = "SELECT * FROM lager WHERE schuhID = ?";
        String updateLagerSQL = "UPDATE lager SET menge = menge + ? WHERE schuhID = ?";
        String insertLagerSQL = "INSERT INTO lager (schuhID, menge) VALUES (?, ?)";

        try (PreparedStatement checkStmt = c.prepareStatement(checkSchuhSQL)) {
            checkStmt.setInt(1, schuhId);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next()) {
                try (PreparedStatement updateStmt = c.prepareStatement(updateLagerSQL)) {
                    updateStmt.setInt(1, anzahl);
                    updateStmt.setInt(2, schuhId);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = c.prepareStatement(insertLagerSQL)) {
                    insertStmt.setInt(1, schuhId);
                    insertStmt.setInt(2, anzahl);
                    insertStmt.executeUpdate();
                }
            }
        }
    }

}
