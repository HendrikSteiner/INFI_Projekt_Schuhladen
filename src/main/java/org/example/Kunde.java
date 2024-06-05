package org.example;

import java.sql.*;

public class Kunde {
    public static void erstelleTabelleKunde(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS kunden " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT ," +
                " name char(30), " +
                " email char(50), " +
                " kontostand DECIMAL(10, 2))";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Kunde table created successfully");
    }

    public static void werteEintragen(String name, String email, double kontostand, Connection c) throws SQLException {
        String sql = "INSERT INTO kunden (name, email, kontostand) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setDouble(3, kontostand);
            pstmt.executeUpdate();
            System.out.println("Werte erfolgreich eingetragen!");
        }
    }
    public static void erhoeheKontostand(int kundenId, double betrag, Connection c) throws SQLException {
        String sql = "UPDATE kunden SET kontostand = kontostand + ? WHERE ID = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setDouble(1, betrag);
            pstmt.setInt(2, kundenId);
            pstmt.executeUpdate();
            System.out.println("Kontostand erhöht.");
        } catch (SQLException e) {
            System.out.println("Fehler beim Erhöhen des Kontostands: " + e.getMessage());
        }
    }

    public static void verringereKontostand(int kundenId, double betrag, Connection c) throws SQLException {
        String sql = "UPDATE kunden SET kontostand = kontostand - ? WHERE ID = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setDouble(1, betrag);
            pstmt.setInt(2, kundenId);
            pstmt.executeUpdate();
            System.out.println("Kontostand verringert.");
        } catch (SQLException e) {
            System.out.println("Fehler beim Verringern des Kontostands: " + e.getMessage());
        }
    }

    public static double getKontostand(int kundenId, Connection c) throws SQLException {
        String sql = "SELECT kontostand FROM kunden WHERE ID = ?";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setInt(1, kundenId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("kontostand");
            }
        }
        return 0.0;
    }

    public static void kundenAnzeigen(Connection c)
    {
        try {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM kunden");
            System.out.println("Kunden-Tabelle:");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String kundenName = resultSet.getString("name");
                String kundenEmail = resultSet.getString("email");
                System.out.println("KundenID: " + id + ", Name: " + kundenName + ", Email: " + kundenEmail);
            }
        }catch (SQLException e) {
                System.out.println("Fehler beim anzeigen der Kunden: " + e.getMessage());
            }
    }
}
