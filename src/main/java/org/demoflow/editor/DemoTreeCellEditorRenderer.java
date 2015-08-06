package org.demoflow.editor;

import net.miginfocom.swing.MigLayout;
import org.demoflow.DemoComponentManager;
import org.demoflow.demo.Demo;
import org.demoflow.node.DemoNode;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.Calculator;
import org.demoflow.utils.UiUtils;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 *
 *
 * @deprecated not using trees any more
 */
public class DemoTreeCellEditorRenderer extends JPanel implements TreeCellRenderer, TreeCellEditor {

    private static final int DEFAULT_PARAMETER_NAME_WIDTH_PIXELS = 400;
    private final Demo demo;
    private final JFrame rootFrame;

    private final DemoComponentManager demoComponentManager;

    private JLabel label;
    private JLabel valueLabel;
    private JLabel expandIcon;

    private final Color selectionBackground;
    private final Color normalBackground;

    private final ArrayList<CellEditorListener> listeners = new ArrayList<>();

    private DemoTreeNode editedTreeNode;
    private JTree tree;

    private double timeScaling = 1;
    private final JComboBox<Class<? extends Calculator>> calculatorSelector;

    private boolean canEditCalculator = false;
    private final DefaultComboBoxModel calculatorSelectorModel;
    private final JLabel calculatorLabel;

    public DemoTreeCellEditorRenderer(Demo demo, final DemoComponentManager demoComponentManager, final JFrame rootFrame) {
        super(new MigLayout());

        notNull(demo, "demo");
        notNull(rootFrame, "rootFrame");
        notNull(demoComponentManager, "demoComponentManager");

        this.demo = demo;
        this.rootFrame = rootFrame;
        this.demoComponentManager = demoComponentManager;

        setBorder(new LineBorder(Color.RED));

        // Node name
        label = new JLabel("");
        add(label);

        // Calculator label
        calculatorLabel = new JLabel("");
        add(calculatorLabel);

        // Calculator selector
        calculatorSelectorModel = new DefaultComboBoxModel();
        calculatorSelector = new JComboBox<>(calculatorSelectorModel);
        calculatorSelector.setSelectedItem(null);
        calculatorSelector.setRenderer(new CalculatorComboBoxRenderer());
        add(calculatorSelector);
        calculatorSelector.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (canEditCalculator) {
                    final Class<? extends Calculator> selectedType = (Class<? extends Calculator>) calculatorSelector.getSelectedItem();

                    final Parameter parameter = (Parameter) getDemoNode();
                    final int response = JOptionPane.showConfirmDialog(rootFrame,
                                                                       "Changing the calculator removes the previous calculator.",
                                                                       "Change calculator for " + parameter.getId() + "?",
                                                                       JOptionPane.OK_CANCEL_OPTION,
                                                                       JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.OK_OPTION) {
                        parameter.setCalculator(demoComponentManager.createCalculator(selectedType));
                    }
                }
            }
        });


        // Value
        valueLabel = new JLabel("");
        add(valueLabel);

        selectionBackground = DefaultLookup.getColor(this, ui, "Tree.selectionBackground");
        normalBackground    = DefaultLookup.getColor(this, ui, "Tree.textBackground");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        this.tree = tree;
        editedTreeNode = (DemoTreeNode) value;

        final DemoNode demoNode = getDemoNode();
        calculatorSelector.setVisible(false);
        if (demoNode instanceof Parameter) {
            calculatorLabel.setText(getCalculatorTypeName(((Parameter) demoNode).getCalculator()));
            calculatorLabel.setVisible(true);
        }
        else {
            calculatorLabel.setVisible(false);
        }


        updateView(demoNode, selected);

        return this;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree,
                                                Object value,
                                                boolean selected,
                                                boolean expanded,
                                                boolean leaf,
                                                int row) {
        this.tree = tree;
        editedTreeNode = (DemoTreeNode) value;

        final DemoNode demoNode = getDemoNode();

        calculatorLabel.setVisible(false);
        if (demoNode instanceof Parameter) {

            // Update valid calculator types to the combo box used when creating a new calculator
            final Parameter parameter = (Parameter) demoNode;
            final Class parameterType = parameter.getType();
            final List<Class<? extends Calculator>> calculatorTypes = demoComponentManager.getCalculatorTypes(parameterType);
            calculatorSelector.setModel(new DefaultComboBoxModel<Class<? extends Calculator>>(new Vector<Class<? extends Calculator>>(calculatorTypes)));

            /*
            calculatorSelectorModel.removeAllElements();
            for (Class<? extends Calculator> calculatorType : calculatorTypes) {
                calculatorSelectorModel.addElement(calculatorType);
            }
            */

            // Set the selected calculator
            final Calculator calculator = parameter.getCalculator();
            calculatorSelector.setSelectedItem(calculator == null ? null : calculator.getClass());

            calculatorSelector.setVisible(true);

            canEditCalculator = true;
        }
        else {
            calculatorSelector.setVisible(false);
        }


        updateView(demoNode, selected);

        return this;
    }

    private void updateView(DemoNode demoNode, boolean selected) {
        canEditCalculator = false;


        // Color selection according to look and feel
        setBackground(selected ? selectionBackground : normalBackground);

        // TODO: Draw focus as well

        label.setText(demoNode.toString());

    }

    private DemoNode getDemoNode() {
        return editedTreeNode.getDemoNode();
    }


    @Override public Object getCellEditorValue() {
        return editedTreeNode;
    }

    @Override public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override public boolean stopCellEditing() {

        editedTreeNode = null;
        canEditCalculator = false;

        // Notify listeners
        final ChangeEvent event = new ChangeEvent(this);
        for (CellEditorListener listener : listeners) {
            listener.editingStopped(event);
        }

        return true;
    }

    @Override public void cancelCellEditing() {

        editedTreeNode = null;
        canEditCalculator = false;

        // Notify listeners
        final ChangeEvent event = new ChangeEvent(this);
        for (CellEditorListener listener : listeners) {
            listener.editingCanceled(event);
        }
    }

    @Override public void addCellEditorListener(CellEditorListener l) {
        listeners.add(l);
    }

    @Override public void removeCellEditorListener(CellEditorListener l) {
        listeners.remove(l);
    }

    @Override public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {

        Dimension size = super.getPreferredSize();
        size.width = calculateCellWidth();
        return size;
    }

    @Override
    public void setBounds(final int x, final int y, final int width, final int height) {

        super.setBounds(x, y, width, height);
//        super.setBounds(x, y, Math.min(tree.getWidth() - x, width), height);
    }

    private int calculateCellWidth() {
        // Indent amount
        final int depth = getDemoNode().getDepth();
        final int treeIndent = UiUtils.getJTreeIndent(tree);
        final int currentIndent = depth * treeIndent;

        // Width of demo
        int demoWidthPixels = (int) (timeScaling * demo.getDurationSeconds());

        // Width of parameter name
        int paramNameWidth = DEFAULT_PARAMETER_NAME_WIDTH_PIXELS;

        // Maximum depth of any node determines maximum indent level
        int maximumIndent = treeIndent * demo.getMaxDepth();

        // Determine total row width
        int width = maximumIndent - currentIndent + paramNameWidth + demoWidthPixels;

        return width;
    }

    public double getTimeScaling() {
        return timeScaling;
    }

    public void setTimeScaling(double timeScaling) {
        this.timeScaling = timeScaling;
    }

    private String getCalculatorTypeName(Calculator calculator) {
        return calculator == null ?
               "No calculator" :
               calculator.getClass().getSimpleName().replace("Calculator", "");
    }

}
