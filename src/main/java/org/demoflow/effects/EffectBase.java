package org.demoflow.effects;

import org.flowutils.Symbol;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 */
public abstract class EffectBase implements Effect {

    private final Map<Symbol, ParameterRange> parameterRanges = new LinkedHashMap<>();
    private final ConcurrentMap<Symbol, Object> parameters = new ConcurrentHashMap<>();

    protected final <T> void addParameter(Symbol id, T initialValue, ParameterRange<T> range) {
        parameterRanges.put(id, range);
        parameters.put(id, initialValue);
    }


    @Override public final Map<Symbol, ParameterRange> getParameterRanges() {
        return parameterRanges;
    }

    @Override public final Map<Symbol, Object> getParameterValues() {
        return parameters;
    }

    @Override public final void updateParameters(Map<Symbol, Object> parametersToUpdate) {
        for (Map.Entry<Symbol, Object> entry : parametersToUpdate.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    @Override public final void setParameter(Symbol id, Object value) {
        getParameterRange(id);
        parameters.put(id, value);
    }

    @Override public final <T> T getParameter(Symbol id) {
        getParameterRange(id);
        return (T) parameters.get(id);
    }

    /**
     * @return the range of the specified parameter.
     */
    @Override public final ParameterRange getParameterRange(Symbol id) {
        ParameterRange parameterRange = parameterRanges.get(id);
        if (parameterRange == null) {
            throw new IllegalArgumentException("No parameter with the id '"+id+"'");
        }
        return parameterRange;
    }
}
