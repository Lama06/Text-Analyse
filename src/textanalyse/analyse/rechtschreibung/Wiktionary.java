package textanalyse.analyse.rechtschreibung;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * Sendet HTTP-Anfragen an das Wiktionary
 */
public final class Wiktionary {
    private final HttpClient http = HttpClient.newHttpClient();

    /**
     * Überprüft, ob ein Wort richtig geschrieben ist, indem versucht wird, es im Wiktionary zu finden
     * @param wort das zu überprüfende Wort
     * @return ob das Wort richtig geschrieben ist
     * @throws IOException falls die HTTP-Anfrage scheitert
     */
    public boolean istRichtigGeschrieben(String wort) throws IOException {
        HttpRequest anfrage = HttpRequest.newBuilder()
                .GET().uri(URI.create("https://de.wiktionary.org/wiki/" + wort))
                .build();
        try {
            return http.send(anfrage, HttpResponse.BodyHandlers.discarding()).statusCode() == 200;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Überprüft die Rechtschreibung in die Konsole eingegebener Wörter
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Wiktionary wiktionary = new Wiktionary();
        while (true) {
            String eingabe = scanner.nextLine();
            try {
                System.out.println(wiktionary.istRichtigGeschrieben(eingabe) ? "Richtig" : "Falsch");
            } catch (IOException e) {
                System.err.println("Fehler");
            }
        }
    }
}
