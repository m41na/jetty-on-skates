package com.jarredweb.xmlstore.js;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsApp {
    
    public static String hello(String name) {
        System.out.format("Hi there from Java, %s", name);
        return "greetings from java";
    }

    public static void main(String... args) throws ScriptException, FileNotFoundException, NoSuchMethodException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("require", Js.class);
        engine.eval(new InputStreamReader(JsApp.class.getResourceAsStream("/hello.js")));        
        

        Invocable invocable = (Invocable) engine;

        Object result = invocable.invokeFunction("fun1", "Peter Parker");
        System.out.println(result);
        System.out.println(result.getClass());
    }
}
