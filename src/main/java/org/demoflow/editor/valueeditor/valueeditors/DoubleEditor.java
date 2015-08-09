package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.ValueEditorBase;
import org.demoflow.parameter.range.Range;
import org.uiflow.desktop.ColorUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;

import static org.flowutils.Check.notNull;

/**
 *
 */
public final class DoubleEditor extends ValueEditorBase<Double> {

    private static final DecimalFormat FORMAT = new DecimalFormat("#0.0###");
    public static final Color DEFAULT_ERROR = new Color(255, 0, 0);
    public static final Color DEFAULT_WARNING = new Color(255,190,0);
    public static final Color DEFAULT_EDITED = new Color(255,255,0);
    private JFormattedTextField numberField;
    private Color defaultColor;
    private double originalValue;

    private Color errorColor;
    private Color warningColor;
    private Color editedColor;

    public DoubleEditor(Range<Double> range) {
        super(range);
    }

    @Override protected JComponent buildEditorUi(JPanel editorPanel, Range<Double> range, Double initialValue) {
        numberField = new JFormattedTextField(FORMAT);
        numberField.setPreferredSize(new Dimension(100, 24));

        defaultColor = editorPanel.getBackground();
        notNull(defaultColor, "defaultColor");

        errorColor = ColorUtils.mixColors(0.4, defaultColor, DEFAULT_ERROR);
        warningColor = ColorUtils.mixColors(0.4, defaultColor, DEFAULT_WARNING);
        editedColor = ColorUtils.mixColors(0.2, defaultColor, DEFAULT_EDITED);

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
        if (value == null) setHighlightColor(errorColor);
        else if (outOfRange(value)) setHighlightColor(warningColor);
        else if (originalValue != value) setHighlightColor(editedColor);
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
