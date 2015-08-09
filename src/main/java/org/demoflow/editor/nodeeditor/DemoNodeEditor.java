package org.demoflow.editor.nodeeditor;

import org.demoflow.demo.Demo;
import org.demoflow.editor.DemoEditor;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class DemoNodeEditor extends NodeEditorBase<Demo> {

    private static final Color EDITOR_COLOR = new Color(162, 0, 255);

    public DemoNodeEditor(Demo node, DemoEditor demoEditor) {
        super(node, demoEditor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel valueEditorPanel, Demo node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Demo node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }

    @Override protected Color getEditorColor() {
        return EDITOR_COLOR;
    }

    @Override protected double getEditorColorMixStrength() {
        return 0.4;
    }

}
