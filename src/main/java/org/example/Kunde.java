package org.example;

import java.sql.*;

public class Kunde
{
    public static void erstelleTabelleKunde(Connection c) throws SQLException
    {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS kunden " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT ," +
                " name char(30), " +
                " email char(50))";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Kunde table created successfully");
    }
    public static void werteEintragen(String name, String email, Connection c) throws SQLException {
        String sql = "INSERT INTO kunden (name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = c.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Werte erfolgreich eingetragen!");
        }
    }

    public static void ausgabe(Connection c) throws SQLException {
        String query = "SELECT * FROM kunden";
        try (Statement stmt = c.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Kunden-Tabelle:");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String kundenName = resultSet.getString("name");
                String kundenEmail = resultSet.getString("email");
                System.out.println("KundenID: " + id + ", Name: " + kundenName + ", Email: " + kundenEmail);
            }
        }

    }
}
