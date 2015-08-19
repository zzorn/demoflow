package org.demoflow.editor.valueeditor;

import org.demoflow.parameter.range.Range;
import org.demoflow.utils.SimpleStringFormat;
import org.uiflow.desktop.ColorUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.Format;
import java.util.Objects;

import static org.flowutils.Check.notNull;

/**
 * Base class for editors that use a text field for value entry.
 */
public abstract class TextFieldEditorBase<T> extends ValueEditorBase<T> {

    public static final Color DEFAULT_ERROR_COLOR = new Color(255, 0, 0);
    public static final Color DEFAULT_WARNING_COLOR = new Color(255,190,0);
    public static final Color DEFAULT_EDITED_COLOR = new Color(255,255,0);

    private JFormattedTextField textField;
    private Color defaultColor;
    private Color editedColor;
    private Color warningColor;
    private Color errorColor;
    private String originalValueText;

    public TextFieldEditorBase(Range<T> range) {
        super(range);
    }

    @Override protected JComponent buildEditorUi(JPanel editorPanel, Range<T> range, T initialValue) {
        textField = new JFormattedTextField(createTextFieldFormat());
        textField.setPreferredSize(new Dimension(120, 24));

        defaultColor = editorPanel.getBackground();
        notNull(defaultColor, "defaultColor");

        errorColor = ColorUtils.mixColors(0.4, defaultColor, DEFAULT_ERROR_COLOR);
        warningColor = ColorUtils.mixColors(0.4, defaultColor, DEFAULT_WARNING_COLOR);
        editedColor = ColorUtils.mixColors(0.2, defaultColor, DEFAULT_EDITED_COLOR);

        textField.getDocument().addDocumentListener(new DocumentListener() {
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

        textField.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
            }

            @Override public void focusLost(FocusEvent e) {
                handleUiValueUpdate();
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                handleUiValueUpdate();
            }
        });

        return textField;
    }


    private void handleUiValueUpdate() {
        final String text = textField.getText();
        T newValue = parseValue(text);
        if (newValue != null) {
            onValueUpdatedInUi(newValue);
            originalValueText = text;
            updateHighlightColor();
        }
    }

    private void updateHighlightColor() {
        final String text = textField.getText();
        final T value = parseValue(text);
        if (value == null) setHighlightColor(errorColor);
        else if (outOfRange(value)) setHighlightColor(warningColor);
        else if (originalValueText != null && !originalValueText.equals(text)) setHighlightColor(editedColor);
        else setHighlightColor(defaultColor);
    }

    private void setHighlightColor(Color color) {
        textField.setBackground(color);
        textField.repaint();
    }

    private boolean outOfRange(T value) {
        T clamped = getRange().clampToRange(value);
        return !Objects.equals(clamped, value);
    }

    private T parseValue(final String text) {
        try {
            return stringToValue(text);
        } catch (Exception e) {
            return null;
        }
    }

    @Override protected final void onValueUpdatedToUi(T value) {
        final String textValue = valueToString(value);
        textField.setText(textValue);
        originalValueText = textValue;
        updateHighlightColor();
    }

    /**
     * Convert the specified value to a string to be displayed in the editor.
     * Uses the range to do this by default.  Override as needed.
     */
    protected String valueToString(T value) {
        return getRange().valueToString(value);
    }

    /**
     * @return the value for the specified string, or null if it could not be parsed.
     *         Uses the range to do this by default.  Override as needed.
     * @throws Exception can throw any exception, it will be caught and the entered value marked as invalid.
     */
    protected T stringToValue(String text) throws Exception {
        return getRange().valueFromStringOrNull(text);
    }

    /**
     * @return format that should be used to format the displayed value with in the FormattedTextField.
     *         Uses a simple string format without any special formatting by default.  Override as needed.
     */
    protected Format createTextFieldFormat() {
        return new SimpleStringFormat();
    }

}
