package org.demoflow.editor.nodeeditor;

import org.demoflow.demo.Demo;
import org.demoflow.editor.DemoEditor;

import javax.swing.*;

/**
 *
 */
public class DemoNodeEditor extends NodeEditorBase<Demo> {

    public DemoNodeEditor(Demo node, DemoEditor demoEditor) {
        super(node, demoEditor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Demo node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Demo node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }
}
