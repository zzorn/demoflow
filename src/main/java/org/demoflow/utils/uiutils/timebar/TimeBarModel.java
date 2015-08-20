package org.demoflow.utils.uiutils.timebar;

import org.flowutils.Check;
import org.flowutils.MathUtils;

import java.util.ArrayList;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class TimeBarModel {

    private final List<TimeBarModelListener> listeners = new ArrayList<>(4);

    private static final double MIN_VISIBLE_AREA = 0.01;

    private double duration;
    private double currentTime;
    private double visibleStartPos;
    private double visibleEndPos;

    public TimeBarModel(double duration) {
        setDuration(duration);
        setCurrentTime(0);
        setVisibleArea(0, 1);
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        Check.positive(duration, "duration");

        if (this.duration != duration) {
            this.duration = duration;
            notifyDurationChanged();
        }
    }

    public void setCurrentTime(double currentTime) {
        if (this.currentTime != currentTime) {
            this.currentTime = currentTime;
            notifyCurrentTimeChanged();
        }
    }

    public void setVisibleArea(double visibleStartPos, double visibleEndPos) {
        if (this.visibleStartPos != visibleStartPos ||
            this.visibleEndPos != visibleEndPos) {

            this.visibleStartPos = visibleStartPos;
            this.visibleEndPos = visibleEndPos;

            // Clamp
            this.visibleStartPos = MathUtils.clamp0To1(this.visibleStartPos);
            this.visibleEndPos = MathUtils.clamp(this.visibleEndPos, this.visibleStartPos, 1);

            // Ensure the visible are doesn't get empty
            final double visibleArea = this.visibleEndPos - this.visibleStartPos;
            double toExpand = MIN_VISIBLE_AREA - visibleArea;
            if (toExpand > 0) {
                this.visibleStartPos -= toExpand*0.5;
                this.visibleEndPos += toExpand*0.5;

                // Clamp expansion to start and end
                if (this.visibleEndPos - MIN_VISIBLE_AREA < 0) {
                    this.visibleStartPos = 0;
                    this.visibleEndPos = MIN_VISIBLE_AREA;
                }
                else if (this.visibleStartPos + MIN_VISIBLE_AREA > 1.0) {
                    this.visibleStartPos = 1.0 - MIN_VISIBLE_AREA;
                    this.visibleEndPos = 1.0;
                }
            }

            notifyVisibleAreaChanged();
        }
    }

    public void setVisibleStartPos(double visibleStartPos) {
        setVisibleArea(visibleStartPos, visibleEndPos);
    }

    public void setVisibleEndPos(double visibleEndPos) {
        setVisibleArea(visibleStartPos, visibleEndPos);
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public double getVisibleArea() {
        return getVisibleEndPos() - getVisibleStartPos();
    }

    public double getVisibleTime() {
        return getVisibleArea() * duration;
    }

    public double getVisibleStartPos() {
        return visibleStartPos;
    }

    public double getVisibleEndPos() {
        return visibleEndPos;
    }

    public double getVisibleStartTime() {
        return visibleStartPos * duration;
    }

    public double getVisibleEndTime() {
        return visibleEndPos * duration;
    }

    /**
     * @param listener listener to notify about changes and updates to this time bar model.
     */
    public final void addListener(TimeBarModelListener listener) {
        notNull(listener, "listener");
        if (listeners.contains(listener)) throw new IllegalArgumentException("The TimeBarListener has already been added as a listener, can't add it twice");

        listeners.add(listener);
    }

    /**
     * @param listener listener to remove.
     */
    public final void removeListener(TimeBarModelListener listener) {
        listeners.remove(listener);
    }

    protected final void notifyCurrentTimeChanged() {
        for (TimeBarModelListener listener : listeners) {
            listener.onCurrentTimeChanged(this);
        }
    }

    protected final void notifyVisibleAreaChanged() {
        for (TimeBarModelListener listener : listeners) {
            listener.onVisibleAreaChanged(this);
        }
    }

    protected final void notifyDurationChanged() {
        for (TimeBarModelListener listener : listeners) {
            listener.onDurationChanged(this);
        }
    }
}
