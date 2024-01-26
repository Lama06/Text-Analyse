package textanalyse.util;

public record Wort(String inhalt, int zeile, int anfang, String kontext) {
    @Override
    public String toString() {
        return inhalt;
    }
}
