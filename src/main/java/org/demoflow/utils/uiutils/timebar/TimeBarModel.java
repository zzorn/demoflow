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

    private double startTime;
    private double endTime;
    private double currentTime;
    private double visibleAreaStartTime;
    private double visibleAreaEndTime;
    private double secondLength = 1;

    public TimeBarModel(double duration) {
        this(0, duration);
    }

    public TimeBarModel(double startTime, double endTime) {
        setStartAndEndTime(startTime, endTime);
        setCurrentTime(startTime);
        setVisibleArea(startTime, endTime);
    }

    public void setEndTime(double endTime) {
        setStartAndEndTime(0, endTime);
    }

    public void setStartAndEndTime(double startTime, double endTime) {
        if (this.startTime != startTime ||
            this.endTime != endTime) {

            this.startTime = startTime;
            this.endTime = endTime;
            if (this.endTime < this.startTime) this.endTime = this.startTime;

            notifyStartEndChanged();
        }
    }

    public void setCurrentTime(double currentTime) {
        if (this.currentTime != currentTime) {
            this.currentTime = currentTime;
            notifyCurrentTimeChanged();
        }
    }

    public void setVisibleArea(double visibleStartTime, double visibleEndTime) {
        if (visibleAreaStartTime != visibleStartTime ||
            visibleAreaEndTime != visibleEndTime) {

            visibleAreaStartTime = MathUtils.clamp(visibleStartTime, startTime, endTime);
            visibleAreaEndTime = MathUtils.clamp(visibleEndTime, visibleAreaStartTime, endTime);
            notifyVisibleAreaChanged();
        }
    }

    public void setVisibleAreaStartTime(double visibleAreaStartTime) {
        setVisibleArea(visibleAreaStartTime, visibleAreaEndTime);
    }

    public void setVisibleAreaEndTime(double visibleAreaEndTime) {
        setVisibleArea(visibleAreaStartTime, visibleAreaEndTime);
    }

    public double getSecondLength() {
        return secondLength;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public double getDuration() {
        return getEndTime() - getStartTime();
    }

    public double getVisibleDuration() {
        return getVisibleAreaEndTime() - getVisibleAreaStartTime();
    }

    public double getVisibleAreaStartTime() {
        return visibleAreaStartTime;
    }

    public double getVisibleAreaEndTime() {
        return visibleAreaEndTime;
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

    protected final void notifyStartEndChanged() {
        for (TimeBarModelListener listener : listeners) {
            listener.onStartEndChanged(this);
        }
    }
}
