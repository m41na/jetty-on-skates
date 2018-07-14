package com.jarredweb.jesty.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class ZestyJs {

    private static final NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();
    private static final ScriptEngine ENGINE = FACTORY.getScriptEngine("-scripting", "-doe", "-ot", "--language=es6");

    static {
        ScriptContext context = ENGINE.getContext();
        context.setAttribute("zesty", new AppProvider(), ScriptContext.ENGINE_SCOPE);
        context.setAttribute("__dirname", System.getProperty("user.dir"), ScriptContext.ENGINE_SCOPE);
    }

    public void start(String... args) throws ScriptException, FileNotFoundException, NoSuchMethodException {
        if (args != null && args.length > 0) {
            ENGINE.eval(new FileReader(args[0]));
        } else {
            //ENGINE.eval(new FileReader("www/zjdbc-test.js"));
            //ENGINE.eval(new FileReader("www/zjdbc.js"));
            //ENGINE.eval(new FileReader("www/zestyjs.js"));
            //ENGINE.eval(new FileReader("www/tasks2/app.js"));
            //ENGINE.eval(new FileReader("www/tasks2/test-ejs.js"));
            //ENGINE.eval(new FileReader("www/tasks2/test-tmpl.js"));
            //ENGINE.eval(new FileReader("www/tasks2/test-nunjucks.js"));
            //ENGINE.eval(new FileReader("www/tasks2/test-template7.js"));
            ENGINE.eval(new FileReader("www/tasks2/test-temple.js"));
            //ENGINE.eval(new FileReader("www/todoapp/index.js"));
        }
    }

    public static void ping() throws ScriptException {
        ENGINE.eval("print('very much alive here');");
    }

    public static void main(String... args) throws ScriptException, FileNotFoundException, NoSuchMethodException {
        new ZestyJs().start(args);
    }
}
