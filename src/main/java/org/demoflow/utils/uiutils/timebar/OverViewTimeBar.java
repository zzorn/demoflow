package org.demoflow.utils.uiutils.timebar;

import org.flowutils.MathUtils;

import java.awt.*;

/**
 * TimeBar showing whole demo time and allowing change of the visible area.
 */
public class OverViewTimeBar extends TimeBar {

    private static final Color START_HANDLE_COLOR = new Color(210, 171, 75);
    private static final Color END_HANDLE_COLOR = new Color(208, 126, 52);
    private static final Color VISIBLE_HANDLE_COLOR = new Color(143, 124, 54);

    public OverViewTimeBar(double duration) {
        this(new TimeBarModel(duration));
    }

    public OverViewTimeBar(final TimeBarModel timeBarModel) {
        super(timeBarModel);

        addHandle(new HandleBase(START_HANDLE_COLOR) {
            @Override public double getStart() {
                return timeBarModel.getVisibleStartPos();
            }

            @Override public void moveTo(double newStart) {
                timeBarModel.setVisibleStartPos(newStart);
            }
        });

        addHandle(new HandleBase(END_HANDLE_COLOR) {
            @Override public double getStart() {
                return timeBarModel.getVisibleEndPos();
            }

            @Override public void moveTo(double newStart) {
                timeBarModel.setVisibleEndPos(newStart);
            }
        });

        addDefaultHandle(new HandleBase(VISIBLE_HANDLE_COLOR) {
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

            @Override public void setStartAndEnd(double start, double end) {
                timeBarModel.setVisibleArea(start, end);
            }
        });
    }

    @Override protected double getViewStartPos() {
        return 0;
    }

    @Override protected double getViewEndPos() {
        return 1;
    }

}
