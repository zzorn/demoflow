package org.demoflow.utils.uiutils.timebar;

import org.demoflow.effect.Effect;
import org.flowutils.MathUtils;

import java.awt.*;

/**
 *
 */
public class EffectTimeBar extends TimeBar {

    private static final Color START_HANDLE_COLOR = new Color(210, 115, 77);
    private static final Color END_HANDLE_COLOR = new Color(208, 78, 113);
    private static final Color VISIBLE_HANDLE_COLOR = new Color(143, 71, 52);

    public EffectTimeBar(final TimeBarModel timeBarModel, final Effect effect) {
        super(timeBarModel);

        addHandle(new HandleBase(START_HANDLE_COLOR) {
            @Override public double getStart() {
                return effect.getRelativeStartTime();
            }

            @Override public void moveTo(double newStart) {
                effect.setRelativeStartTime(newStart);
            }
        });

        addHandle(new HandleBase(END_HANDLE_COLOR) {
            @Override public double getStart() {
                return effect.getRelativeEndTime();
            }

            @Override public void moveTo(double newStart) {
                effect.setRelativeEndTime(newStart);
            }
        });

        addDefaultHandle(new HandleBase(VISIBLE_HANDLE_COLOR) {
            @Override public double getStart() {
                return effect.getRelativeStartTime();
            }

            @Override public double getEnd() {
                return effect.getRelativeEndTime();
            }

            @Override public void moveTo(double newStart) {
                final double visibleArea = effect.getRelativeEndTime() - effect.getRelativeStartTime();

                // Don't allow dragging so that the size changes when dragging the whole visible area
                newStart = MathUtils.clamp(newStart, 0, 1.0 - visibleArea);

                effect.setEffectTimePeriod(newStart, newStart + visibleArea);
            }

            @Override public void setStartAndEnd(double start, double end) {
                effect.setEffectTimePeriod(start, end);
            }
        });
    }


}
