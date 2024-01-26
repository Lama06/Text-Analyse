package textanalyse.ui;

import textanalyse.analyse.*;
import textanalyse.analyse.anapher.AnaphernAnalyse;
import textanalyse.analyse.rechtschreibung.RechtschreibungAnalyse;
import textanalyse.analyse.suche.SuchenAnalyse;
import textanalyse.daten.TextAnalyse;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class TextAnalyseFenster {
    private final TextAnalyse daten;
    private final JFrame fenster;
    private final LiveRechtschreibungAnalyse liveRechtschreibungAnalyse;

    public TextAnalyseFenster(TextAnalyse daten) {
        this.daten = daten;
        TextEingabe textView = new TextEingabe(daten);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setViewportView(textView.getWidget());

        fenster = new JFrame();
        fenster.setTitle("Text-Analyse");
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenster.setSize(500, 500);
        fenster.setContentPane(scrollPane);
        fenster.setJMenuBar(menuBarErstellen());
        fenster.setVisible(true);

        liveRechtschreibungAnalyse = new LiveRechtschreibungAnalyse(daten);
    }

    private JMenuBar menuBarErstellen() {
        JMenuItem anaphern = new JMenuItem();
        anaphern.setText("Anaphern finden");
        anaphern.addActionListener(e -> {
            AnaphernAnalyse.anaphernFinden(daten.getText()).graphischAusgeben(this, daten);

        });

        JMenuItem rechtschreibung = new JMenuItem();
        rechtschreibung.setText("Rechtschreibung prüfen");
        rechtschreibung.addActionListener(e -> {
            RechtschreibungAnalyse.fehlerFinden2(daten.getText()).graphischAusgeben(this, daten);
        });

        JCheckBoxMenuItem liveRechtschreibung = new JCheckBoxMenuItem();
        liveRechtschreibung.setText("Live-Rechtschreibprüfung");
        liveRechtschreibung.setState(false);
        liveRechtschreibung.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (liveRechtschreibung.getState()) {
                    liveRechtschreibungAnalyse.start();
                } else {
                    liveRechtschreibungAnalyse.stop();
                }
            }
        });

        JMenuItem suche = new JMenuItem();
        suche.setText("Suche");
        suche.addActionListener(e -> {
            String suchwort = JOptionPane.showInputDialog("Suchbegriff");
            SuchenAnalyse.wortSuchen(daten.getText(), suchwort).graphischAusgeben(this, daten);
        });

        JMenu stat = new JMenu();
        stat.setText("Statistik");

        JMenuItem satzarten = new JMenuItem();
        satzarten.setText("Satzarten");
        satzarten.addActionListener(e -> {
            SatzartenAnalyse.satzartenZählen(daten.getText()).graphischAusgeben(this, daten);
        });
        stat.add(satzarten);

        JMenuItem wörterStatikstik = new JMenuItem();
        wörterStatikstik.setText("Wörter-Statistik");
        wörterStatikstik.addActionListener(e -> {
            WörterHäufigkeitsAnalyse.wörterZählen(daten.getText()).graphischAusgeben(this, daten);
        });
        stat.add(wörterStatikstik);

        JMenuItem buchstabenStatikstik = new JMenuItem();
        buchstabenStatikstik.setText("Buchstaben-Statisktik");
        buchstabenStatikstik.addActionListener(e -> {
            BuchstabenHäufigkeitAnalyse.buchstabenZählen(daten.getText()).graphischAusgeben(this, daten);
        });
        stat.add(buchstabenStatikstik);

        JMenuItem wortlänge = new JMenuItem();
        wortlänge.setText("Wört- und Satzlängen");
        wortlänge.addActionListener(e ->
                WortBuchstabenAnzahlAnalyse.wörterUndBuchstabenZählen(daten.getText()).graphischAusgeben(this, daten));
        stat.add(wortlänge);

        JMenuItem langeKurzeSätze = new JMenuItem();
        langeKurzeSätze.setText("Lange und kurze Sätze");
        langeKurzeSätze.addActionListener(e -> new LangeKurzeSätzeAnalyse(this, daten));
        stat.add(langeKurzeSätze);

        JMenuItem schlüsselwörter = new JMenuItem();
        schlüsselwörter.setText("Schlüsselwörter");
        schlüsselwörter.addActionListener(_ -> new SchlüsselwörterAnalyse(fenster, daten));
        stat.add(schlüsselwörter);

        JMenuItem reset = new JMenuItem();
        reset.setText("Markierungen entfernen");
        reset.addActionListener(e -> {
            daten.getDokument().setCharacterAttributes(0, daten.getDokument().getLength(), SimpleAttributeSet.EMPTY, true);
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Analysieren");
        menu.add(anaphern);
        menu.add(rechtschreibung);
        menu.add(liveRechtschreibung);
        menu.add(suche);
        menu.addSeparator();
        menu.add(stat);
        menu.addSeparator();
        menu.add(reset);
        menuBar.add(menu);

        JMenu einstellungen = new JMenu();
        einstellungen.setText("Einstellugen");
        JMenuItem einstellungenÖffnen = new JMenuItem();
        einstellungenÖffnen.setText("Öffnen");
        einstellungenÖffnen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EinstellungenFenster(TextAnalyseFenster.this, daten);
            }
        });
        einstellungen.add(einstellungenÖffnen);
        menuBar.add(einstellungen);

        JMenu datei = new JMenu();
        datei.setText("Datei");

        JMenuItem öffnen = new JMenuItem();
        öffnen.setText("Öffnen");
        öffnen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int status = fileChooser.showOpenDialog(fenster);
                if (status != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File selected = fileChooser.getSelectedFile();
                String inhalt;
                try {
                    inhalt = Files.readString(selected.toPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(fenster, ex.getMessage());
                    return;
                }
                daten.setText(inhalt);
            }
        });
        datei.add(öffnen);

        JMenuItem speichern = new JMenuItem();
        speichern.setText("Speichern");
        speichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                int status = jFileChooser.showSaveDialog(fenster);
                if (status != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File selectedFile = jFileChooser.getSelectedFile();
                try {
                    Files.writeString(selectedFile.toPath(), daten.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(fenster, ex.getMessage());
                }
            }
        });
        datei.add(speichern);
        menuBar.add(datei);
        return menuBar;
    }

    public JFrame getFenster() {
        return fenster;
    }
}
