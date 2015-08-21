package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.Fun;
import org.demoflow.calculator.function.FunBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.MathUtils;

/**
 * Maps the inputs to -1..1 and outputs from -1..1 for a function.
 */
public final class MapInputsOutputsFun extends FunBase {

    public final Parameter<Fun> function;
    public final Parameter<Double> sourceStart;
    public final Parameter<Double> sourceEnd;
    public final Parameter<Double> targetStart;
    public final Parameter<Double> targetEnd;
    public final Parameter<Boolean> clampInputRange;
    public final Parameter<Boolean> clampOutputRange;

    public MapInputsOutputsFun() {
        this(null);
    }

    public MapInputsOutputsFun(Fun baseFunction) {
        this(baseFunction, -1, 1, -1, 1);
    }

    public MapInputsOutputsFun(Fun baseFunction,
                               double sourceStart, double sourceEnd,
                               double targetStart, double targetEnd) {
        this(baseFunction, sourceStart, sourceEnd, targetStart, targetEnd, false, false);
    }
    public MapInputsOutputsFun(Fun baseFunction,
                               double sourceStart, double sourceEnd,
                               double targetStart, double targetEnd,
                               boolean clampInputRange,
                               boolean clampOutputRange) {
        this.function = addParameter("baseFunction", baseFunction);
        this.sourceStart = addParameter("sourceStart", sourceStart);
        this.sourceEnd = addParameter("sourceEnd", sourceEnd);
        this.targetStart = addParameter("targetStart", targetStart);
        this.targetEnd = addParameter("targetEnd", targetEnd);
        this.clampInputRange = addParameter("clampInputRange", clampInputRange);
        this.clampOutputRange = addParameter("clampOutputRange", clampOutputRange);
    }

    @Override public double get(double x) {
        // Map from source range to -1..1
        double value = MathUtils.relPos(x, sourceStart.get(), sourceEnd.get());
        if (clampInputRange.get()) value = MathUtils.clampMinus1To1(value);

        // Calculate function
        if (function.get() != null) value = function.get().get(value);

        // Map from function to output range
        value = MathUtils.map(value, -1, 1, targetStart.get(), targetEnd.get());
        if (clampOutputRange.get()) value = MathUtils.clampMinus1To1(value);

        return value;
    }
}
