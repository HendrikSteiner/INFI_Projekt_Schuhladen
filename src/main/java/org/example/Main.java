package org.example;

import java.sql.Connection;
import java.sql.SQLException;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main
{
    public static Connection c = null;

    public static void main(String[] args) throws SQLException, ClassNotFoundException
    {
        try {
            c = Verbindung.erstelleDatenbank();
            Kunde.erstelleTabelleKunde(c);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        } finally {
            closeResources();
        }
       
    }

    public static void closeResources()
    {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}