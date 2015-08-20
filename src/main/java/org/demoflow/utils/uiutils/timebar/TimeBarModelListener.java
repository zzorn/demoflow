package org.demoflow.utils.uiutils.timebar;

/**
 * Listens to changes in the visible time area and the current time, and other changes.
 */
public interface TimeBarModelListener {

    void onCurrentTimeChanged(TimeBarModel timeBar);

    void onVisibleAreaChanged(TimeBarModel timeBar);

    void onStartEndChanged(TimeBarModel timeBar);

}
