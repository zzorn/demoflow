package org.demoflow.editor.nodeeditor;

import org.demoflow.DemoComponentManager;
import org.demoflow.demo.Demo;
import org.demoflow.editor.Editor;

import javax.swing.*;

/**
 *
 */
public class DemoNodeEditor extends NodeEditorBase<Demo> {

    public DemoNodeEditor(Demo node, Editor editor) {
        super(node, editor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Demo node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Demo node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }
}
