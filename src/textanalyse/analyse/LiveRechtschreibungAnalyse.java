package textanalyse.analyse;

import textanalyse.analyse.rechtschreibung.RechtschreibungAnalyse;
import textanalyse.daten.TextAnalyse;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import java.awt.event.ActionEvent;

public final class LiveRechtschreibungAnalyse {
    private final TextAnalyse daten;
    private final Timer timer;

    public LiveRechtschreibungAnalyse(TextAnalyse daten) {
        this.daten = daten;
        timer = new Timer(3000, this::kontrollieren);
        timer.setRepeats(true);
    }

    private void kontrollieren(ActionEvent actionEvent) {
        daten.getDokument().setCharacterAttributes(0, daten.getDokument().getLength(), SimpleAttributeSet.EMPTY, true);
        RechtschreibungAnalyse rechtschreibungAnalyse = RechtschreibungAnalyse.fehlerFinden2(daten.getText());
        rechtschreibungAnalyse.graphischAusgeben(null, daten);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
