package org.demoflow.editor.valueeditor;

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
    private T originalValue;

    public TextFieldEditorBase(Range<T> range) {
        super(range);
    }

    @Override protected JComponent buildEditorUi(JPanel editorPanel, Range<T> range, T initialValue) {
        textField = new JFormattedTextField(createTextFieldFormat());
        textField.setPreferredSize(new Dimension(100, 24));

        defaultColor = editorPanel.getBackground();
        notNull(defaultColor, "defaultColor");

        errorColor = ColorUtils.mixColors(0.4, defaultColor, DEFAULT_ERROR_COLOR);
        warningColor = ColorUtils.mixColors(0.4, defaultColor, DEFAULT_WARNING_COLOR);
        editedColor = ColorUtils.mixColors(0.2, defaultColor, DEFAULT_EDITED_COLOR);

        originalValue = initialValue;

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
        T newValue = parseValue();
        if (newValue != null) {
            onValueUpdatedInUi(newValue);
            originalValue = newValue;
            updateHighlightColor();
        }
    }

    private void updateHighlightColor() {
        final T value = parseValue();
        if (value == null) setHighlightColor(errorColor);
        else if (outOfRange(value)) setHighlightColor(warningColor);
        else if (originalValue != value) setHighlightColor(editedColor);
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

    private T parseValue() {
        T newValue;
        try {
            final String text = textField.getText();
            newValue = parseValue(text);
        } catch (Exception ex) {
            newValue = null;
        }
        return newValue;
    }

    @Override protected final void onValueUpdatedToUi(T value) {
        textField.setText(value.toString());
        originalValue = value;
        updateHighlightColor();
    }

    /**
     * @return format that should be used to format the displayed value with in the FormattedTextField.
     */
    protected abstract Format createTextFieldFormat();

    /**
     * @param text text the user entered in the text field.
     * @return parse the value from the text.  Return null for invalid values.
     * @throws Exception can throw any exception, it will be caught and the entered value marked as invalid.
     */
    protected abstract T parseValue(String text) throws Exception;
}
