package textanalyse.analyse;

public record WortBuchstabenAnzahlAnalyse(float worteproSatz, float buchstabenProWort) implements Analyse {
    public static WortBuchstabenAnzahlAnalyse wörterUndBuchstabenZählen(String text) {
        int buchstaben = text.length();
        int wörter = text.split(" ").length;
        int sätze = text.split("\\.|!|\\?").length;
        float durschnittWörterProSatz = (float) wörter / sätze;
        float durschnittBuchstabenProWort = (float) buchstaben / wörter;
        return new WortBuchstabenAnzahlAnalyse(durschnittWörterProSatz, durschnittBuchstabenProWort);
    }

    @Override
    public String getKonsolenAusgabe() {
        return """
                Jeder Satz hat im Durschnitt %s Wörter und jedes Wort %s Buchstaben
                """.formatted(worteproSatz, buchstabenProWort);
    }
}
