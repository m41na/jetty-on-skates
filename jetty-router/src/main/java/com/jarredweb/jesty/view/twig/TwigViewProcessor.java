package com.jarredweb.jesty.view.twig;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.resource.reference.ResourceReference;

public class TwigViewProcessor implements ViewProcessor{
    
    protected final ViewConfiguration factory;

    public TwigViewProcessor(ViewConfiguration factory) {
        this.factory = factory;
    }
    
    @Override
    public void write(HttpServletResponse response, JtwigTemplate template, String view, String contentType, Map<String, Object> model) throws IOException {
        response.setContentType(contentType);
        JtwigModel viewModel = JtwigModel.newModel(model);
        template.render(viewModel, new PrintStream(response.getOutputStream(), true, "UTF-8"));
    }

    @Override
    public JtwigTemplate resolve(String templatePath, ResourceReference where) throws Exception {
        ResourceReference resource = new ResourceReference(where.getType(), Paths.get("www/" + templatePath + ".html").toFile().getAbsolutePath());
        JtwigTemplate jtwigTemplate = new JtwigTemplate(factory.getEnvironment(), resource);
        return jtwigTemplate;
    }

    @Override
    public String templateDir() {
        return "www";
    }
}
