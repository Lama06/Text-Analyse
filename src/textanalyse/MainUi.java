package textanalyse;

import com.formdev.flatlaf.FlatLightLaf;
import textanalyse.daten.TextAnalyse;
import textanalyse.util.Farbe;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainUi {
    private static String dateiLaden(String pfad) throws IOException {
        return Files.readString(Path.of(pfad));
    }

    public static void main(String[] args) throws IOException {
        Farbe.aktiviert = false;
        FlatLightLaf.setup();
        String text = dateiLaden("text.txt");
        SwingUtilities.invokeLater(() -> {
            TextAnalyse textAnalyse = new TextAnalyse(text);
            new TextAnalyseFenster(textAnalyse);
        });
    }
}
