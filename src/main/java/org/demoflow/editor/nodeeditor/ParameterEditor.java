package org.demoflow.editor.nodeeditor;

import org.demoflow.editor.CalculatorComboBoxRenderer;
import org.demoflow.editor.Editor;
import org.demoflow.node.DemoNode;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public final class ParameterEditor<T extends DemoNode> extends NodeEditorBase<Parameter> {

    private static final int SELECTOR_WIDTH = 150;

    private static final boolean ASK_PERMISSION_BEFORE_CALCULATOR_CHANGE = false;

    private JComboBox<Class<? extends Calculator>> calculatorSelector;

    private Class<? extends Calculator> currentCalculatorType = null;


    public ParameterEditor(Parameter node, Editor editor) {
        super(node, editor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Parameter node) {
        final Calculator calculator = node.getCalculator();
        currentCalculatorType = calculator == null ? null : calculator.getClass();

        // Create calculation type selection combo box
        final List<Class<? extends Calculator>> calculatorTypes = getEditor().getComponentManager().getCalculatorTypes(node.getType());
        calculatorSelector = new JComboBox<>(new Vector<>(calculatorTypes));
        calculatorSelector.setRenderer(new CalculatorComboBoxRenderer());
        calculatorSelector.setEnabled(true);
        calculatorSelector.setPreferredSize(new Dimension(SELECTOR_WIDTH, calculatorSelector.getHeight()));
        calculatorSelector.setSelectedItem(currentCalculatorType);
        calculatorSelector.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.out.println("ParameterEditor.actionPerformed");
                System.out.println("currentCalculatorType = " + currentCalculatorType);
                final Class<? extends Calculator> selectedType = (Class<? extends Calculator>) calculatorSelector.getSelectedItem();
                System.out.println("selectedType = " + selectedType);
                if (selectedType != currentCalculatorType) {
                    // Something changed

                    if (ASK_PERMISSION_BEFORE_CALCULATOR_CHANGE) {
                        // Ask for permission, as we might mess up a large branch otherwise
                        final int response = JOptionPane.showConfirmDialog(getEditor().getRootFrame(),
                                                                           "Changing the calculator removes the previous calculator.",
                                                                           "Change calculator for " +
                                                                           getNode().getId() +
                                                                           "?",
                                                                           JOptionPane.OK_CANCEL_OPTION,
                                                                           JOptionPane.QUESTION_MESSAGE);
                        if (response == JOptionPane.OK_OPTION) {
                            changeCalculatorType(selectedType);
                        }
                    }
                    else {
                        changeCalculatorType(selectedType);
                    }
                }
            }
        });

        otherTopBarContentPanel.add(calculatorSelector);
    }

    @Override protected void doUpdateNodeUi(Parameter node) {
        final Calculator calculator = node.getCalculator();
        final Class<? extends Calculator> calculatorType = calculator == null ? null : calculator.getClass();

        if (calculatorType != currentCalculatorType) {
            currentCalculatorType = calculatorType;

            System.out.println("ParameterEditor.doUpdateNodeUi XXXXXXXXXXX");
            calculatorSelector.setSelectedItem(calculatorType);
        }
    }


    private void changeCalculatorType(Class<? extends Calculator> selectedType) {
        currentCalculatorType = selectedType;

        System.out.println("ParameterEditor.changeCalculatorType XXXXXXXXXXXXXX");
        getNode().setCalculator(getEditor().getComponentManager().createCalculator(selectedType));
    }
}
