package org.demoflow.editor;

import com.bulenkov.darcula.DarculaLaf;
import net.miginfocom.swing.MigLayout;
import org.demoflow.DemoComponentManager;
import org.demoflow.demo.DefaultDemo;
import org.demoflow.editor.nodeeditor.DemoNodeEditor;
import org.demoflow.view.View;
import org.demoflow.demo.Demo;
import org.demoflow.demo.DemoListener;
import org.flowutils.LogUtils;
import org.flowutils.StringUtils;
import org.uiflow.desktop.ColorUtils;
import org.uiflow.desktop.ui.SimpleFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import static org.flowutils.Check.notNull;

/**
 * Editor UI for editing a demo.
 */
public final class DemoEditor {

    private static final int DEFAULT_EXPAND_DEPTH = 1;

    private static final DecimalFormat numberFormat = new DecimalFormat("#.00");

    private final View view;
    private final DemoComponentManager demoComponentManager;
    private final EditorManager editorManager;
    private final Object setDemoLock = new Object();

    private Demo demo;

    private JProgressBar progressBar;
    private SimpleFrame rootFrame;
    private DemoNodeEditor demoNodeEditor;
    private JPanel treePanel;

    private Color defaultBackgroundColor;

    private File lastDirectoryPath = new File(".");

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

        @Override public void onRestart(Demo demo) {
            showStatus("Restarting", 0);
        }

        @Override public void onShutdown(Demo demo) {
            showStatus("Stopped", 0);
        }

        @Override public void onCompleted() {
            showStatus("Finished", 1);
        }
    };
    private JPanel demoViewPanel;
    private JFileChooser fileChooser;
    private JCheckBox restartCheckBox;
    private static final int MAX_SLIDER_SPEED = 10000;
    private static final int DEFAULT_SLIDER_SPEED = MAX_SLIDER_SPEED / 4;
    private JSlider speedSlider;

    /**
     * @param view view to show demos on.
     * @param demoComponentManager used to access the available effects, calculators, etc.
     * @param editorManager provides value editors.
     */
    public DemoEditor(View view, DemoComponentManager demoComponentManager, EditorManager editorManager) {
        notNull(view, "viewer");
        notNull(demoComponentManager, "demoComponentManager");
        notNull(editorManager, "editorManager");

        this.view = view;
        this.demoComponentManager = demoComponentManager;
        this.editorManager = editorManager;

        setDarkLookAndFeel();

        buildUi();
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
        synchronized (setDemoLock) {
            if (demo != this.demo) {
                if (this.demo != null) {
                    this.demo.removeListener(demoListener);
                }

                this.demo = demo;
                view.setDemo(this.demo);

                if (this.demo != null) {
                    this.demo.addListener(demoListener);

                    // Update auto-restart of the demo to correspond to the UI setting (as it is not saved with the demo)
                    if (restartCheckBox != null) this.demo.setAutoRestart(restartCheckBox.isSelected());

                    // Reset speed to default
                    resetDemoSpeed();
                }

                setDemoToView(this.demo);
            }
        }
    }

    public final EditorManager getEditorManager() {
        return editorManager;
    }

    private void buildUi() {

        final JPanel mainPanel = new JPanel(new MigLayout());

        defaultBackgroundColor = mainPanel.getBackground();

        mainPanel.add(createTopRow(), "wrap");

        mainPanel.add(createEffectView(), "grow, push");

        rootFrame = new SimpleFrame("Demo Editor", mainPanel, 1024, 800, WindowConstants.HIDE_ON_CLOSE);

        fileChooser = createFileChooserDialog();
    }

    private JPanel createTopRow() {
        final JPanel topRow = new JPanel(new MigLayout());

        // Load button
        topRow.add(new JButton(new AbstractAction("Open") {
            @Override public void actionPerformed(ActionEvent e) {
                showOpenDialog();
            }
        }));

        // Save button
        topRow.add(new JButton(new AbstractAction("Save as...") {
            @Override public void actionPerformed(ActionEvent e) {
                showSaveDialog();
            }
        }));

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
        speedSlider = new JSlider(SwingConstants.HORIZONTAL, 0, MAX_SLIDER_SPEED, DEFAULT_SLIDER_SPEED);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                if (demo != null) {
                    double speed = 1.0 * speedSlider.getValue() / DEFAULT_SLIDER_SPEED;

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
                resetDemoSpeed();
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
        restartCheckBox = new JCheckBox(new AbstractAction("Autorestart") {
            @Override public void actionPerformed(ActionEvent e) {
                demo.setAutoRestart(((JCheckBox) e.getSource()).isSelected());
            }
        });
        restartCheckBox.setSelected(demo != null && demo.isAutoRestart());
        topRow.add(restartCheckBox);

        // Demo position indicator
        progressBar = new JProgressBar();
        topRow.add(progressBar, "width :250:");


        return topRow;
    }

    private void resetDemoSpeed() {
        speedSlider.setValue(DEFAULT_SLIDER_SPEED);
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

        // Scroll pane for tree
        demoViewPanel = new JPanel(new MigLayout("fill"));

        // Scroll pane for the tree
        JScrollPane scrollPane = new JScrollPane(demoViewPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(24); // Fix slow scrolling
        scrollPane.getHorizontalScrollBar().setUnitIncrement(24); // Fix slow scrolling

        this.treePanel.add(scrollPane, "south, grow, push, gaptop 5");

        return this.treePanel;
    }

    private void setDemoToView(Demo demo) {
        if( demoNodeEditor != null) {
            demoViewPanel.remove(demoNodeEditor);
            demoNodeEditor = null;
        }

        if (demo != null) {
            demoNodeEditor = new DemoNodeEditor(demo, this);
            demoViewPanel.add(demoNodeEditor, "grow");

            moderateExpandTree();
        }

        demoViewPanel.revalidate();
        demoViewPanel.repaint();
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

    private void collapseAll() {
        if (demoNodeEditor != null) {
            demoNodeEditor.collapseAll();
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


    private void setDarkLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new DarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            LogUtils.getLogger().error("Could not set the dark Darcula look and feel: " + e.getMessage(), e);
        }
    }

    public Color getTintedBackgroundColor(double tintAmount, Color tintColor) {
        return ColorUtils.mixColors(tintAmount, defaultBackgroundColor, tintColor);
    }

    private void showOpenDialog() {
        // Open load dialog
        final int response = fileChooser.showOpenDialog(rootFrame);
        if (response == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();

            setDemo(null);

            // Load the demo in a separate thread, to allow UI to stay responsive
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        // Load new demo
                        Demo loadedDemo = new DefaultDemo();
                        loadedDemo.load(selectedFile, getComponentManager());

                        // Switch the current demo
                        setDemo(loadedDemo);
                    } catch (Exception e1) {
                        // Show error dialog if load failed
                        LogUtils.getLogger().warn("Could not open demo because: " + e1.getMessage(), e1);
                        JOptionPane.showMessageDialog(rootFrame,
                                                      "Problem opening '"+selectedFile.getName()+"': " + e1.getMessage(),
                                                      "Could not load demo",
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }
    }

    private void showSaveDialog() {
        if (demo == null) return;

        // Pre-suggest a name for the demo
        fileChooser.setSelectedFile(new File(StringUtils.identifierFromName(demo.getName()) + ".demo"));

        // Open save dialog
        final int response = fileChooser.showSaveDialog(rootFrame);
        if (response == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();

            // Confirm overwrites of existing files
            if (selectedFile.exists()) {
                final int overwriteResult = JOptionPane.showConfirmDialog(rootFrame,
                                                                          "The file '" +
                                                                          selectedFile +
                                                                          "'\nalready exists, do you want to overwrite it?",
                                                                          "Overwrite " +
                                                                          selectedFile.getName() +
                                                                          " ?",
                                                                          JOptionPane.OK_CANCEL_OPTION);

                if (overwriteResult != JOptionPane.OK_OPTION) {
                    // Return if overwrite permission not given
                    return;
                }
            }

            // Save file
            try {
                demo.save(selectedFile);
            } catch (Exception e1) {
                // Show error dialog if save failed
                LogUtils.getLogger().warn("Could not save demo because: " + e1.getMessage(), e1);
                JOptionPane.showMessageDialog(rootFrame,
                                              "Problem saving '"+selectedFile.getName()+"': " + e1.getMessage(),
                                              "Could not save demo",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JFileChooser createFileChooserDialog() {
        JFileChooser fileChooser = new JFileChooser(lastDirectoryPath);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Demo files", "demo"));
        return fileChooser;
    }
}
