package org.demoflow;

import org.demoflow.effect.Effect;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.calculator.Calculator;
import org.demoflow.utils.ClassUtils;

import java.util.*;

import static org.flowutils.Check.notNull;

/**
 * Keeps track of available effect, calculator and interpolator types.
 */
// IDEA: Support reloading the available effects, calculators and interpolators?  Will it work if an editor is running and the classes are recompiled?
public final class DemoComponentManager {

    public static final String PROJECT_ROOT_PATH = "org.demoflow";
    public static final List<String> DEFAULT_EFFECTS_PATHS = Arrays.asList("org.demoflow.effect.effects");
    public static final List<String> DEFAULT_CALCULATORS_PATHS = Arrays.asList("org.demoflow.calculator.calculators",
                                                                               "org.demoflow.calculator.function.functions");
    public static final List<String> DEFAULT_INTERPOLATORS_PATHS = Arrays.asList("org.demoflow.interpolator.interpolators");

    private final List<String> effectsPaths;
    private final List<String> calculatorsPaths;
    private final List<String> interpolatorsPaths;

    private final List<Class<? extends Effect>> effectTypes;
    private final List<Class<? extends Calculator>> calculatorTypes;
    private final List<Class<? extends Interpolator>> interpolatorTypes;

    private final Map<Class, List<Class<? extends Calculator>>> calculatorTypesByReturnType = new LinkedHashMap<>();

    /**
     * Loads default effects, calculators, and interpolators.
     */
    public DemoComponentManager() {
        this(PROJECT_ROOT_PATH, DEFAULT_EFFECTS_PATHS, DEFAULT_CALCULATORS_PATHS, DEFAULT_INTERPOLATORS_PATHS);
    }

    /**
     * Loads the specified effects, calculators, and interpolators.
     *
     * @param rootPath project root package.
     * @param effectsPath full name of package to load effects from.
     * @param calculatorsPath full name of package to load calculators from.
     * @param interpolatorsPath full name of package to load interpolators from.
     */
    public DemoComponentManager(String rootPath, String effectsPath, String calculatorsPath, String interpolatorsPath) {
        this(rootPath,
             Collections.singletonList(effectsPath),
             Collections.singletonList(calculatorsPath),
             Collections.singletonList(interpolatorsPath));
    }

    /**
     * Loads the specified effects, calculators, and interpolators.
     *
     * @param rootPath project root package.
     * @param effectsPaths full names of packages to load effects from.
     * @param calculatorsPaths full names of packages to load calculators from.
     * @param interpolatorsPaths full names of packages to load interpolators from.
     */
    public DemoComponentManager(String rootPath, List<String> effectsPaths, List<String> calculatorsPaths, List<String> interpolatorsPaths) {
        notNull(rootPath, "rootPath");
        notNull(effectsPaths, "effectsPaths");
        notNull(calculatorsPaths, "calculatorsPaths");
        notNull(interpolatorsPaths, "interpolatorsPaths");

        this.effectsPaths = effectsPaths;
        this.calculatorsPaths = calculatorsPaths;
        this.interpolatorsPaths = interpolatorsPaths;

        effectTypes = ClassUtils.getClassesImplementing(rootPath, Effect.class, this.effectsPaths);
        calculatorTypes = ClassUtils.getClassesImplementing(rootPath, Calculator.class, this.calculatorsPaths);
        interpolatorTypes = ClassUtils.getClassesImplementing(rootPath, Interpolator.class, this.interpolatorsPaths);

        // Organize calculator types by return type
        for (Class<? extends Calculator> calculatorType : calculatorTypes) {
            // Determine return type by creating an instance of the calculator and querying the return type
            final Class returnType = createCalculator(calculatorType).getReturnType();

            // Skip calculators with undefined return types (basically interpolating calculator)
            if (returnType != null) {
                // Get list of calculators with this return type
                List<Class<? extends Calculator>> calculatorsWithThisReturnType = calculatorTypesByReturnType.get(returnType);
                if (calculatorsWithThisReturnType == null) {
                    calculatorsWithThisReturnType = new ArrayList<>();
                    calculatorTypesByReturnType.put(returnType, calculatorsWithThisReturnType);
                    calculatorsWithThisReturnType.add(null);
                }

                // Add this calculator type to that list
                calculatorsWithThisReturnType.add(calculatorType);
            }
        }
    }

    /**
     * @return all available effects.
     */
    public List<Class<? extends Effect>> getEffectTypes() {
        return effectTypes;
    }

    /**
     * @return all available calculators.
     */
    public List<Class<? extends Calculator>> getCalculatorTypes() {
        return calculatorTypes;
    }

    /**
     * @return the calculators that calculate a value of the specified type, or an empty list if there are no such calculators.
     */
    public List<Class<? extends Calculator>> getCalculatorTypes(Class desiredResultType) {
        List<Class<? extends Calculator>> calculatorTypes = calculatorTypesByReturnType.get(desiredResultType);
        if (calculatorTypes == null) calculatorTypes = Collections.emptyList();

        return calculatorTypes;
    }

    /**
     * @return an instance of the specified calculator type.
     */
    public Calculator createCalculator(Class<? extends Calculator> type) {
        if (type == null) return null;
        if (!calculatorTypes.contains(type)) throw new IllegalStateException("Unknown calculator type " + type);

        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create a calculator of type " + type +".  Ensure it has a public no-parameters constructor.  The error was: " + e.getMessage(), e);
        }
    }

    /**
     * @return all available interpolators.
     */
    public List<Class<? extends Interpolator>> getInterpolatorTypes() {
        return interpolatorTypes;
    }

}
