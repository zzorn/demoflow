package org.demoflow.editor.nodeeditor;

import org.demoflow.editor.CreateEffectDialog;
import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.Effect;
import org.demoflow.effect.EffectContainer;
import org.demoflow.node.DemoNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 */
public class EffectEditor<T extends DemoNode> extends NodeEditorBase<Effect> {

    private static final Color EDITOR_COLOR = new Color(255, 97, 0);

    private final CreateEffectDialog createEffectDialog;


    public EffectEditor(Effect node, DemoEditor demoEditor) {
        super(node, demoEditor);

        createEffectDialog = new CreateEffectDialog(demoEditor.getRootFrame(), demoEditor.getComponentManager());
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel valueEditorPanel, Effect node) {
        // Add add button
        if (getNode() instanceof EffectContainer) {
            otherTopBarContentPanel.add(new JButton(new AbstractAction("Add Effect") {
                @Override public void actionPerformed(ActionEvent e) {
                    createEffectDialog.openDialog((EffectContainer) getNode());
                }
            }), "right");
        }

        /* TODO: Fix node place update in UI
        // Add move buttons
        otherTopBarContentPanel.add(new JButton(new AbstractAction("^") {
            @Override public void actionPerformed(ActionEvent e) {
                EffectContainer container = (EffectContainer) getNode().getParent();
                container.moveEffect(getNode(), -1);
            }

            @Override public boolean isEnabled() {
                EffectContainer container = (EffectContainer) getNode().getParent();
                return container.indexOf(getNode()) > 0;
            }
        }), "right");

        otherTopBarContentPanel.add(new JButton(new AbstractAction("v") {
            @Override public void actionPerformed(ActionEvent e) {
                EffectContainer container = (EffectContainer) getNode().getParent();
                container.moveEffect(getNode(), +1);
            }

            @Override public boolean isEnabled() {
                EffectContainer container = (EffectContainer) getNode().getParent();
                return container.indexOf(getNode()) < container.getEffects().size - 1;
            }
        }), "right");
        */

        // Add delete button
        otherTopBarContentPanel.add(new JButton(new AbstractAction("X") {
            @Override public void actionPerformed(ActionEvent e) {
                EffectContainer container = (EffectContainer) getNode().getParent();
                container.removeEffect(getNode());
            }
        }), "pushx, right");

    }

    @Override protected void doUpdateNodeUi(Effect node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }

    @Override protected Color getEditorColor() {
        return EDITOR_COLOR;
    }

    @Override protected double getEditorColorMixStrength() {
        return 0.4;
    }

}
