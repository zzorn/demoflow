package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.MathUtils;

/**
 *
 */
public class UpDownCalculator extends CalculatorBase<Double> {

    public final Parameter<Double> inputValue;

    public final Parameter<Double> sourceStart;
    public final Parameter<Double> sourcePeak;
    public final Parameter<Double> sourceEnd;

    public final Parameter<Double> outStart;
    public final Parameter<Double> outPeak;
    public final Parameter<Double> outEnd;

    public UpDownCalculator() {
        this.inputValue = addParameter("inputValue", 0.0);
        this.sourceStart = addParameter("sourceStart", 0.0);
        this.sourcePeak = addParameter("sourcePeak", 0.5);
        this.sourceEnd = addParameter("sourceEnd", 1.0);
        this.outStart = addParameter("outStart", 0.0);
        this.outPeak = addParameter("outPeak", 1.0);
        this.outEnd = addParameter("outEnd", 0.0);
    }

    @Override
    protected Double doCalculate(CalculationContext calculationContext, Double currentValue, Parameter<Double> parameter) {
        Double input = inputValue.get();
        if (input <= sourcePeak.get()) {
            return MathUtils.map(input, sourceStart.get(), sourcePeak.get(), outStart.get(), outPeak.get());
        }
        else {
            return MathUtils.map(input, sourcePeak.get(), sourceEnd.get(), outPeak.get(), outEnd.get());
        }
    }

    @Override
    protected void doResetState() {
    }

    @Override
    public Class<Double> getReturnType() {
        return Double.class;
    }
}
