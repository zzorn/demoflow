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

import static org.flowutils.Check.notNull;

/**
 *
 */
public class TimeBar extends JPanel {

    protected static final int DRAG_BUTTON = MouseEvent.BUTTON1;
    protected static final double MOUSE_WHEEL_SCALING_FACTOR = 1.3333;
    private static final Color BACKGROUND_COLOR = new Color(100, 100, 100);
    private static final Color CURRENT_TIME_COLOR = new Color(255, 243, 227, 255);
    private static final Color CURRENT_TIME_COLOR_BORDER1 = new Color(255, 219, 117, 94);
    private static final Color CURRENT_TIME_COLOR_BORDER2 = new Color(242, 195, 91, 43);
    private static final int CURRENT_TIME_RADIUS = 1;
    private static final Color DEFAULT_HANDLE_COLOR = new Color(172, 100, 84);
    protected final TimeBarModel timeBarModel;
    protected Handle defaultHandle;
    protected Handle draggedHandle;
    protected double dragOffset;
    private ArrayList<Handle> handles = new ArrayList<>();

    private static final int HANDLE_PIXEL_MARGIN = 4;

    public TimeBar(final TimeBarModel timeBarModel) {
        super(new MigLayout());
        notNull(timeBarModel, "timeBarModel");

        this.timeBarModel = timeBarModel;

        setupListeners(timeBarModel);
    }

    private void setupListeners(TimeBarModel timeBarModel) {
        addMouseWheelListener(new MouseWheelListener() {
            @Override public void mouseWheelMoved(MouseWheelEvent e) {
                final double clickedTime = getTimeAtMouseLocation(e);
                Handle handle = getHandleAtPosition(clickedTime);
                if (handle == null) handle = defaultHandle;
                if (handle != null && handle.isZoomable()) {
                    // Zoom the handle
                    final double scale = e.getPreciseWheelRotation();
                    double zoomFactor = Math.pow(MOUSE_WHEEL_SCALING_FACTOR, scale);

                    final double areaSize = handle.getLength();
                    final double newAreaSize = zoomFactor * areaSize;
                    final double marginMove = (newAreaSize - areaSize);
                    final double relativeZoomLocation = MathUtils.mapAndClamp(getTimeAtMouseLocation(e),
                                                                              getViewStartPos(),
                                                                              getViewEndPos(),
                                                                              handle.getStart(),
                                                                              handle.getEnd());

                    handle.setStartAndEnd(handle.getStart() - marginMove * (relativeZoomLocation),
                                          handle.getEnd() + marginMove * (1.0 - relativeZoomLocation));
                    repaint();
                }
            }
        });

        addMouseListener(new MouseInputAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == DRAG_BUTTON && draggedHandle == null) {
                    final double clickedTime = getTimeAtMouseLocation(e);
                    Handle handle = getHandleAtPosition(clickedTime);
                    if (handle != null) {
                        // Start dragging this handle
                        draggedHandle = handle;
                        dragOffset = clickedTime - handle.getStart();
                    } else {
                        // No handle found, grab the default handle and center it on the mouse
                        draggedHandle = defaultHandle;
                        if (draggedHandle != null) {
                            dragOffset = draggedHandle.getLength() * 0.5;
                            draggedHandle.moveTo(clickedTime - dragOffset);
                            repaint();
                        }
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

    protected double getViewStartPos() {
        return timeBarModel.getVisibleStartPos();
    }

    protected double getViewEndPos() {
        return timeBarModel.getVisibleEndPos();
    }

    protected final Handle getHandleAtPosition(final double relativePosition) {
        // Find overlapping handle
        final double marginDuration = durationOfPixelMargin();
        for (Handle handle : handles) {
            if (handle.getStart() - marginDuration <= relativePosition && relativePosition < handle.getEnd() + marginDuration) {

                return handle;
            }
        }

        return null;
    }

    protected final void addHandle(final Handle handle) {
        handles.add(handle);
    }

    protected final void addDefaultHandle(final Handle handle) {
        handles.add(handle);
        defaultHandle = handle;
    }

    protected final double getTimeAtMouseLocation(MouseEvent e) {
        return screenToTime(e.getX());
    }

    protected final double screenToTime(int x) {
        return MathUtils.map(x, 0, getWidth(), getViewStartPos(), getViewEndPos());
    }

    protected final int timeToScreen(final double time) {
        return (int) MathUtils.map(time, getViewStartPos(), getViewEndPos(), 0, getWidth());
    }

    protected final double durationOfPixelMargin() {
        return MathUtils.map(HANDLE_PIXEL_MARGIN, 0, getWidth(), 0, getViewEndPos() - getViewStartPos());
    }

    protected final int durationToScreen(final double duration) {
        return (int) MathUtils.map(duration, 0, getViewEndPos() - getViewStartPos(), 0, getWidth());
    }

    public final TimeBarModel getTimeBarModel() {
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

            int x = -HANDLE_PIXEL_MARGIN + timeToScreen(handle.getStart());
            int w = 2 * HANDLE_PIXEL_MARGIN + durationToScreen(handle.getLength());

            g2.setColor(handle.getColor());
            g2.fillRect(x, 0, w, getHeight());
            g2.draw3DRect(x, 0, w, getHeight(), true);
        }

        // Draw current time
        int currentTimeX = timeToScreen(timeBarModel.getCurrentTime());
        g2.setColor(CURRENT_TIME_COLOR_BORDER2);
        g2.fillRect(currentTimeX - CURRENT_TIME_RADIUS*2, 0, 1+4*CURRENT_TIME_RADIUS, getHeight());
        g2.setColor(CURRENT_TIME_COLOR_BORDER1);
        g2.fillRect(currentTimeX - CURRENT_TIME_RADIUS, 0, 1+2*CURRENT_TIME_RADIUS, getHeight());
        g2.setColor(CURRENT_TIME_COLOR);
        g2.drawLine(currentTimeX, 0, currentTimeX, getHeight());
    }

    protected interface Handle {
        double getStart();
        double getEnd();
        double getLength();

        Color getColor();
        boolean isZoomable();

        void moveTo(double newStart);
        void setStartAndEnd(double start, double end);
    }

    protected static abstract class HandleBase implements Handle {

        private Color color;

        public HandleBase() {
            this(DEFAULT_HANDLE_COLOR);
        }

        public HandleBase(Color color) {
            this.color = color;
        }

        @Override public double getEnd() {
            return getStart();
        }

        @Override public final double getLength() {
            return getEnd() - getStart();
        }

        @Override public final Color getColor() {
           return color;
        }

        @Override public boolean isZoomable() {
            return getEnd() > getStart() + 0.0000001;
        }

        @Override public void setStartAndEnd(double start, double end) {
        }
    }
}
