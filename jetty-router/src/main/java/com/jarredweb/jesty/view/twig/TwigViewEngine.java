package com.jarredweb.jesty.view.twig;

import com.jarredweb.jesty.view.ViewEngine;
import java.io.File;
import java.util.Map;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.resource.reference.ResourceReference;

public class TwigViewEngine implements ViewEngine{

    private static TwigViewEngine instance;
    private final ViewConfiguration config;
    private final ViewProcessor view;

    private TwigViewEngine() {
        config = new TwigViewConfiguration();
        view = new TwigViewProcessor(config);
    }

    public static TwigViewEngine instance() {
        if (instance == null) {
            synchronized (TwigViewEngine.class) {
                instance = new TwigViewEngine();
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

    @Override
    public String merge(String template, Map<String, Object> model) throws Exception{
        JtwigTemplate resolved = TwigViewEngine.getProcessor().resolve(template, ResourceReference.file(new File(".", view.templateDir())));
        return resolved.render(JtwigModel.newModel(model));
    }
}
