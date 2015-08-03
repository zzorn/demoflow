package org.demoflow;

import net.miginfocom.swing.MigLayout;
import org.demoflow.demo.Demo;
import org.demoflow.demo.DemoListener;
import org.flowutils.Check;
import org.uiflow.desktop.ui.SimpleFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 *
 */
// TODO: Editing UI
public final class Editor {

    private final View view;

    private Demo demo;

    public final DecimalFormat numberFormat = new DecimalFormat("#.00");

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

    public JProgressBar progressBar;

    /**
     * @param view view to show demos on.
     */
    public Editor(View view) {
        Check.notNull(view, "viewer");
        this.view = view;

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
        if (demo != this.demo) {
            if (this.demo != null) {
                this.demo.removeListener(demoListener);
            }

            this.demo = demo;
            view.setDemo(this.demo);

            if (this.demo != null) {
                this.demo.addListener(demoListener);
            }
        }
    }

    private void buildUi() {

        final JPanel mainPanel = new JPanel(new MigLayout());

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
        mainPanel.add(pause);

        // Speed
        final int maxSliderSpeed = 10000;
        final int defaultSliderSpeed = maxSliderSpeed / 4;
        final JSlider speedSlider = new JSlider(SwingConstants.HORIZONTAL, 0, maxSliderSpeed, defaultSliderSpeed);
        speedSlider.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                if (demo != null) {
                    double speed = 1.0 * speedSlider.getValue() / defaultSliderSpeed;
                    demo.setSpeed(speed);
                }
            }
        });
        mainPanel.add(new JLabel("Speed"));
        mainPanel.add(speedSlider);
        mainPanel.add(createButton("1:1", new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    speedSlider.setValue(defaultSliderSpeed);
                }
            }
        }));

        // Restart button
        mainPanel.add(createButton("Restart", new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (demo != null) {
                    demo.reset();
                }
            }
        }));

        // Demo position indicator
        progressBar = new JProgressBar();
        mainPanel.add(progressBar, "width :250:");

        new SimpleFrame("Demo Editor", mainPanel, 1024, 400, WindowConstants.HIDE_ON_CLOSE);
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


}
