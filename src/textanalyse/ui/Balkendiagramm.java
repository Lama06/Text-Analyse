package textanalyse.ui;

import textanalyse.analyse.suche.SuchenAnalyse;
import textanalyse.daten.TextAnalyse;
import textanalyse.util.TextUtil;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public final class Balkendiagramm extends JComponent {
    private record Eintrag(String text, int wert) { }

    private static final int ZEILE_HÖHE = 40;
    private static final int BREITE_GESAMT = 400;
    private static final int BREITE_BALKEN = 300;

    private final List<Eintrag> daten;

    private float prozent = 0;
    private Timer timer;

    public Balkendiagramm(Map<String, Integer> daten, TextAnalyseFenster fenster, TextAnalyse text) {
        this.daten = daten.entrySet().stream().map(eintrag -> new Eintrag(eintrag.getKey(), eintrag.getValue()))
                .sorted(Comparator.comparing(Eintrag::wert).reversed()).toList();
        setPreferredSize(new Dimension(BREITE_GESAMT, ZEILE_HÖHE*daten.size()));

        animationStarten();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((e.getModifiersEx() | MouseEvent.SHIFT_DOWN_MASK) == e.getModifiersEx()) {
                    animationStarten();
                    return;
                }
                int zeile = e.getY() / ZEILE_HÖHE;
                SuchenAnalyse.wortSuchen(text.getText(), Balkendiagramm.this.daten.get(zeile).text).graphischAusgeben(fenster, text);
            }
        });
    }

    private void animationStarten() {
        if (timer != null) return;
        prozent = 0;
        timer = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prozent += 0.1f * (1 - prozent);
                if (1 - prozent < 0.00001) {
                    prozent = 1;
                    timer.stop();
                    timer = null;
                }
                repaint();
            }
        });
        timer.start();
    }

    private int maximalerWert() {
        return daten.stream().mapToInt(Eintrag::wert).max().orElseThrow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setBackground(Color.WHITE);
        int i = 0;
        for (Eintrag e : daten) {
            int y = ZEILE_HÖHE*i;
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(TextUtil.großschreiben(e.text), 10, y+ZEILE_HÖHE/2);
            graphics2D.setColor(Color.BLACK);
            graphics2D.setPaint(Color.RED);
            graphics2D.fillRect(BREITE_GESAMT-BREITE_BALKEN, y+5, (int) (prozent*BREITE_BALKEN*((float) e.wert/maximalerWert())), ZEILE_HÖHE - 10);
            i++;
        }
    }
}
