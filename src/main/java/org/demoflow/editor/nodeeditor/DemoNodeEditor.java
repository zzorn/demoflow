package org.demoflow.editor.nodeeditor;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.demoflow.demo.Demo;
import org.demoflow.editor.CreateEffectDialog;
import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.EffectContainer;
import org.demoflow.utils.uiutils.timebar.TimeBar;
import org.demoflow.utils.uiutils.timebar.TimeBarModel;

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

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorBar, Demo node) {
        // Add add button
        addBarButton("Add Effect", "Adds a new effect", null, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                createEffectDialog.openDialog(getNode());
            }
        });

        // Create time preview ui
        timeEditorBar.add(new TimeBar(node.getTimeBarModel()), new CC().pad("0").grow().gap("0").push());
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
