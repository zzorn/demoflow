package org.demoflow.editor.nodeeditor;

import org.demoflow.DemoComponentManager;
import org.demoflow.editor.Editor;
import org.demoflow.effect.Effect;
import org.demoflow.node.DemoNode;

import javax.swing.*;

/**
 *
 */
public class EffectEditor<T extends DemoNode> extends NodeEditorBase<Effect> {

    public EffectEditor(Effect node, Editor editor) {
        super(node, editor);
    }

    @Override protected void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, Effect node) {
        // IMPLEMENT: Implement buildUi

    }

    @Override protected void doUpdateNodeUi(Effect node) {
        // IMPLEMENT: Implement doUpdateNodeUi

    }
}
