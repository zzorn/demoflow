package org.demoflow;

import org.flowutils.Check;
import org.flowutils.service.ServiceBase;
import org.flowutils.service.ServiceProvider;

/**
 *
 */
public class Editor extends ServiceBase {

    private final View view;

    public Editor(View view) {
        Check.notNull(view, "viewer");
        this.view = view;
    }

    @Override protected void doInit(ServiceProvider serviceProvider) {

    }

    @Override protected void doShutdown() {

    }
}
