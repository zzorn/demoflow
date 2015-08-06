package org.demoflow.editor;

import net.miginfocom.swing.MigLayout;
import org.demoflow.DemoComponentManager;
import org.demoflow.editor.nodeeditor.DemoNodeEditor;
import org.demoflow.view.View;
import org.demoflow.demo.Demo;
import org.demoflow.demo.DemoListener;
import org.uiflow.desktop.ui.SimpleFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import static org.flowutils.Check.notNull;

/**
 *
 */
// TODO: Editing UI
public final class Editor {

    private static final int DEFAULT_EXPAND_DEPTH = 1;

    private static final DecimalFormat numberFormat = new DecimalFormat("#.00");

    private final View view;
    private final DemoComponentManager demoComponentManager;

    private Demo demo;

    private JProgressBar progressBar;
    private SimpleFrame rootFrame;
    private DemoNodeEditor demoNodeEditor;
    private JPanel treePanel;

    private final DemoListener demoListener = new DemoListener() {
        @Override
        public void onProgress(Demo demo, double currentDemoTime, double totalDemoTime, double relativeDemoTime) {
            showProgress(currentDemoTime, totalDemoTime, relativeDemoTime);
        }

        @Override public void onPauseChanged(Demo demo, boolean paused) {
        }

        @Override public void onSetup(Demo demo) {
            showStatus("Starting", 0);
        }

        @Override public void onShutdown(Demo demo) {
            showStatus("Stopped", 0);
        }

        @Override public void onCompleted() {
            showStatus("Finished", 1);
        }
    };
    private JPanel demoViewPanel;

    /**
     * @param view view to show demos on.
     * @param demoComponentManager used to access the available effects, calculators, etc.
     */
    public Editor(View view, DemoComponentManager demoComponentManager) {
        this(view, demoComponentManager, null);
    }

    /**
     * @param view view to show demos on.
     * @param demoComponentManager used to access the available effects, calculators, etc.
     * @param demo demo to edit, or null if it is specified later.
     */
    public Editor(View view, DemoComponentManager demoComponentManager, Demo demo) {
        notNull(view, "viewer");
        notNull(demoComponentManager, "demoComponentManager");

        this.view = view;
        this.demoComponentManager = demoComponentManager;

        buildUi();

        setDemo(demo);
    }

    /**
     * @return the currently edited demo.
     */
    public Demo getDemo() {
        return demo;
    }

    /**
     * @param demo the demo to edit.
     */
    public void setDemo(Demo demo) {
        if (demo != this.demo) {
            if (this.demo != null) {
                this.demo.removeListener(demoListener);
            }

            this.demo = demo;
            view.setDemo(this.demo);

            if (this.demo != null) {
                this.demo.addListener(demoListener);
            }

            setDemoToView(this.demo);
        }
    }

    private void buildUi() {

        final JPanel mainPanel = new JPanel(new MigLayout());

        mainPanel.add(createTopRow(), "wrap");

        mainPanel.add(createEffectView(), "grow, push");

        rootFrame = new SimpleFrame("Demo Editor", mainPanel, 1024, 800, WindowConstants.HIDE_ON_CLOSE);
    }

    private JPanel createTopRow() {
        final JPanel topRow = new JPanel(new MigLayout());

        // Pause button
        final JToggleButton pause = new JToggleButton("Pause");
        pause.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    demo.setPaused(!demo.isPaused());
                    pause.setSelected(demo.isPaused());
                }
            }
        });
        topRow.add(pause);

        // Speed slider
        final int maxSliderSpeed = 10000;
        final int defaultSliderSpeed = maxSliderSpeed / 4;
        final JSlider speedSlider = new JSlider(SwingConstants.HORIZONTAL, 0, maxSliderSpeed, defaultSliderSpeed);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                if (demo != null) {
                    double speed = 1.0 * speedSlider.getValue() / defaultSliderSpeed;

                    // Square speed to allow for faster and slower playing
                    speed *= speed;

                    demo.setSpeed(speed);
                }
            }
        });
        topRow.add(new JLabel("Speed"));
        topRow.add(speedSlider);
        topRow.add(createButton("1:1", new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    speedSlider.setValue(defaultSliderSpeed);
                }
            }
        }));

        // Restart button
        topRow.add(createButton("Restart", new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    demo.reset();
                }
            }
        }));

        // Auto-restart checkbox
        topRow.add(new JCheckBox(new AbstractAction("Autorestart") {
            @Override public void actionPerformed(ActionEvent e) {
                demo.setAutoRestart(((JCheckBox) e.getSource()).isSelected());
            }
        }));

        // Demo position indicator
        progressBar = new JProgressBar();
        topRow.add(progressBar, "width :250:");

        return topRow;
    }

    private JComponent createEffectView() {
        // Panel to place tree and related controls in
        this.treePanel = new JPanel(new MigLayout("fill"));

        // Collapse button
        this.treePanel.add(new JButton(new AbstractAction("Collapse") {
            @Override public void actionPerformed(ActionEvent e) {
                moderateExpandTree();
            }
        }));

        // Expand button
        this.treePanel.add(new JButton(new AbstractAction("Expand") {
            @Override public void actionPerformed(ActionEvent e) {
                expandAll();
            }
        }), "pushx");

        // Scroll pane for the tree
        demoViewPanel = new JPanel(new MigLayout("fill"));
        JScrollPane scrollPane = new JScrollPane(demoViewPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(24); // Fix slow scrolling

        this.treePanel.add(scrollPane, "south, grow, push, gaptop 5");

        return this.treePanel;
    }

    private void setDemoToView(Demo demo) {
        if (demo != null) {
            if( demoNodeEditor != null) {
                demoViewPanel.remove(demoNodeEditor);
            }
            demoNodeEditor = new DemoNodeEditor(demo, this);
            demoViewPanel.add(demoNodeEditor, "grow");

            moderateExpandTree();

            demoViewPanel.revalidate();
            demoViewPanel.repaint();
        }
        else {
            if( demoNodeEditor != null) {
                demoViewPanel.remove(demoNodeEditor);
            }
            demoNodeEditor = null;
        }
    }

    private void moderateExpandTree() {
        if (demoNodeEditor != null) {
            demoNodeEditor.expandToDepth(DEFAULT_EXPAND_DEPTH);
        }
    }

    private void expandAll() {
        if (demoNodeEditor != null) {
            demoNodeEditor.expandAll();
        }
    }

    private JButton createButton(final String name, final ActionListener listener) {
        final JButton button = new JButton(name);
        button.addActionListener(listener);
        return button;
    }

    private void showProgress(double currentDemoTime, double totalDemoTime, double relativeDemoTime) {
        if (progressBar != null) {
            final String progressText = numberFormat.format(currentDemoTime) + " s / " +
                                        numberFormat.format(totalDemoTime) + " s (" +
                                        numberFormat.format(100 * relativeDemoTime) + "%)";
            progressBar.setString(progressText);
            progressBar.setStringPainted(true);
            progressBar.setMaximum(1000);
            progressBar.setValue((int) (1000 * relativeDemoTime));
        }
    }

    private void showStatus(String status, double progress) {
        if (progressBar != null) {
            progressBar.setString(status);
            progressBar.setStringPainted(true);
            progressBar.setMaximum(1000);
            progressBar.setValue((int) (1000 * progress));
        }
    }


    public DemoComponentManager getComponentManager() {
        return demoComponentManager;
    }

    public SimpleFrame getRootFrame() {
        return rootFrame;
    }
}
