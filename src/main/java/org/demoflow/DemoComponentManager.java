package org.demoflow;

import nu.xom.Element;
import org.demoflow.effect.Effect;
import org.demoflow.calculator.Calculator;
import org.demoflow.utils.ClassUtils;

import java.io.IOException;
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

    private final List<String> effectsPaths;
    private final List<String> calculatorsPaths;

    private final List<Class<? extends Effect>> effectTypes;
    private final List<Class<? extends Calculator>> calculatorTypes;

    private final Map<Class, List<Class<? extends Calculator>>> calculatorTypesByReturnType = new LinkedHashMap<>();

    /**
     * Loads default effects, calculators, and interpolators.
     */
    public DemoComponentManager() {
        this(PROJECT_ROOT_PATH, DEFAULT_EFFECTS_PATHS, DEFAULT_CALCULATORS_PATHS);
    }

    /**
     * Loads the specified effects, calculators, and interpolators.
     *
     * @param rootPath project root package.
     * @param effectsPath full name of package to load effects from.
     * @param calculatorsPath full name of package to load calculators from.
     */
    public DemoComponentManager(String rootPath, String effectsPath, String calculatorsPath) {
        this(rootPath,
             Collections.singletonList(effectsPath),
             Collections.singletonList(calculatorsPath));
    }

    /**
     * Loads the specified effects, calculators, and interpolators.
     *
     * @param rootPath project root package.
     * @param effectsPaths full names of packages to load effects from.
     * @param calculatorsPaths full names of packages to load calculators from.
     */
    public DemoComponentManager(String rootPath, List<String> effectsPaths, List<String> calculatorsPaths) {
        notNull(rootPath, "rootPath");
        notNull(effectsPaths, "effectsPaths");
        notNull(calculatorsPaths, "calculatorsPaths");

        this.effectsPaths = effectsPaths;
        this.calculatorsPaths = calculatorsPaths;

        effectTypes = ClassUtils.getClassesImplementing(rootPath, Effect.class, this.effectsPaths);
        calculatorTypes = ClassUtils.getClassesImplementing(rootPath, Calculator.class, this.calculatorsPaths);

        // Organize calculator types by return type
        for (Class<? extends Calculator> calculatorType : calculatorTypes) {
            // Determine return type by creating an instance of the calculator and querying the return type
            final Class returnType = createCalculator(calculatorType).getReturnType();

            // Skip calculators with undefined return types
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
        final ArrayList<Class<? extends Calculator>> types = new ArrayList<>();

        for (Map.Entry<Class, List<Class<? extends Calculator>>> entry : calculatorTypesByReturnType.entrySet()) {
            if (desiredResultType.isAssignableFrom(entry.getKey())) {
                types.addAll(entry.getValue());
            }
        }

        return types;
    }

    /**
     * @return an instance of the specified effect type, using the simple class name of the effect.
     */
    public Effect createEffect(String simpleTypeName) {
        for (Class<? extends Effect> effectType : effectTypes) {
            if (effectType.getSimpleName().equals(simpleTypeName)) {
                return createEffect(effectType);
            }
        }

        throw new IllegalStateException("Unknown effect type '" + simpleTypeName + "'");
    }

    /**
     * @return an instance of the specified calculator type, using the simple class name of the calculator.
     */
    public Calculator createCalculator(String simpleTypeName) {
        for (Class<? extends Calculator> calculatorType : calculatorTypes) {
            if (calculatorType.getSimpleName().equals(simpleTypeName)) {
                return createCalculator(calculatorType);
            }
        }

        throw new IllegalStateException("Unknown calculator type '" + simpleTypeName + "'");
    }


    /**
     * @return an instance of the specified calculator type.
     */
    public <T extends Calculator> T createCalculator(Class<T> type) {
        if (type == null) return null;
        if (!calculatorTypes.contains(type)) throw new IllegalStateException("Unknown calculator type " + type);

        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create a calculator of type " + type +".  Ensure it has a public no-parameters constructor.  The error was: " + e.getMessage(), e);
        }
    }

    /**
     * @return an instance of the specified effect type.
     */
    public <T extends Effect> T createEffect(Class<T> type) {
        if (type == null) return null;
        if (!effectTypes.contains(type)) throw new IllegalStateException("Unknown effect type " + type);

        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not create an effect of type " + type +".  Ensure it has a public no-parameters constructor.  The error was: " + e.getMessage(), e);
        }
    }

    public Calculator loadCalculator(Element element) throws IOException {
        // Determine calculator type
        final String type = element.getAttributeValue("type");
        if (type == null) return null;

        // Create calculator
        final Calculator calculator = createCalculator(type);

        // Load calculator content
        calculator.fromXmlElement(element, this);

        return calculator;
    }

    public Effect loadEffect(Element element) throws IOException {
        // Determine effect type
        final String type = element.getAttributeValue("type");

        // Create effect
        final Effect effect = createEffect(type);

        // Load effect content
        effect.fromXmlElement(element, this);

        return effect;
    }
}
