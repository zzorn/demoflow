package org.demoflow.demo;

/**
 * Listens to demo state changes.
 */
public interface DemoListener {

    void onProgress(Demo demo, double currentDemoTime, double totalDemoTime, double relativeDemoTime);

    void onPauseChanged(Demo demo, boolean paused);

    void onSetup(Demo demo);

    void onShutdown(Demo demo);

    /**
     * Called when the demo finishes.
     */
    void onCompleted();
}
