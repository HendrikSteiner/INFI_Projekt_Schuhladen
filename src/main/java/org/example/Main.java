package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

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
        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("###################################################################################################################################################################################################################################");
            System.out.println("| add Kunde [k] | add Schuh [s] | kaufen [buy] | verkaufen [sell] | Bestellung anzeigen [ba] | Lagerbestand [lb] | import from json [ij] | export from json [ej] || exit [e] |");
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
                        System.out.println("Kontostand: ");
                        double kontostand = Double.parseDouble(scanner.nextLine());
                        Kunde.werteEintragen(name, email, kontostand, c);
                    } catch (NumberFormatException e) {
                        System.out.println("Fehler beim Eintragen des Kundens: " + e.getMessage());
                    }
                    break;
                }
                case "s": {
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
                case "buy": {
                    Bestellung.bestellen(c);
                    break;
                }
                case "sell":
                {
                    System.out.println("Schönen guten Tag was wollen Sie verkaufen?");
                    System.out.println("Welcher Kunde will etwas verkaufen? KundenID: ");
                    Kunde.kundenAnzeigen(c);
                    int kundenID = Integer.parseInt(scanner.nextLine());
                    double derzeitiger_kontostand = Kunde.getKontostand(kundenID, c);
                    System.out.println("Derzeitiger Kontostand des Kunden: "+derzeitiger_kontostand);
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
                    double rechnung = preis * menge;
                    Kunde.erhoeheKontostand(kundenID, rechnung, c);

                    double neuer_kontostand = Kunde.getKontostand(kundenID, c);
                    System.out.println("Neuer Kontostand des Kunden: "+ neuer_kontostand);
                    break;
                }
                case "ba": {
                    Schuhe.zeigeSchuhe(c);
                    Bestellung.bestellungAnzeigen(c);
                    break;
                }
                case "lb": {
                    Lager.lagerbestandanzeigen(c);
                    break;
                }
                case "ij": {
                    System.out.print("Bitte geben Sie den Pfad zur JSON-Datei ein: ");
                    String filePath = scanner.nextLine();
                    JSONImport.importFromJSON(filePath, c);
                    break;
                }
                case "ej":
                {
                    System.out.print("Bitte geben Sie den Pfad zur JSON-Datei ein: ");
                    String filePath = scanner.nextLine();
                    JSONExport.exportToJSON(c,filePath);
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