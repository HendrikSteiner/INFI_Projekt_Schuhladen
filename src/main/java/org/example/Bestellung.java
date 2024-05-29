package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;

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

        public static void bestellen(Connection c) {
            Scanner scanner = new Scanner(System.in);
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

                System.out.println("-----------------------------------------");
                System.out.println("Wer soll etwas bestellen?");
                System.out.print("Kunden ID: ");
                int kundenId = Integer.parseInt(scanner.nextLine());

                ResultSet resultSet2 = stmt.executeQuery("SELECT * FROM schuhe");
                System.out.println("Schuhe-Tabelle:");
                while (resultSet2.next()) {
                    int schuhId = resultSet2.getInt("ID");
                    String marke = resultSet2.getString("marke");
                    String modell = resultSet2.getString("modell");
                    double preis = resultSet2.getDouble("preis");
                    System.out.println("SchuhID: " + schuhId + ", Marke: " + marke + ", Modell: " + modell + ", Preis: " + preis);
                }

                System.out.println("-----------------------------------------");
                System.out.println("Was soll der Kunde " + kundenId + " bestellen?");
                System.out.print("Schuh ID: ");
                int schuhId = Integer.parseInt(scanner.nextLine());

                System.out.print("Anzahl: ");
                int anzahl = Integer.parseInt(scanner.nextLine());

                if (istSchuhVerfuegbar(schuhId, anzahl, c)) {
                    String sql = "INSERT INTO bestellung (kundenId, schuhid, anzahl, bestellzeit) VALUES (?, ?, ?, NOW())";
                    try (PreparedStatement pstmt = c.prepareStatement(sql)) {
                        pstmt.setInt(1, kundenId);
                        pstmt.setInt(2, schuhId);
                        pstmt.setInt(3, anzahl);
                        pstmt.executeUpdate();
                    }
                    Lager.erhoeheLagerbestand(schuhId, anzahl, c); //bringt Probleme
                    System.out.println("Bestellung erfolgreich eingetragen.");
                } else {
                    System.out.println("Fehler: Schuh nicht auf Lager oder nicht ausreichende Menge verfügbar.");
                }
            } catch (NumberFormatException | SQLException e) {
                System.out.println("Fehler beim Eintragen der Bestellung: " + e.getMessage());
            }
        }

        private static boolean istSchuhVerfuegbar(int schuhId, int anzahl, Connection c) throws SQLException {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT menge FROM lager WHERE schuhID = " + schuhId);

            if (resultSet.next()) {
                int lagerMenge = resultSet.getInt("menge");
                return lagerMenge >= anzahl;
            }
            return false;
        }

     /*   ----------------------------------------------------------------------------------------------------------------------*/

    public static void selectFromBestellungen(Connection c, String kundenId) {
        try {
            String sql = "SELECT * FROM bestellung WHERE kundenId = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, kundenId);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int kundenIdResult = resultSet.getInt("kundenId");
                int schuhid = resultSet.getInt("schuhid");
                int anzahl = resultSet.getInt("anzahl");
                Timestamp bestellzeit = resultSet.getTimestamp("bestellzeit");
                System.out.println("Der Kunden ID: " + kundenIdResult +
                        ", hat den Schuh mit der Schuh ID: " + schuhid +
                        ", so oft bestellt: " + anzahl +
                        ", Bestellzeit: " + bestellzeit);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bestellungAnzeigen(Connection c) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        try {
            Statement stmt = c.createStatement();
            String a3 = "SELECT * FROM kunden";
            ResultSet resultSet = stmt.executeQuery(a3);
            System.out.println("Kunden-Tabelle:");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String kundenName = resultSet.getString("name");
                String kundenEmail = resultSet.getString("email");
                System.out.println("KundenID: " + id + ", Name: " + kundenName + ", Email: " + kundenEmail);
            }
            System.out.print("Kunden ID: ");
            String kundenId = scanner.nextLine();
            selectFromBestellungen(c, kundenId);
        } catch (SQLException e) {
            System.out.println("Fehler beim Anzeigen der Bestellungen: " + e.getMessage());
        }
    }

}
