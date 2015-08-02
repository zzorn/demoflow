package org.demoflow;

import org.flowutils.Check;
import org.flowutils.service.ServiceBase;
import org.flowutils.service.ServiceProvider;

/**
 *
 */
public class Editor extends ServiceBase {

    private final Viewer viewer;

    public Editor(Viewer viewer) {
        Check.notNull(viewer, "viewer");
        this.viewer = viewer;
    }

    @Override protected void doInit(ServiceProvider serviceProvider) {

    }

    @Override protected void doShutdown() {

    }
}
