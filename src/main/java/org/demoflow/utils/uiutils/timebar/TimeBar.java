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
    private static final double MOUSE_WHEEL_SCALING_FACTOR = 1.27;
    private static final Color BACKGROUND_COLOR = new Color(100, 100, 100);
    private static final Color CURRENT_TIME_COLOR = new Color(255, 243, 227, 255);
    private static final Color CURRENT_TIME_COLOR_BORDER1 = new Color(255, 219, 117, 94);
    private static final Color CURRENT_TIME_COLOR_BORDER2 = new Color(242, 195, 91, 43);
    private static final int CURRENT_TIME_RADIUS = 1;
    private static final Color START_HANDLE_COLOR = new Color(210, 171, 75);
    private static final Color END_HANDLE_COLOR = new Color(208, 126, 52);
    private static final Color VISIBLE_HANDLE_COLOR = new Color(143, 124, 54);
    private static final Color DEFAULT_HANDLE_COLOR = new Color(172, 100, 84);

    private final TimeBarModel timeBarModel;

    private ArrayList<Handle> handles = new ArrayList<>();

    private Handle defaultDragHandle;

    private Handle draggedHandle;
    private double dragOffset;

    public TimeBar(double duration) {
        this(new TimeBarModel(duration));
    }

    public TimeBar(final TimeBarModel timeBarModel) {
        super(new MigLayout());
        this.timeBarModel = timeBarModel;

        handles.add(new VisibleAreaStartHandle(timeBarModel));
        handles.add(new VisibleAreaEndHandle(timeBarModel));
        final VisibleAreaHandle visibleAreaHandle = new VisibleAreaHandle(timeBarModel);
        handles.add(visibleAreaHandle);
        defaultDragHandle = visibleAreaHandle;

        addMouseWheelListener(new MouseWheelListener() {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                final double scale = e.getPreciseWheelRotation();
                double zoomFactor = Math.pow(MOUSE_WHEEL_SCALING_FACTOR, scale);

                final double areaSize = timeBarModel.getVisibleArea();
                final double newAreaSize = zoomFactor * areaSize;
                final double marginMove = 0.5 * (newAreaSize - areaSize);
                timeBarModel.setVisibleArea(timeBarModel.getVisibleStartPos() - marginMove,
                                            timeBarModel.getVisibleEndPos() + marginMove);
                repaint();
            }
        });

        addMouseListener(new MouseInputAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == DRAG_BUTTON && draggedHandle == null) {
                    // Determine location of the press on the timebar
                    final double clickedTime = getTimeAtMouseLocation(e);

                    // Find overlapping handle
                    for (Handle handle : handles) {
                        if (handle.overlaps(clickedTime, getWidth())) {
                            // Start dragging this handle
                            draggedHandle = handle;
                            dragOffset = clickedTime - handle.getStart();
                            return;
                        }
                    }

                    // No handle found, grab the default handle and center it on the mouse
                    draggedHandle = defaultDragHandle;
                    if (draggedHandle != null) {
                        dragOffset = draggedHandle.getLength() * 0.5;
                        draggedHandle.moveTo(clickedTime - dragOffset);
                        repaint();
                    }
                }
            }

            @Override public void mouseReleased(MouseEvent e) {
                if (e.getButton() == DRAG_BUTTON && draggedHandle != null) {
                    draggedHandle.moveTo(getTimeAtMouseLocation(e) - dragOffset);
                    draggedHandle = null;
                    repaint();
                }
            }

        });

        addMouseMotionListener(new MouseInputAdapter() {
            @Override public void mouseDragged(MouseEvent e) {
                if (draggedHandle != null) {
                    draggedHandle.moveTo(getTimeAtMouseLocation(e) - dragOffset);
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

            @Override public void onDurationChanged(TimeBarModel timeBar) {
                repaint();
            }
        });
    }

    private double getTimeAtMouseLocation(MouseEvent e) {
        return MathUtils.relPos(e.getX(), 0, getWidth());
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
        int currentTimeX = (int) (timeBarModel.getCurrentTime() * getWidth());
        g2.setColor(CURRENT_TIME_COLOR_BORDER2);
        g2.fillRect(currentTimeX - CURRENT_TIME_RADIUS*2, 0, 1+4*CURRENT_TIME_RADIUS, getHeight());
        g2.setColor(CURRENT_TIME_COLOR_BORDER1);
        g2.fillRect(currentTimeX - CURRENT_TIME_RADIUS, 0, 1+2*CURRENT_TIME_RADIUS, getHeight());
        g2.setColor(CURRENT_TIME_COLOR);
        g2.drawLine(currentTimeX, 0, currentTimeX, getHeight());
    }

    private interface Handle {
        double getStart();
        double getEnd();
        double getLength();

        boolean overlaps(double location, int componentWidth);

        int getMarginInPixels();
        Color getColor();

        int mapStartLocation(int targetStart, int targetEnd);
        int mapEndLocation(int targetStart, int targetEnd);
        int mapDuration(int targetStart, int targetEnd);

        void moveTo(double newStart);
    }

    private abstract class HandleBase implements Handle {

        private static final int PIXEL_MARGIN = 4;
        private Color color;
        private int pixelMargin;

        public HandleBase() {
            this(DEFAULT_HANDLE_COLOR);
        }

        public HandleBase(Color color) {
            this(color, PIXEL_MARGIN);
        }

        public HandleBase(Color color, int pixelMargin) {
            this.color = color;
            this.pixelMargin = pixelMargin;
        }

        @Override public double getEnd() {
            return getStart();
        }

        @Override public final double getLength() {
            return getEnd() - getStart();
        }

        @Override public final boolean overlaps(double location, int componentWidth) {
            if (componentWidth <= 0) return false;

            double relativeMarginSize = (double) getMarginInPixels() / componentWidth;
            return (location >= getStart() - relativeMarginSize) && (location <= getEnd() + relativeMarginSize);
        }

        @Override public final int getMarginInPixels() {
            return pixelMargin;
        }

        @Override public final Color getColor() {
           return color;
        }

        @Override public final int mapStartLocation(int targetStart, int targetEnd) {
            return -pixelMargin + (int) MathUtils.mix(getStart(), targetStart, targetEnd);
        }

        @Override public final int mapEndLocation(int targetStart, int targetEnd) {
            return +pixelMargin + (int) MathUtils.mix(getEnd(), targetStart, targetEnd);
        }

        @Override public int mapDuration(int targetStart, int targetEnd) {
            return pixelMargin * 2 + (int) MathUtils.mix(getEnd() - getStart(), targetStart, targetEnd);
        }
    }

    private class VisibleAreaHandle extends HandleBase {
        private final TimeBarModel timeBarModel;

        public VisibleAreaHandle(TimeBarModel timeBarModel) {
            super(VISIBLE_HANDLE_COLOR);
            this.timeBarModel = timeBarModel;
        }

        @Override public double getStart() {
            return timeBarModel.getVisibleStartPos();
        }

        @Override public double getEnd() {
            return timeBarModel.getVisibleEndPos();
        }

        @Override public void moveTo(double newStart) {
            final double visibleArea = timeBarModel.getVisibleArea();

            // Don't allow dragging so that the size changes when dragging the whole visible area
            newStart = MathUtils.clamp(newStart, 0, 1.0 - visibleArea);

            timeBarModel.setVisibleArea(newStart, newStart + visibleArea);
        }
    }

    private class VisibleAreaStartHandle extends HandleBase {
        private final TimeBarModel timeBarModel;

        public VisibleAreaStartHandle(TimeBarModel timeBarModel) {
            super(START_HANDLE_COLOR);
            this.timeBarModel = timeBarModel;
        }

        @Override public double getStart() {
            return timeBarModel.getVisibleStartPos();
        }

        @Override public void moveTo(double newStart) {
            timeBarModel.setVisibleStartPos(newStart);
        }
    }

    private class VisibleAreaEndHandle extends HandleBase {
        private final TimeBarModel timeBarModel;

        public VisibleAreaEndHandle(TimeBarModel timeBarModel) {
            super(END_HANDLE_COLOR);
            this.timeBarModel = timeBarModel;
        }

        @Override public double getStart() {
            return timeBarModel.getVisibleEndPos();
        }

        @Override public void moveTo(double newStart) {
            timeBarModel.setVisibleEndPos(newStart);
        }
    }

}
