package org.demoflow.editor.nodeeditor;

import org.demoflow.demo.Demo;
import org.demoflow.editor.CreateEffectDialog;
import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.EffectContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class DemoNodeEditor extends NodeEditorBase<Demo> {

    private static final Color EDITOR_COLOR = new Color(162, 0, 255);

    private final CreateEffectDialog createEffectDialog;

    public DemoNodeEditor(Demo node, DemoEditor demoEditor) {
        super(node, demoEditor);

        createEffectDialog = new CreateEffectDialog(demoEditor.getRootFrame(), demoEditor.getComponentManager());
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel valueEditorPanel, Demo node) {
        // Add add button
        addBarButton("Add Effect", "Adds a new effect", null, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                createEffectDialog.openDialog(getNode());
            }
        });
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
