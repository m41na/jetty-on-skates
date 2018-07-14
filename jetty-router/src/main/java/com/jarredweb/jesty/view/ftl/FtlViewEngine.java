package com.jarredweb.jesty.view.ftl;

import com.jarredweb.jesty.view.ViewEngine;
import freemarker.template.Template;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class FtlViewEngine implements ViewEngine{

    private static FtlViewEngine instance;
    private final ViewConfiguration config;
    private final ViewProcessor view;

    private FtlViewEngine() throws IOException {
        config = new FtlViewConfiguration();
        view = new FtlViewProcessor();
    }

    public static FtlViewEngine instance() throws IOException {
        if (instance == null) {
            synchronized (FtlViewEngine.class) {
                instance = new FtlViewEngine();
            }
        }
        return instance;
    }

    public static ViewConfiguration getConfiguration() throws IOException {
        return instance().config;
    }

    public static ViewProcessor getProcessor() throws IOException {
        return instance().view;
    }

    @Override
    public String merge(String template, Map<String, Object> model) throws Exception {
        StringWriter output = new StringWriter();
        String templateName = !template.contains(".")? template.concat(".ftl") : template;
        Template resolved = view.resolve(templateName, config.getEnvironment());
        resolved.process(model, output);
        return output.toString();
    }
}
