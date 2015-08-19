package org.demoflow.editor;

import net.miginfocom.swing.MigLayout;
import org.demoflow.DemoComponentManager;
import org.demoflow.effect.Effect;
import org.demoflow.effect.EffectContainer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class CreateEffectDialog {

    private final JFrame root;
    private final DemoComponentManager componentManager;
    private final JDialog dialog;

    private EffectContainer effectContainer;

    public CreateEffectDialog(JFrame root, final DemoComponentManager componentManager) {
        notNull(root, "root");
        notNull(componentManager, "componentManager");

        this.root = root;
        this.componentManager = componentManager;

        dialog = new JDialog(root, "Create Effect", true);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 12"));

        // Effect types combo box
        final java.util.List<Class<? extends Effect>> effectTypes = componentManager.getEffectTypes();
        final Class[] effectClasses = effectTypes.toArray(new Class[effectTypes.size()]);
        final JComboBox<Class<? extends Effect>> effectTypeSelector = new JComboBox<Class<? extends Effect>>(effectClasses);
        effectTypeSelector.setRenderer(new ClassComboBoxRenderer("null", "Effect"));
        mainPanel.add(new JLabel("Effect to add"));
        mainPanel.add(effectTypeSelector);

        // Ok & Cancel
        JPanel buttonPanel = new JPanel(new MigLayout("gap 12"));
        mainPanel.add(buttonPanel, "south");

        buttonPanel.add(new JButton(new AbstractAction("Ok") {
            @Override public void actionPerformed(ActionEvent e) {
                // Add effect
                final Effect effect = componentManager.createEffect((Class<? extends Effect>) effectTypeSelector.getSelectedItem());
                effectContainer.addEffect(effect);

                // Hide dialog
                dialog.setVisible(false);
            }
        }), "pushx, right");

        buttonPanel.add(new JButton(new AbstractAction("Cancel") {
            @Override public void actionPerformed(ActionEvent e) {
                // Hide dialog
                dialog.setVisible(false);
            }
        }));

        dialog.setContentPane(mainPanel);
        //dialog.setPreferredSize(new Dimension(500, 200));
        dialog.pack();
        dialog.setLocationRelativeTo(root);
    }

    public void openDialog(EffectContainer effectContainer) {
        notNull(effectContainer, "effectContainer");

        this.effectContainer = effectContainer;

        dialog.setVisible(true);
    }



}
