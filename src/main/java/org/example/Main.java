package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;


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
            Lager.erstelleTabelleLager(c);
            Bestellung.erstelleTabelleBestellungen(c);
            Schuhe.erstelleTabelleSchuhe(c);
            befuellen();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(1);
        } finally {
            closeResources();
        }
    }

    public static void befuellen() throws SQLException, ClassNotFoundException, IOException {
        while(true)
        {
            Scanner scanner = new Scanner(System.in);

            System.out.println("###################################################################################################################################################################################################################################");
            System.out.println("| add Kunde [k] | add Schuh [s] | bestellen [b] | Bestellung anzeigen | Lagerbestand [lb] | import from csv [i] |  export from csv [ex] || exit [e] |");
            System.out.println("###################################################################################################################################################################################################################################");
            System.out.print("Choose an Action: ");
            String input = scanner.nextLine();

            switch (input) {
                case "k": {
                    try {
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        Kunde.werteEintragen(name, email, c);
                    } catch (NumberFormatException e) {
                        System.out.println("Fehler beim Eintragen des Kundens: " + e.getMessage());
                    }
                    break;
                }
                case "s":
                {
                    try {
                        System.out.print("Marke: ");
                        String marke = scanner.nextLine();
                        System.out.print("Modell: ");
                        String modell = scanner.nextLine();
                        System.out.print("Schuhgröße: ");
                        int schuhgroesse = Integer.parseInt(scanner.nextLine());
                        System.out.print("Preis: ");
                        double preis = Double.parseDouble(scanner.nextLine());
                        System.out.print("Menge: ");
                        int menge = Integer.parseInt(scanner.nextLine());
                        System.out.print("Farbe: ");
                        String farbe = scanner.nextLine();

                        Schuhe.werteEintragen(marke, modell, schuhgroesse, preis, menge, farbe, c);
                    } catch (NumberFormatException e) {
                        System.out.println("Fehler beim Eintragen der Schuhe: " + e.getMessage());
                    }
                    break;
                }
                case "b":
                {
                    Bestellung.bestellen(c);
                }
                case "ba": {
                    Bestellung.bestellungAnzeigen(c);
                    break;
                }
                case "lb":{
                    Lager.lagerbestandanzeigen(c);
                    break;
                }
                case "e": {
                    System.exit(0);
                    break;
                }

                default:
                    System.out.println("Falsche Eingabe");
            }
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