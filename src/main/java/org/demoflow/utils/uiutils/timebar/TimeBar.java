package org.demoflow.utils.uiutils.timebar;

import net.miginfocom.swing.MigLayout;
import org.flowutils.MathUtils;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 *
 */
public class TimeBar extends JPanel {

    private static final int DRAG_BUTTON = MouseEvent.BUTTON1;
    private static final double MOUSE_WHEEL_SCALING_FACTOR = 1.5;
    private static final Color BACKGROUND_COLOR = new Color(100, 100, 100);
    private static final Color CURRENT_TIME_COLOR = new Color(255, 0, 0);

    private final TimeBarModel timeBarModel;

    private ArrayList<Handle> handles = new ArrayList<>();

    private Handle draggedHandle;

    public TimeBar(double duration) {
        this(new TimeBarModel(duration));
    }

    public TimeBar(final TimeBarModel timeBarModel) {
        super(new MigLayout());
        this.timeBarModel = timeBarModel;

        handles.add(new VisibleAreaStartHandle(timeBarModel));
        handles.add(new VisibleAreaEndHandle(timeBarModel));
        handles.add(new VisibleAreaHandle(timeBarModel));

        addMouseWheelListener(new MouseWheelListener() {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                final double scale = e.getPreciseWheelRotation();
                double zoomFactor = Math.pow(MOUSE_WHEEL_SCALING_FACTOR, scale);
                final double duration = timeBarModel.getVisibleDuration();
                final double newDuration = zoomFactor * duration;
                final double marginMove = 0.5 * (newDuration - duration);
                timeBarModel.setVisibleArea(timeBarModel.getVisibleAreaStartTime() - marginMove,
                                            timeBarModel.getVisibleAreaEndTime() + marginMove);
                repaint();
            }
        });

        addMouseListener(new MouseInputAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == DRAG_BUTTON) {
                    // Determine location of the press on the timebar
                    final double clickedTime = getTimeAtMouseLocation(e);

                    // Find overlapping handle
                    for (Handle handle : handles) {
                        if (handle.overlaps(clickedTime, getWidth())) {
                            // Start dragging this handle
                            draggedHandle = handle;
                            break;
                        }
                    }
                }
            }

            @Override public void mouseReleased(MouseEvent e) {
                if (e.getButton() == DRAG_BUTTON && draggedHandle != null) {
                    draggedHandle.moveTo(getTimeAtMouseLocation(e));
                    draggedHandle = null;
                    repaint();
                }
            }

            @Override public void mouseDragged(MouseEvent e) {
                if (e.getButton() == DRAG_BUTTON && draggedHandle != null) {
                    draggedHandle.moveTo(getTimeAtMouseLocation(e));
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseInputAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                if (draggedHandle != null) {
                    draggedHandle.moveTo(getTimeAtMouseLocation(e));
                    repaint();
                }
            }
        });


        timeBarModel.addListener(new TimeBarModelListener() {
            @Override public void onCurrentTimeChanged(TimeBarModel timeBar) {
                repaint();
            }

            @Override public void onVisibleAreaChanged(TimeBarModel timeBar) {
                repaint();
            }

            @Override public void onStartEndChanged(TimeBarModel timeBar) {
                repaint();
            }
        });
    }

    private double getTimeAtMouseLocation(MouseEvent e) {
        return MathUtils.map(e.getX(),
                             0,
                             getWidth(),
                             timeBarModel.getStartTime(),
                             timeBarModel.getEndTime());
    }

    public TimeBarModel getTimeBarModel() {
        return timeBarModel;
    }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Paint background
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Paint handles
        for (int i = handles.size() - 1; i >= 0; i--) {
            Handle handle = handles.get(i);

            int x = handle.mapStartLocation(0, getWidth());
            int w = handle.mapDuration(0, getWidth());

            g2.setColor(handle.getColor());
            g2.fillRect(x, 0, w, getHeight());
            g2.draw3DRect(x, 0, w, getHeight(), true);
        }

        // Draw current time
        int currentTimeX = (int) MathUtils.map(timeBarModel.getCurrentTime(),
                                               timeBarModel.getStartTime(), timeBarModel.getEndTime(),
                                               0, getWidth());
        g2.setColor(CURRENT_TIME_COLOR);
        g2.drawLine(currentTimeX, 0, currentTimeX, getHeight());
    }

    private interface Handle {
        double getStart();
        double getEnd();

        boolean overlaps(double location, int componentWidth);

        int getMarginInPixels();
        Color getColor();

        int mapStartLocation(int targetStart, int targetEnd);
        int mapEndLocation(int targetStart, int targetEnd);
        int mapDuration(int targetStart, int targetEnd);

        void moveTo(double newStart);
    }

    private abstract class HandleBase implements Handle {

        private Color color;
        private int pixelMargin;

        public HandleBase() {
            this(new Color(144, 58, 56));
        }

        public HandleBase(Color color) {
            this(color, 4);
        }

        public HandleBase(Color color, int pixelMargin) {
            this.color = color;
            this.pixelMargin = pixelMargin;
        }

        @Override public double getEnd() {
            return getStart();
        }

        @Override public final boolean overlaps(double location, int componentWidth) {
            final double duration = timeBarModel.getEndTime() - timeBarModel.getStartTime();
            double relativeMarginSize = MathUtils.map(getMarginInPixels(), 0, componentWidth, 0, duration);
            return (location >= getStart() - relativeMarginSize) && (location <= getEnd() + relativeMarginSize);
        }

        @Override public final int getMarginInPixels() {
            return pixelMargin;
        }

        @Override public final Color getColor() {
           return color;
        }

        @Override public final int mapStartLocation(int targetStart, int targetEnd) {
            return -pixelMargin + (int) MathUtils.map(getStart(), timeBarModel.getStartTime(), timeBarModel.getEndTime(), targetStart, targetEnd);
        }

        @Override public final int mapEndLocation(int targetStart, int targetEnd) {
            return +pixelMargin + (int) MathUtils.map(getEnd(), timeBarModel.getStartTime(), timeBarModel.getEndTime(), targetStart, targetEnd);
        }

        @Override public int mapDuration(int targetStart, int targetEnd) {
            return pixelMargin * 2 + (int) MathUtils.map(getEnd() - getStart(), timeBarModel.getStartTime(), timeBarModel.getEndTime(), targetStart, targetEnd);
        }
    }

    private class VisibleAreaHandle extends HandleBase {
        private final TimeBarModel timeBarModel;

        public VisibleAreaHandle(TimeBarModel timeBarModel) {
            super(new Color(159, 187, 217));
            this.timeBarModel = timeBarModel;
        }

        @Override public double getStart() {
            return timeBarModel.getVisibleAreaStartTime();
        }

        @Override public double getEnd() {
            return timeBarModel.getVisibleAreaEndTime();
        }

        @Override public void moveTo(double newStart) {
            final double visibleAreaLength = timeBarModel.getVisibleAreaEndTime() -
                                             timeBarModel.getVisibleAreaStartTime();
            timeBarModel.setVisibleArea(newStart, newStart + visibleAreaLength);
        }
    }

    private class VisibleAreaStartHandle extends HandleBase {
        private final TimeBarModel timeBarModel;

        public VisibleAreaStartHandle(TimeBarModel timeBarModel) {
            super(new Color(83, 166, 217));
            this.timeBarModel = timeBarModel;
        }

        @Override public double getStart() {
            return timeBarModel.getVisibleAreaStartTime();
        }

        @Override public void moveTo(double newStart) {
            timeBarModel.setVisibleAreaStartTime(newStart);
        }
    }

    private class VisibleAreaEndHandle extends HandleBase {
        private final TimeBarModel timeBarModel;

        public VisibleAreaEndHandle(TimeBarModel timeBarModel) {
            super(new Color(83, 166, 217));
            this.timeBarModel = timeBarModel;
        }

        @Override public double getStart() {
            return timeBarModel.getVisibleAreaEndTime();
        }

        @Override public void moveTo(double newStart) {
            timeBarModel.setVisibleAreaEndTime(newStart);
        }
    }

}
