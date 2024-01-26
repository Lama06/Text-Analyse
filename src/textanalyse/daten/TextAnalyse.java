package textanalyse.daten;

import javax.swing.text.*;
import java.awt.*;

public final class TextAnalyse {
    private final StyledDocument dokument = new DefaultStyledDocument();

    public TextAnalyse(String text) {
        Style normalerStil = dokument.addStyle("Normal", null);
        StyleConstants.setFontSize(normalerStil, 20);

        Style rot = dokument.addStyle("Markierung", normalerStil);
        StyleConstants.setBold(rot, true);
        StyleConstants.setForeground(rot, Color.RED);
        StyleConstants.setBackground(rot, Color.GRAY);

        try {
            dokument.insertString(0, text, null);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < dokument.getDefaultRootElement().getElementCount(); i++) {
            Element zeile = dokument.getDefaultRootElement().getElement(i);
            dokument.setLogicalStyle(zeile.getStartOffset(), normalerStil);
        }
    }

    public StyledDocument getDokument() {
        return dokument;
    }

    public String getText() {
        try {
            return dokument.getText(0, dokument.getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException();
        }
    }

    public void setText(String text) {
        try {
            dokument.remove(0, dokument.getLength());
            dokument.insertString(0, text, SimpleAttributeSet.EMPTY);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        Element wurzel = dokument.getDefaultRootElement();
        for (int i = 0; i < wurzel.getElementCount(); i++) {
            Element absatz = wurzel.getElement(i);
            dokument.setLogicalStyle(absatz.getStartOffset(), dokument.getStyle("Normal"));
        }
    }
}
