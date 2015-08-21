package org.demoflow.effect.effects;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.effect.EffectBase;
import org.demoflow.effect.RenderContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.utils.SpeechService;

/**
 *
 */
// TODO: Add volume, balance, etc controls.
public final class SpeechEffect extends EffectBase {

    public final Parameter<Double> trigger;
    public final Parameter<Double> threshold;
    public final Parameter<String> speech;

    private boolean triggered;

    public SpeechEffect() {
        this(0, 0.5, "Hello World");
    }

    public SpeechEffect(double trigger, double triggerThreshold, String speech) {
        this.trigger = addParameter("trigger", trigger);
        this.threshold = addParameter("triggerThreshold", triggerThreshold);
        this.speech = addParameter("speech", speech);
    }

    @Override protected void doUpdate(CalculationContext calculationContext) {

    }

    @Override protected void doRender(RenderContext renderContext) {
        // If we are not speaking and the threshold was hit, speak again
        final SpeechService speechService = renderContext.getDemoServices().speechService;
        if (!speechService.isSpeaking() &&
            trigger.get() >= threshold.get() &&
            !triggered) {

            triggered = true;
            final String speech = this.speech.get();
            if (speech != null) {
                speechService.say(speech);
            }
        }
        else if (trigger.get() < threshold.get()) {
            // Reset trigger if we go back under
            triggered = false;
        }
    }

    @Override protected void doReset() {
        triggered = false;

        // TODO: Stop current speech
    }
}
