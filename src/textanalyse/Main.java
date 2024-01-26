package textanalyse;

import textanalyse.analyse.*;
import textanalyse.analyse.anapher.AnaphernAnalyse;
import textanalyse.analyse.rechtschreibung.RechtschreibungAnalyse;
import textanalyse.analyse.suche.SuchenAnalyse;
import textanalyse.util.Farbe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    private static String dateiLaden(String pfad) throws IOException {
        return Files.readString(Path.of(pfad));
    }

    public static void main(String[] args) throws IOException {
        Farbe.aktiviert = true;
        String text = dateiLaden("text.txt");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-".repeat(20));
            System.out.println(Farbe.GELB + "Text-Analyse" + Farbe.STANDARD);
            System.out.println("-".repeat(20));
            System.out.println(Farbe.BLAU + "1: Wörter zählen");
            System.out.println("2: Buchstaben zählen");
            System.out.println("3: Wort suchen");
            System.out.println("4: Satzarten zählen");
            System.out.println("5: Durschnittliche Wörter- und Satzlängen");
            System.out.println("6: Anaphern zählen");
            System.out.println("7, 8: Rechtschreibung prüfen" + Farbe.STANDARD);
            System.out.println("-".repeat(20));
            System.out.print(Farbe.GELB + "Auswahl: " + Farbe.STANDARD);

            Analyse analyse = switch (scanner.nextLine()) {
                case "1" -> WörterHäufigkeitsAnalyse.wörterZählen(text);
                case "2" -> BuchstabenHäufigkeitAnalyse.buchstabenZählen(text);
                case "3" -> {
                    System.out.print(Farbe.GELB + "Suchwort: " + Farbe.STANDARD);
                    String suchwort = scanner.nextLine();
                    yield SuchenAnalyse.wortSuchen(text, suchwort);
                }
                case "4" -> SatzartenAnalyse.satzartenZählen(text);
                case "5" -> WortBuchstabenAnzahlAnalyse.wörterUndBuchstabenZählen(text);
                case "6" -> AnaphernAnalyse.anaphernFinden(text);
                case "7" -> RechtschreibungAnalyse.fehlerFinden1(text);
                case "8" -> RechtschreibungAnalyse.fehlerFinden2(text);
                default -> {
                    System.out.println(Farbe.ROT + "Falsche Eingabe" + Farbe.STANDARD);
                    yield null;
                }
            };

            System.out.println("-".repeat(20));
            if (analyse == null) {
                continue;
            }
            System.out.println(analyse.getKonsolenAusgabe());
            scanner.nextLine();
        }
    }
}