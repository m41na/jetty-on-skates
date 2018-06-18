package com.jarredweb.jettsey.app;

import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ZestyJs {

    private static final ScriptEngine ENGINE = new ScriptEngineManager().getEngineByName("nashorn");

    static {
        ScriptContext context = ENGINE.getContext();
        context.setAttribute("zesty", new AppServer(), ScriptContext.ENGINE_SCOPE);
    }

    public static void ping() throws ScriptException {
        ENGINE.eval("print('up and about');");
    }

    public static void main(String... args) throws ScriptException, FileNotFoundException, NoSuchMethodException {
        if (args != null && args.length > 0) {
            ENGINE.eval(new FileReader(args[0]));
        } else {
            ENGINE.eval(new FileReader("www/zestyjs.js"));
        }
        Invocable invocable = (Invocable) ENGINE;

        Object result = invocable.invokeFunction("ping");
        System.out.println(result);
    }
}
