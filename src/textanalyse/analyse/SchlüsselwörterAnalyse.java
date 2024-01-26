package textanalyse.analyse;

import textanalyse.daten.TextAnalyse;
import textanalyse.util.TextUtil;
import textanalyse.util.Wort;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ein Fenster, das eine nach Häufigkeit sortierte Liste aller Wörter anzeigt, bei denen es sich nicht
 * um Füllwörter oder andere häufig benutzte handelt.
 */
public final class SchlüsselwörterAnalyse {
    /**
     * Liste der häufigsten deutschen Wörter
     */
    private static final List<String> wortliste;
    static {
        URL url = SchlüsselwörterAnalyse.class.getResource("wortliste.txt");
        if (url == null) {
            throw new RuntimeException();
        }
        try {
            String inhalt = Files.readString(Path.of(url.toURI()));
            wortliste = List.of(inhalt.split("\r\n"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final TextAnalyse daten;
    private final JDialog fenster;
    private final JList<String> schlüsselWörter;

    public SchlüsselwörterAnalyse(JFrame parent, TextAnalyse daten) {
        this.daten = daten;

        // Fenster erstellen
        fenster = new JDialog(parent);
        fenster.setTitle("Schlüsselwörter");
        JPanel content = new JPanel();
        fenster.setContentPane(content);
        BoxLayout layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);
        content.setLayout(layout);
        JLabel überschrift = new JLabel();
        content.add(überschrift);
        überschrift.setText("Schlüsselwörter");
        überschrift.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Wortliste zur UI hinzufügen
        schlüsselWörter = new JList<>();
        JScrollPane scrollPane = new JScrollPane();;
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setViewportView(schlüsselWörter);
        scrollPane.setPreferredSize(new Dimension(200, 700));
        content.add(scrollPane);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        listeAktualisieren();

        // Auf Änderung des Textes reagieren
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                listeAktualisieren();
            }
        };
        daten.getDokument().addDocumentListener(documentListener);
        fenster.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                daten.getDokument().removeDocumentListener(documentListener);
            }
        });

        fenster.setVisible(true);
    }

    /**
     * Aktualisiert die Liste der angezeigten Schlüsselwörter basierend auf dem aktuell eingegebenen Text.
     */
    private void listeAktualisieren() {
        String text = daten.getText();
        Map<String, Integer> schlüsselWörterAnzahlen = new HashMap<>();
        List<Wort> wörter = TextUtil.wörterExtrahieren(text, 0);
        for (Wort wort : wörter) {
            String inhalt = wort.inhalt();
            if (wortliste.stream().anyMatch(inhalt::equalsIgnoreCase)) {
                continue;
            }
            schlüsselWörterAnzahlen.compute(inhalt.toLowerCase(), (_, anzahl) -> anzahl == null ? 1 : anzahl+1);
        }
        schlüsselWörter.setListData(schlüsselWörterAnzahlen
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .map(TextUtil::großschreiben)
                .toArray(String[]::new));
        fenster.pack();
    }
}
