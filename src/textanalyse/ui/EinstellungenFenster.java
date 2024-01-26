package textanalyse.ui;

import textanalyse.daten.TextAnalyse;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class EinstellungenFenster {
    private JDialog fenster;

    public EinstellungenFenster(TextAnalyseFenster parent, TextAnalyse daten) {
        fenster = new JDialog(parent.getFenster());
        fenster.setTitle("Einstellungen");
        JPanel content = new JPanel();
        fenster.setContentPane(content);
        fenster.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        BoxLayout layout = new BoxLayout(content, BoxLayout.PAGE_AXIS);

        JLabel label = new JLabel();
        label.setText("Einstellugen");
        label.setFont(label.getFont().deriveFont(20f));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(label);

        addFarbeKnopf("Normal", "Normal", daten, content);
        addSchriftGrößeEinstellung("Normal", "Normal", daten, content);
        addCheckBox(daten, "Normal", "Normal: Fett", StyleConstants::isBold, StyleConstants::setBold, content);
        addCheckBox(daten, "Normal", "Normal: Kursiv", StyleConstants::isItalic, StyleConstants::setItalic, content);
        addCheckBox(daten, "Normal", "Normal: Unterstrichen", StyleConstants::isUnderline, StyleConstants::setUnderline, content);

        content.add(Box.createVerticalGlue());
        content.add(Box.createVerticalStrut(40));

        addFarbeKnopf("Markierung", "Markierung", daten, content);
        addSchriftGrößeEinstellung("Markierung", "Markierung", daten, content);
        addCheckBox(daten, "Markierung", "Markierung: Fett", StyleConstants::isBold, StyleConstants::setBold, content);
        addCheckBox(daten, "Markierung", "Markierung: Kursiv", StyleConstants::isItalic, StyleConstants::setItalic, content);
        addCheckBox(daten, "Markierung", "Markierung: Unterstrichen", StyleConstants::isUnderline, StyleConstants::setUnderline, content);

        content.setLayout(layout);
        fenster.pack();
        fenster.setVisible(true);
    }

    private void addFarbeKnopf(String name, String beschriftung, TextAnalyse daten, JPanel content) {
        JButton button = new JButton();
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setText("Farbe ändern: " + beschriftung);
        button.setForeground(StyleConstants.getForeground(daten.getDokument().getStyle(name)));
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                button.setForeground(StyleConstants.getForeground(daten.getDokument().getStyle(name)));
            }
        };
        daten.getDokument().getStyle(name).addChangeListener(changeListener);
        fenster.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                daten.getDokument().getStyle(name).removeChangeListener(changeListener);
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color neueFarbe = JColorChooser.showDialog(button, "Farbe ändern", button.getForeground());
                if (neueFarbe == null) {
                    return;
                }
                StyleConstants.setForeground(daten.getDokument().getStyle(name), neueFarbe);
            }
        });
        content.add(button);
        content.add(Box.createVerticalStrut(5));
    }

    private void addSchriftGrößeEinstellung(String name, String beschriftung, TextAnalyse daten, JPanel content) {
        JLabel label = new JLabel();
        label.setText("Schriftgröße ändern von: " + beschriftung);
        content.add(label);

        JSlider slider = new JSlider();
        slider.setMinimum(10);
        slider.setMaximum(40);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setValue(StyleConstants.getFontSize(daten.getDokument().getStyle(name)));
        boolean[] isUserInput = new boolean[] { true };
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!isUserInput[0]) {
                   return;
                }
                StyleConstants.setFontSize(daten.getDokument().getStyle(name), slider.getValue());
            }
        });
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                isUserInput[0] = false;
                try {
                    slider.setValue(StyleConstants.getFontSize(daten.getDokument().getStyle(name)));
                } finally {
                    isUserInput[0] = true;
                }
            }
        };
        daten.getDokument().getStyle(name).addChangeListener(changeListener);
        fenster.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                daten.getDokument().getStyle(name).removeChangeListener(changeListener);
            }
        });
        content.add(slider);
    }

    private void addCheckBox(
            TextAnalyse daten,
            String stilName,
            String beschriftung,
            Function<Style, Boolean> getEigenschaft,
            BiConsumer<Style, Boolean> setEigenschaft,
            JPanel content
    ) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBox.setText(beschriftung);
        checkBox.setSelected(getEigenschaft.apply(daten.getDokument().getStyle(stilName)));
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEigenschaft.accept(daten.getDokument().getStyle(stilName), checkBox.isSelected());
            }
        });
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                checkBox.setSelected(getEigenschaft.apply(daten.getDokument().getStyle(stilName)));
            }
        };
        daten.getDokument().getStyle(stilName).addChangeListener(changeListener);
        fenster.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                daten.getDokument().getStyle(stilName).removeChangeListener(changeListener);
            }
        });
        content.add(checkBox);
    }
}
