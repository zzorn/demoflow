package org.demoflow.calculator;

/**
 * Enumeration indicating a type of time and containing a getter to get the selected type of time.
 */
public enum TimeType {
    TIME_FROM_DEMO_START,
    TIME_FROM_DEMO_END,
    RELATIVE_DEMO_POSITION,
    TIME_FROM_EFFECT_START,
    TIME_FROM_EFFECT_END,
    RELATIVE_EFFECT_POSITION,
    ;

    public double getTime(CalculationContext calculationContext) {
        switch (this) {
            case TIME_FROM_DEMO_START: return calculationContext.getSecondsFromDemoStart();
            case TIME_FROM_DEMO_END: return calculationContext.getSecondsFromDemoEnd();
            case RELATIVE_DEMO_POSITION: return calculationContext.getRelativeDemoTime();
            case TIME_FROM_EFFECT_START: return calculationContext.getSecondsFromEffectStart();
            case TIME_FROM_EFFECT_END: return calculationContext.getSecondsFromEffectEnd();
            case RELATIVE_EFFECT_POSITION: return calculationContext.getRelativeEffectPosition();
            default: throw new IllegalStateException("Unknown time type " + this);
        }
    }
}
