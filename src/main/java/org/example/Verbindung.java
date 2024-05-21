package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Verbindung
{
    private static final String URL = "jdbc:mysql:schuhladen.db";

    public static Connection erstelleDatenbank() throws SQLException, ClassNotFoundException {
        Connection c = null;

        c = DriverManager.getConnection(URL);
        System.out.println("Verbindung zur Datenbank hergestellt.");
        return c;
    }
}
