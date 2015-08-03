package org.demoflow.animation;

/**
 * Listens to demo state changes.
 */
public interface DemoListener {

    void onProgress(Demo demo, double currentDemoTime, double totalDemoTime, double relativeDemoTime);

    void onPauseChanged(Demo demo, boolean paused);

    void onSetup(Demo demo);

    void onShutdown(Demo demo);

}
