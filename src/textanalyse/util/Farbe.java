package textanalyse.util;

public enum Farbe {
    STANDARD("\u001B[0m"),
    BLAU("\u001B[34m"),
    GELB("\u001B[33m"),
    ROT("\u001B[31m");

    public static boolean aktiviert;

    private final String text;

    Farbe(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        if (!aktiviert) return "";
        return text;
    }
}
