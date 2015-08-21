package org.demoflow;

import org.demoflow.utils.SpeechService;
import org.flowutils.service.ServiceProviderBase;

/**
 *
 */
public class DemoServices extends ServiceProviderBase {

    public final SpeechService speechService;

    public DemoServices() {
        speechService = addService(new SpeechService());
    }

}
