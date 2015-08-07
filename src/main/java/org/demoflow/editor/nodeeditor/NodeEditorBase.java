package org.demoflow.editor.nodeeditor;

import com.badlogic.gdx.utils.Array;
import net.miginfocom.swing.MigLayout;
import org.demoflow.demo.Demo;
import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.Effect;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeListener;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.Calculator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.MouseInputAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;


import static org.flowutils.Check.notNull;

/**
 * Base class for DemoNode editors.
 */
public abstract class NodeEditorBase<T extends DemoNode> extends JPanel {

    private static final boolean DRAW_DEBUG_BORDERS = false;

    private static final int LABEL_WIDTH = 100;

    private static final Image ARROW_RIGHT = Toolkit.getDefaultToolkit().getImage("assets/icons/arrow_right.png");
    private static final Image ARROW_DOWN = Toolkit.getDefaultToolkit().getImage("assets/icons/arrow_down.png");
    private static final String CHILD_UI_LAYOUT_SETTINGS = "newline, left";

    private final ImageIcon arrowRightIcon = new ImageIcon(ARROW_RIGHT);
    private final ImageIcon arrowDownIcon = new ImageIcon(ARROW_DOWN);

    private final Map<DemoNode, NodeEditorBase> childUis = new LinkedHashMap<>();
    private final T node;
    private final DemoEditor demoEditor;

    private int barHeight = 24;
    private int indentWidth = 20;
    private JLabel expandToggle;
    private JLabel nameLabel;
    private JPanel timeEditorPanel;
    private JPanel childPanel;
    private JPanel indenter;
    private boolean expanded = true;

    private final DemoNodeListener nodeListener = new DemoNodeListener() {
        @Override public void onChildAdded(DemoNode parent, DemoNode child) {
            addChildUi(child);
        }

        @Override public void onChildRemoved(DemoNode parent, DemoNode child) {
            removeChildUi(child);
        }

        @Override public void onNodeUpdated(DemoNode node) {
            updateNodeUi();
        }
    };

    /**
     * Creates editor for the specified node.
     */
    public NodeEditorBase(T node, DemoEditor demoEditor) {
        super(new MigLayout("insets 0, gapy 0"));

        notNull(node, "node");
        notNull(demoEditor, "editor");

        this.node = node;
        this.demoEditor = demoEditor;

        buildBasicUi();

        node.addNodeListener(nodeListener);
    }

    /**
     * @return edited node.
     */
    public T getNode() {
        return node;
    }

    /**
     * @return panel with time based editor.  Should be scrolled separately from the rest of the node ui.
     */
    public JPanel getTimeEditorPanel() {
        return timeEditorPanel;
    }

    private void buildBasicUi() {
        JPanel topBar = new JPanel(new MigLayout("insets 0"));
        add(topBar, "north, pushx");

        // Add indenter
        indenter = new JPanel(new MigLayout("insets 0"));
        topBar.add(indenter, "align left");
        debugBorderize(indenter, Color.blue);

        // Expand collapse button
        expandToggle = new JLabel(expanded ? arrowDownIcon : arrowRightIcon);
        setSize(expandToggle, barHeight, barHeight);
        indenter.add(expandToggle, "push, align right");
        indenter.addMouseListener(new MouseInputAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                setExpanded(!expanded);
            }
        });

        // Name of node
        nameLabel = new JLabel(node.getName());
        topBar.add(nameLabel, "align left");
        debugBorderize(nameLabel, Color.green);

        // Space for other things in top bar
        JPanel otherTopBarContentPanel = new JPanel(new MigLayout("insets 0"));
        topBar.add(otherTopBarContentPanel);

        // Time editor bar
        timeEditorPanel = new JPanel(new MigLayout("insets 0"));

        // Space for children
        childPanel = new JPanel(new MigLayout("insets 0, gapy 0"));
        add(childPanel, "south, grow");

        // Add UIs for current children
        final Array<? extends DemoNode> children = node.getChildren();
        for (int i = 0; i < children.size; i++) {
            addChildUi(children.get(i));
        }

        // Do implementation specific ui building
        buildUi(otherTopBarContentPanel, timeEditorPanel, node);

        debugBorderize(NodeEditorBase.this, Color.RED);

        updateNodeUi();
    }

    private void setExpanded(boolean expanded) {
        if (this.expanded != expanded) {
            // Update state
            this.expanded = expanded;

            // Update expand icon appearance
            expandToggle.setIcon(this.expanded ? arrowDownIcon : arrowRightIcon);

            // Update child visibility
            childPanel.setVisible(expanded);
            if (expanded) {
                childPanel.setMaximumSize(null);
                childPanel.setMinimumSize(null);
                childPanel.setPreferredSize(null);
            }
            else {
                final Dimension collapsedSize = new Dimension(childPanel.getWidth(), 0);
                childPanel.setMaximumSize(collapsedSize);
                childPanel.setMinimumSize(collapsedSize);
                childPanel.setPreferredSize(collapsedSize);
            }

            // Refresh
            refreshLayout();
        }
    }

    private void updateIndent() {
        final int maxIndent = indentWidth * (1 + node.getRootNode().getMaxDepth());
        final int ownIndent = (1 + node.getDepth()) * indentWidth;
        final int surplusIndent = maxIndent - ownIndent;

        setSize(indenter, ownIndent, barHeight);
        setSize(nameLabel, LABEL_WIDTH + surplusIndent, barHeight);
    }

    private void setSize(final JComponent component, int width, final int height) {
        final Dimension size = new Dimension(width, height);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
    }

    private void addChildUi(DemoNode child) {
        notNull(child, "child");

        final NodeEditorBase childUi;
        if (child instanceof Demo) childUi = new DemoNodeEditor((Demo) child, demoEditor);
        else if (child instanceof Effect) childUi = new EffectEditor((Effect) child, demoEditor);
        else if (child instanceof Parameter) childUi = new ParameterEditor((Parameter) child, demoEditor);
        else if (child instanceof Calculator) {
            // For calculators, just add the child parameters of the calculator instead of the calculation node
            childUi = null;
            for (DemoNode calculatorParameter : child.getChildren()) {
                addChildUi(calculatorParameter);
            }

        }
        else throw new IllegalStateException("Unknown node type: " + child.getClass());

        if (childUi != null) {
            childUis.put(child, childUi);
            childPanel.add(childUi, CHILD_UI_LAYOUT_SETTINGS);

            updateIndent();

            refreshLayout();
        }
    }

    private void removeChildUi(DemoNode child) {

        if (child instanceof Calculator) {
            // For calculators, remove the children, as the calculation node itself is not added
            for (DemoNode calculatorParameters : child.getChildren()) {
                removeChildUi(calculatorParameters);
            }
        }
        else {
            final NodeEditorBase childUi = childUis.remove(child);
            if (childUi != null) {
                childPanel.remove(childUi);
                invalidate();
                repaint();

                updateIndent();

                refreshLayout();
            }
        }
    }

    private void updateNodeUi() {
        nameLabel.setText(node.getName());
        updateIndent();
        expandToggle.setVisible(node.getChildCount() > 0);
        doUpdateNodeUi(node);
    }


    protected abstract void doUpdateNodeUi(T node);


    /**
     * Create node type specific UIs.
     * @param otherTopBarContentPanel
     * @param timeEditorPanel
     */
    protected abstract void buildUi(JPanel otherTopBarContentPanel, JPanel timeEditorPanel, T node);

    private void debugBorderize(final JComponent component, final Color color) {
        // DEBUG: border
        if (DRAW_DEBUG_BORDERS) component.setBorder(new LineBorder(color));
    }

    private void refreshLayout() {
        childPanel.revalidate();
        childPanel.repaint();
    }

    public void collapseAll() {
        setExpanded(false);
        for (NodeEditorBase childEditor : childUis.values()) {
            childEditor.collapseAll();
        }
    }

    public void expandAll() {
        setExpanded(true);
        for (NodeEditorBase childEditor : childUis.values()) {
            childEditor.expandAll();
        }
    }

    public void expandToDepth(int depth) {
        if (depth <= 0) {
            collapseAll();
        }
        else {
            setExpanded(true);
            for (NodeEditorBase childEditor : childUis.values()) {
                childEditor.expandToDepth(depth - 1);
            }
        }
    }

    protected final DemoEditor getDemoEditor() {
        return demoEditor;
    }
}
