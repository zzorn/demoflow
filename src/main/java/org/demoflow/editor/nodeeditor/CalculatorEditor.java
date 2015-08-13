package org.demoflow.editor.nodeeditor;

import org.demoflow.editor.DemoEditor;
import org.demoflow.node.DemoNode;
import org.demoflow.calculator.Calculator;

import javax.swing.*;

/**
 *
 */
public class CalculatorEditor<T extends DemoNode> extends NodeEditorBase<Calculator> {

    public CalculatorEditor(Calculator node, DemoEditor demoEditor) {
        super(node, demoEditor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel valueEditorPanel, Calculator node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Calculator node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }
}
