package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JSONExport {

    public static void exportToJSON(Connection connection, String filePath) {
        try {
            JSONArray jsonArray = ShoesAsJSON(connection);
            writeJSONToFile(jsonArray, filePath);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray ShoesAsJSON(Connection connection) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT * FROM schuhe";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            while (resultSet.next()) {
                JSONObject shoeJson = new JSONObject();
                shoeJson.put("marke", resultSet.getString("marke"));
                shoeJson.put("modell", resultSet.getString("modell"));
                shoeJson.put("schuhgroesse", resultSet.getInt("schuhgroesse"));
                shoeJson.put("preis", resultSet.getDouble("preis"));
                shoeJson.put("menge", resultSet.getInt("menge"));
                shoeJson.put("farbe", resultSet.getString("farbe"));
                jsonArray.put(shoeJson);
            }
        }
        return jsonArray;
    }

    private static void writeJSONToFile(JSONArray jsonArray, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toString(4));
        }
    }
}
