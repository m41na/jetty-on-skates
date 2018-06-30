package com.jarredweb.jesty.view;

public class ViewEngine {

    private static ViewEngine instance;
    private final ViewConfiguration config;
    private final ViewProcessor view;

    private ViewEngine() {
        config = new TwigViewConfiguration();
        view = new TwigViewProcessor(config);
    }

    public static ViewEngine instance() {
        if (instance == null) {
            synchronized (ViewEngine.class) {
                instance = new ViewEngine();
            }
        }
        return instance;
    }

    public static ViewConfiguration getConfiguration() {
        return instance().config;
    }

    public static ViewProcessor getProcessor() {
        return instance().view;
    }
}
