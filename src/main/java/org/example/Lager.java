package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Lager
{
    public static void erstelleTabelleLager(Connection c) throws SQLException
    {
        Statement stmt = c.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS lager " +
                "(schuh_id INTEGER PRIMARY KEY, " +
                " marke INTEGER)";
        stmt.executeUpdate(createTableSQL);
        System.out.println("Lager table created successfully");
    }
}
