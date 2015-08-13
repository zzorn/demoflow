package org.demoflow.editor.nodeeditor;

import com.badlogic.gdx.utils.Array;
import net.miginfocom.swing.MigLayout;
import org.demoflow.demo.Demo;
import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.Effect;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeListener;
import org.demoflow.node.DemoNodeListenerAdapter;
import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.Calculator;
import org.flowutils.StringUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
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
    private static final int OTHER_TOPBAR_WIDTH = 150;

    private static final Image ARROW_RIGHT = Toolkit.getDefaultToolkit().getImage("assets/icons/arrow_right.png");
    private static final Image ARROW_DOWN = Toolkit.getDefaultToolkit().getImage("assets/icons/arrow_down.png");
    private static final String CHILD_UI_LAYOUT_SETTINGS = "newline, left, growx, pushx";
    private static final Dimension ZERO_SIZE = new Dimension(0, 0);

    private final ImageIcon arrowRightIcon = new ImageIcon(ARROW_RIGHT);
    private final ImageIcon arrowDownIcon = new ImageIcon(ARROW_DOWN);

    private final Map<DemoNode, NodeEditorBase> childUis = new LinkedHashMap<>();
    private final T node;
    private final DemoEditor demoEditor;

    private int barHeight = 24;
    private int indentWidth = 20;
    private JLabel expandToggle;
    private JLabel nameLabel;
    private JPanel timeEditorBar;
    private JPanel childPanel;
    private JPanel indenter;
    private boolean expanded = true;

    private final DemoNodeListener nodeListener = new DemoNodeListenerAdapter() {
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

    private final DemoNodeListener rootNodeListener = new DemoNodeListenerAdapter() {
        @Override public void onNodeMaxDepthChanged(DemoNode node, int newMaxDepth) {
            // Recalculate the indent
            // DEBUG:
            System.out.println("NodeEditorBase.onNodeMaxDepthChanged " + node.getName() + " " + newMaxDepth);
            updateIndent();
        }
    };
    private JPanel otherTopBarContentPanel;
    private JPanel topBar;

    /**
     * Creates editor for the specified node.
     */
    public NodeEditorBase(T node, DemoEditor demoEditor) {
        super(new MigLayout("insets 0, gapy 0, fillx"));

        notNull(node, "node");
        notNull(demoEditor, "editor");

        this.node = node;
        this.demoEditor = demoEditor;

        buildBasicUi();

        node.addNodeListener(nodeListener);
        node.getRootNode().addNodeListener(rootNodeListener);
    }

    /**
     * @return edited node.
     */
    public T getNode() {
        return node;
    }

    private void buildBasicUi() {
        // Add indenter
        indenter = new JPanel(new MigLayout("insets 0"));
        add(indenter, "align left");
        forceHeight(indenter, barHeight);
        debugBorderize(indenter, Color.blue);

        topBar = new JPanel(new MigLayout("insets 0, gap 0"));
        add(topBar);

        // Expand collapse button
        expandToggle = new JLabel(expanded ? arrowDownIcon : arrowRightIcon);
        forceSize(expandToggle, barHeight, barHeight);
        topBar.add(expandToggle, "align left");
        topBar.addMouseListener(new MouseInputAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                setExpanded(!expanded);
            }
        });

        // Name of node
        nameLabel = new JLabel(node.getName());
        topBar.add(nameLabel, "align left");
        debugBorderize(nameLabel, Color.green);

        // Space for other things in top bar
        otherTopBarContentPanel = new JPanel(new MigLayout("insets 0"));
        topBar.add(otherTopBarContentPanel, "");
        forceSize(otherTopBarContentPanel, OTHER_TOPBAR_WIDTH, barHeight);
        debugBorderize(otherTopBarContentPanel, Color.YELLOW);

        // Time editor bar
        timeEditorBar = new JPanel(new MigLayout("left, insets 0, gapy 0, fillx"));
        add(timeEditorBar, "growx, pushx, height " + barHeight);

        debugBorderize(timeEditorBar, Color.CYAN);

        // Tune background
        updateColors();

        // Space for children
        childPanel = new JPanel(new MigLayout("insets 0, gapy 0, fillx"));
        /*
        childPanel.setBorder(new MatteBorder(0, 0, 2, 0, (Color) null));
         */
        add(childPanel, "south, grow, push");

        // Add UIs for current children
        final Array<? extends DemoNode> children = node.getChildren();
        for (int i = 0; i < children.size; i++) {
            addChildUi(children.get(i));
        }

        // Do implementation specific ui building
        buildUi(otherTopBarContentPanel, timeEditorBar, node);

        debugBorderize(NodeEditorBase.this, Color.RED);

        updateNodeUi();
        updateIndent();
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
                setSizes(childPanel, null);
            }
            else {
                setSizes(childPanel, ZERO_SIZE);
            }

            // Refresh
            refreshLayout();
        }
    }

    private void setSizes(final JPanel component, Dimension collapsedSize) {
        component.setMaximumSize(collapsedSize);
        component.setMinimumSize(collapsedSize);
        component.setPreferredSize(collapsedSize);
    }

    private void updateIndent() {
        final int maxIndent = indentWidth * (node.getRootNode().getMaxDepth());
        final int ownIndent = (node.getDepth()) * indentWidth;
        final int surplusIndent = maxIndent - ownIndent;

        forceSize(indenter, ownIndent, barHeight);
        forceSize(nameLabel, LABEL_WIDTH + surplusIndent, barHeight);

        refreshLayout();
    }

    private void forceSize(final JComponent component, int width, final int height) {
        final Dimension size = new Dimension(width, height);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
    }

    private void forceHeight(final JComponent component, final int height) {
        final Dimension size = new Dimension(component.getWidth(), height);
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

            // If this was the first added child, expand this panel
            if (childUis.size() == 1) expandAll();

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

                // If this was the last removed child, collapse this panel
                if (childUis.size() == 0) collapseAll();

                refreshLayout();
            }
        }
    }

    private void updateNodeUi() {
        // NOTE: This is called quite often (when a parameter value is updated in a running demo)
        nameLabel.setText(StringUtils.capitalize(node.getName()));
        expandToggle.setVisible(node.getChildCount() > 0);
        doUpdateNodeUi(node);
    }


    protected abstract void doUpdateNodeUi(T node);


    /**
     * Create node type specific UIs.
     * @param otherTopBarContentPanel
     * @param valueEditorPanel
     */
    protected abstract void buildUi(JPanel otherTopBarContentPanel, JPanel valueEditorPanel, T node);

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

    private void updateColors() {
        final Color color = calculateTintedBackgroundColor();
        if (!color.equals(topBar.getBackground())) {
            setTopBarColor(color);
        }
    }

    protected void setTopBarColor(Color color) {
        topBar.setBackground(color);
        otherTopBarContentPanel.setBackground(color);
        topBar.setBorder(new MatteBorder(2, 0, 0, 0, color.darker()));
    }

    protected Color getEditorColor() {
        return null;
    }

    protected double getEditorColorMixStrength() {
        return 0.1;
    }

    protected Color calculateTintedBackgroundColor() {
        final Color editorColor = getEditorColor();
        return demoEditor.getTintedBackgroundColor(editorColor == null ? 0 : getEditorColorMixStrength(), editorColor);
    }

}
