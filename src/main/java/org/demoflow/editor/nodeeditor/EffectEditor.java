package org.demoflow.editor.nodeeditor;

import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.Effect;
import org.demoflow.node.DemoNode;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class EffectEditor<T extends DemoNode> extends NodeEditorBase<Effect> {

    private static final Color EDITOR_COLOR = new Color(255, 166, 0);

    public EffectEditor(Effect node, DemoEditor demoEditor) {
        super(node, demoEditor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Effect node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Effect node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }

    @Override protected Color getEditorColor() {
        return EDITOR_COLOR;
    }

    @Override protected double getEditorColorMixStrength() {
        return 0.1;
    }

}
