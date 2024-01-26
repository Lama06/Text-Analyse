package textanalyse.analyse;

public record SatzartenAnalyse(int aussagen, int fragen, int ausrufe) implements Analyse {
    public static SatzartenAnalyse satzartenZählen(String text) {
        int punktAmEnde = text.endsWith(".") ? 1 : 0;
        int fragezeichenAmEnde = text.endsWith("?") ? 1 : 0;
        int ausrufezeichenAmEnde = text.endsWith("!") ? 1 : 0;
        return new SatzartenAnalyse(
                text.split("\\.").length-1 + punktAmEnde,
                text.split("\\?").length-1 + fragezeichenAmEnde,
                text.split("!").length-1 + ausrufezeichenAmEnde
        );
    }

    @Override
    public String getKonsolenAusgabe() {
        return """
            Aussagesätze: %s
            Fragen:       %s
            Ausrufe:      %s
            """.formatted(aussagen, fragen, ausrufe);
    }
}
