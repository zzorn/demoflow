package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.ValueEditorBase;
import org.demoflow.parameter.range.Range;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

/**
 *
 */
public final class DoubleEditor extends ValueEditorBase<Double> {

    private static final DecimalFormat FORMAT = new DecimalFormat("#0.0###");
    private JFormattedTextField numberField;
    private Color defaultColor;
    private double originalValue;

    private static final Color ERROR_COLOR = new Color(255, 200, 200);
    private static final Color WARNING_COLOR = new Color(255,230,200);
    private static final Color EDITED_COLOR = new Color(255,255,200);

    public DoubleEditor(Range<Double> range) {
        super(range);
    }

    @Override protected JComponent buildEditorUi(JPanel editorPanel, Range<Double> range, Double initialValue) {
        numberField = new JFormattedTextField(FORMAT);
        numberField.setPreferredSize(new Dimension(100, 24));
        defaultColor = numberField.getBackground();
        originalValue = initialValue;

        numberField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                updateHighlightColor();
            }

            @Override public void removeUpdate(DocumentEvent e) {
                updateHighlightColor();
            }

            @Override public void changedUpdate(DocumentEvent e) {
                updateHighlightColor();
            }
        });

        numberField.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
            }

            @Override public void focusLost(FocusEvent e) {
                handleUiValueUpdate();
            }
        });

        numberField.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                handleUiValueUpdate();
            }
        });

        return numberField;
    }

    private void handleUiValueUpdate() {
        Double newValue = parseValue();
        if (newValue != null) {
            onValueUpdatedInUi(newValue);
            originalValue = newValue;
            updateHighlightColor();
        }
    }

    private void updateHighlightColor() {
        final Double value = parseValue();
        if (value == null) setHighlightColor(ERROR_COLOR);
        else if (outOfRange(value)) setHighlightColor(WARNING_COLOR);
        else if (originalValue != value) setHighlightColor(EDITED_COLOR);
        else setHighlightColor(defaultColor);
    }

    private void setHighlightColor(Color color) {
        numberField.setBackground(color);
        numberField.repaint();
    }

    private boolean outOfRange(double value) {
        double clamped = getRange().clampToRange(value);
        return clamped != value;
    }

    private Double parseValue() {
        Double newValue;
        try {
            newValue = Double.parseDouble(numberField.getText());
        } catch (Exception ex) {
            newValue = null;
        }
        return newValue;
    }

    @Override protected void onValueUpdatedToUi(Double value) {
        numberField.setText(value.toString());
        originalValue = value;
        updateHighlightColor();
    }
}
