package org.demoflow;

import org.demoflow.effect.Effect;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.parameter.calculator.Calculator;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.flowutils.Check.notNull;

/**
 * Keeps track of available effect, calculator and interpolator types.
 */
public final class DemoComponentManager {

    private static final String PROJECT_ROOT_PATH = "org.demoflow";
    private static final String DEFAULT_EFFECTS_PATH = "org.demoflow.effect.effects";
    private static final String DEFAULT_CALCULATORS_PATH = "org.demoflow.parameter.calculator.calculators";
    private static final String DEFAULT_INTERPOLATORS_PATH = "org.demoflow.interpolator.interpolators";

    private final String effectsPath;
    private final String calculatorsPath;
    private final String interpolatorsPath;

    private final List<Class<? extends Effect>> effectTypes;
    private final List<Class<? extends Calculator>> calculatorTypes;
    private final List<Class<? extends Interpolator>> interpolatorTypes;

    private final Map<Class, List<Class<? extends Calculator>>> calculatorTypesByReturnType = new LinkedHashMap<>();

    // IDEA: Add constructor that takes list of paths, making it easier to use the demoflow framework with custom components-

    /**
     * Loads default effects, calculators, and interpolators.
     */
    public DemoComponentManager() {
        this(PROJECT_ROOT_PATH, DEFAULT_EFFECTS_PATH, DEFAULT_CALCULATORS_PATH, DEFAULT_INTERPOLATORS_PATH);
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
        notNull(rootPath, "rootPath");
        notNull(effectsPath, "effectsPath");
        notNull(calculatorsPath, "calculatorsPath");
        notNull(interpolatorsPath, "interpolatorsPath");

        this.effectsPath = effectsPath;
        this.calculatorsPath = calculatorsPath;
        this.interpolatorsPath = interpolatorsPath;

        effectTypes = getClassesImplementing(rootPath, Effect.class, effectsPath);
        calculatorTypes = getClassesImplementing(rootPath, Calculator.class, calculatorsPath);
        interpolatorTypes = getClassesImplementing(rootPath, Interpolator.class, interpolatorsPath);

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
            throw new IllegalStateException("Could not create a calculator of type " + type +": " + e.getMessage(), e);
        }
    }

    /**
     * @return all available interpolators.
     */
    public List<Class<? extends Interpolator>> getInterpolatorTypes() {
        return interpolatorTypes;
    }

    /**
     * @return all classes that implement or extend the specified type, and are located in the specified package.
     */
    private <T> List<Class<? extends T>> getClassesImplementing(String rootPath, final Class<T> type, String packageToGetClassesFrom) {
        final List<Class<? extends T>> classes = new ArrayList<>();

        Reflections reflections = new Reflections(rootPath);
        final Set<Class<? extends T>> allSubTypes = reflections.getSubTypesOf(type);
        for (Class<? extends T> component : allSubTypes) {
            // Get the subtypes that are in the specified package
            if (component.getPackage().getName().equals(packageToGetClassesFrom)) {
                classes.add(component);
            }
        }

        return classes;
    }

}
