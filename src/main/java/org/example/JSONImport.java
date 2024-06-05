package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JSONImport {

    public static void importFromJSON(String filePath, Connection connection) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray jsonArray = new JSONArray(tokener);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                insertIntoSchuhe(jsonObject, connection);
            }

            System.out.println("Import erfolgreich abgeschlossen!");

        } catch (IOException | SQLException e) {
            System.err.println("Fehler beim Importieren der Daten: " + e.getMessage());
        }
    }

    private static void insertIntoSchuhe(JSONObject jsonObject, Connection connection) throws SQLException {
        String marke = jsonObject.getString("marke");
        String modell = jsonObject.getString("modell");
        int schuhgroesse = jsonObject.getInt("schuhgroesse");
        double preis = jsonObject.getDouble("preis");
        int menge = jsonObject.getInt("menge");
        String farbe = jsonObject.getString("farbe");

        String schuheSQL = "INSERT INTO schuhe (marke, modell, schuhgroesse, preis, menge, farbe) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(schuheSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, marke);
            pstmt.setString(2, modell);
            pstmt.setInt(3, schuhgroesse);
            pstmt.setDouble(4, preis);
            pstmt.setInt(5, menge);
            pstmt.setString(6, farbe);
            pstmt.executeUpdate();

            try (var generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int schuhID = generatedKeys.getInt(1);
                    insertIntoLager(schuhID, menge, connection);
                }
            }
        }
    }

    private static void insertIntoLager(int schuhID, int menge, Connection connection) throws SQLException {
        String lagerSQL = "INSERT INTO lager (schuhID, menge, eingangsdatum) VALUES (?, ?, NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(lagerSQL)) {
            pstmt.setInt(1, schuhID);
            pstmt.setInt(2, menge);
            pstmt.executeUpdate();
        }
    }
}
