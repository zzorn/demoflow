package org.demoflow.parameter.range.ranges;

import org.demoflow.calculator.TimeType;
import org.demoflow.parameter.range.SelectRange;

/**
 * Range for time types.
 */
public final class TimeTypeRange extends SelectRange<TimeType> {

    public static final TimeTypeRange FULL = new TimeTypeRange();

    public TimeTypeRange() {
        super(TimeType.class, TimeType.values());
    }
}
