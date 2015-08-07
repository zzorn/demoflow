package org.demoflow.editor.nodeeditor;

import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.Effect;
import org.demoflow.node.DemoNode;

import javax.swing.*;

/**
 *
 */
public class EffectEditor<T extends DemoNode> extends NodeEditorBase<Effect> {

    public EffectEditor(Effect node, DemoEditor demoEditor) {
        super(node, demoEditor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Effect node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Effect node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }
}
