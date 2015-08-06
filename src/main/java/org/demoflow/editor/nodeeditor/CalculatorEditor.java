package org.demoflow.editor.nodeeditor;

import org.demoflow.DemoComponentManager;
import org.demoflow.editor.Editor;
import org.demoflow.node.DemoNode;
import org.demoflow.parameter.calculator.Calculator;

import javax.swing.*;

/**
 *
 */
public class CalculatorEditor<T extends DemoNode> extends NodeEditorBase<Calculator> {

    public CalculatorEditor(Calculator node, Editor editor) {
        super(node, editor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Calculator node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Calculator node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }
}
