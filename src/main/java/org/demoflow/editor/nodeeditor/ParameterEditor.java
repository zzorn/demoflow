package org.demoflow.editor.nodeeditor;

import org.demoflow.editor.CalculatorComboBoxRenderer;
import org.demoflow.editor.DemoEditor;
import org.demoflow.editor.valueeditor.ValueEditor;
import org.demoflow.editor.valueeditor.ValueEditorListener;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeListener;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.ParameterListener;
import org.demoflow.parameter.ParameterListenerAdapter;
import org.demoflow.parameter.calculator.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;

/**
 *
 */
public final class ParameterEditor<T extends DemoNode> extends NodeEditorBase<Parameter> {

    private static final int SELECTOR_WIDTH = 150;

    private static final boolean ASK_PERMISSION_BEFORE_CALCULATOR_CHANGE = false;

    private JComboBox<Class<? extends Calculator>> calculatorSelector;

    private Class<? extends Calculator> currentCalculatorType = null;
    private ValueEditor parameterValueEditor;


    public ParameterEditor(Parameter node, DemoEditor demoEditor) {
        super(node, demoEditor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, final Parameter node) {
        final Calculator calculator = node.getCalculator();
        currentCalculatorType = calculator == null ? null : calculator.getClass();

        // Create calculation type selection combo box
        final List<Class<? extends Calculator>> calculatorTypes = getDemoEditor().getComponentManager().getCalculatorTypes(node.getType());
        calculatorSelector = new JComboBox<>(new Vector<>(calculatorTypes));
        calculatorSelector.setRenderer(new CalculatorComboBoxRenderer());
        calculatorSelector.setEnabled(true);
        calculatorSelector.setPreferredSize(new Dimension(SELECTOR_WIDTH, calculatorSelector.getHeight()));
        calculatorSelector.setSelectedItem(currentCalculatorType);
        calculatorSelector.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                final Class<? extends Calculator> selectedType = (Class<? extends Calculator>) calculatorSelector.getSelectedItem();
                if (selectedType != currentCalculatorType) {
                    // Something changed

                    if (ASK_PERMISSION_BEFORE_CALCULATOR_CHANGE) {
                        // Ask for permission, as we might mess up a large branch otherwise
                        final int response = JOptionPane.showConfirmDialog(getDemoEditor().getRootFrame(),
                                                                           "Changing the calculator removes the previous calculator.",
                                                                           "Change calculator for " +
                                                                           getNode().getId() +
                                                                           "?",
                                                                           JOptionPane.OK_CANCEL_OPTION,
                                                                           JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.OK_OPTION) {
                            changeCalculatorType(selectedType);
                        }
                    } else {
                        changeCalculatorType(selectedType);
                    }
                }
            }
        });
        otherTopBarContentPanel.add(calculatorSelector);


        // Create value editor
        parameterValueEditor = getDemoEditor().getEditorManager().createValueEditor(node.getRange(), node.get());
        if (parameterValueEditor != null) {

            // Add to bar
            otherTopBarContentPanel.add(parameterValueEditor.getEditorUi());

            // Update parameter from editor
            parameterValueEditor.addListener(new ValueEditorListener() {
                @Override public void onValueEdited(ValueEditor editor, Object editedValue) {
                    node.set(editedValue);
                }
            });

            // Update editor from parameter
            node.addParameterListener(new ParameterListenerAdapter() {
                @Override public void onDefaultValueChanged(Parameter parameter, Object newValue) {
                    parameterValueEditor.setValue(newValue);
                }

                @Override public void onCalculatorChanged(Parameter parameter, Calculator newCalculator) {
                    // Only show parameter editor if we don't have any calculator selected
                    parameterValueEditor.getEditorUi().setVisible(newCalculator == null);
                }
            });

            // Only show parameter editor if we don't have any calculator selected
            parameterValueEditor.getEditorUi().setVisible(node.getCalculator() == null);
        }


    }

    @Override protected void doUpdateNodeUi(Parameter node) {
        final Calculator calculator = node.getCalculator();
        final Class<? extends Calculator> calculatorType = calculator == null ? null : calculator.getClass();

        if (calculatorType != currentCalculatorType) {
            currentCalculatorType = calculatorType;

            calculatorSelector.setSelectedItem(calculatorType);
        }
    }


    private void changeCalculatorType(Class<? extends Calculator> selectedType) {
        currentCalculatorType = selectedType;

        getNode().setCalculator(getDemoEditor().getComponentManager().createCalculator(selectedType));
    }
}
