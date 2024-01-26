package textanalyse.analyse;

import textanalyse.daten.TextAnalyse;
import textanalyse.ui.TextAnalyseFenster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class LangeKurzeSätzeAnalyse extends JFrame {
    private final JDialog fenster;
    private final TextAnalyse daten;
    private final JLabel nachricht;
    private final JList<Satz> langeSätzeListe = new JList<>();
    private final JList<Satz> kurzeSätzeListe = new JList<>();

    public LangeKurzeSätzeAnalyse(TextAnalyseFenster parent, TextAnalyse daten) {
        this.daten = daten;
        fenster = new JDialog(parent.getFenster());
        fenster.setTitle("Lange und kurze Sätze");
        fenster.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel();
        BoxLayout layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);
        content.setLayout(layout);
        fenster.setContentPane(content);

        nachricht = new JLabel();
        nachricht.setAlignmentX(0);
        content.add(nachricht);

        content.add(Box.createVerticalStrut(10));
        JLabel label1 = new JLabel("Lange Sätze:");
        content.add(label1);
        label1.setAlignmentX(0);
        content.add(langeSätzeListe);
        langeSätzeListe.setAlignmentX(0);
        langeSätzeListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        langeSätzeListe.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (langeSätzeListe.getSelectedValue() == null) {
                    return;
                }
                langeSätzeListe.getSelectedValue().markieren(daten);
            }
        });
        content.add(Box.createVerticalStrut(10));

        JLabel label2 = new JLabel("Kurze Sätze");
        label2.setAlignmentX(0);
        content.add(label2);
        content.add(kurzeSätzeListe);
        kurzeSätzeListe.setAlignmentX(0);
        kurzeSätzeListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        kurzeSätzeListe.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (kurzeSätzeListe.getSelectedValue() == null) {
                    return;
                }
                kurzeSätzeListe.getSelectedValue().markieren(daten);
            }
        });

        update();

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
                update();
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

    private void update() {
        String text = daten.getText();
        List<Satz> sätze = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (int position = 0; position < text.length(); position++) {
            if (text.charAt(position) == '.') {
                if (!builder.isEmpty()) {
                    sätze.add(new Satz(builder.toString(), position-builder.toString().length()));
                    builder.setLength(0);
                }
                continue;
            }
            builder.append(text.charAt(position));
        }
        if (!builder.isEmpty()) {
            sätze.add(new Satz(builder.toString(), text.length()-builder.toString().length()));
        }
        double durschnitt = sätze.stream().map(Satz::inhalt).mapToInt(String::length).summaryStatistics().getAverage();
        List<Satz> kurzeSätze = sätze.stream().filter(satz -> satz.inhalt.length() <= durschnitt).toList();
        List<Satz> langeSätze = sätze.stream().filter(satz -> satz.inhalt.length() > durschnitt).toList();
        langeSätzeListe.setListData(langeSätze.toArray(Satz[]::new));
        kurzeSätzeListe.setListData(kurzeSätze.toArray(Satz[]::new));
        nachricht.setText("Die durschnittliche Satzlänge ist %.1f. Der Text enthält %s kurze und %s lange Sätze"
                .formatted(durschnitt, kurzeSätze.size(), langeSätze.size()));
        fenster.pack();
    }

    private record Satz(String inhalt, int position) {
        @Override
        public String toString() {
            return inhalt;
        }

        private void markieren(TextAnalyse daten) {
            Element characterElement = daten.getDokument().getCharacterElement(position);
            Object stilName = characterElement.getAttributes().getAttribute(StyleConstants.NameAttribute);
            AttributeSet neuerStil;
            if (stilName instanceof String stilNameString && stilNameString.equals("Markierung")) {
                neuerStil = SimpleAttributeSet.EMPTY;
            } else {
                neuerStil = daten.getDokument().getStyle("Markierung");
            }
            daten.getDokument().setCharacterAttributes(position, inhalt.length(), neuerStil, true);
        }
    }
}
