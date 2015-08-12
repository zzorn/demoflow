package org.demoflow.utils.uiutils;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Vector;

/**
 * A JComboBox that supports scrolling with mouse wheel.
 * Workaround for this 8+ year old bug in Swing: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5045691
 */
public class JComboBoxWithWheelScroll<E> extends JComboBox<E> {

    public JComboBoxWithWheelScroll(ComboBoxModel<E> aModel) {
        super(aModel);
        setupScrolling();
    }

    public JComboBoxWithWheelScroll(E items[]) {
        super(items);
        setupScrolling();
    }

    public JComboBoxWithWheelScroll(Vector<E> items) {
        super(items);
        setupScrolling();
    }

    public JComboBoxWithWheelScroll() {
        setupScrolling();
    }

    private void setupScrolling() {
        // Listen to scroll wheel events and change combo box selection based on them.
        addMouseWheelListener( new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    // scroll up
                    int newIndex = getSelectedIndex() - 1;
                    if (newIndex >= 0) {
                        setSelectedIndex(newIndex);
                    }
                } else {
                    // scroll down
                    int newIndex = getSelectedIndex() + 1;
                    if (newIndex < getItemCount()) {
                        setSelectedIndex(newIndex);
                    }
                }
                //e.consume();
            }
        });
    }

    @Override public E getSelectedItem() {
        return (E) super.getSelectedItem();
    }
}
