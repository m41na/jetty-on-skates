package com.jarredweb.editor.app;

import com.coveo.nashorn_modules.FilesystemFolder;
import com.coveo.nashorn_modules.Require;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class NashJs {

    private static final NashornScriptEngine ENGINE = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

    public static void main(String... args) {
        try {
            FilesystemFolder rootFolder = FilesystemFolder.create(new File("www"), "UTF-8");
            Require.enable(ENGINE, rootFolder);
            ScriptContext context = ENGINE.getContext();
            context.setAttribute("runner", new NashJs(), ScriptContext.ENGINE_SCOPE);

            printHello();
            loadKnockout();
            loadNashjs();
            loadVuejs();
        } catch (ScriptException | FileNotFoundException | NoSuchMethodException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void printHello() throws ScriptException {
        ENGINE.eval("print('Hello from javascript!');");
    }

    public static void loadNashjs() throws ScriptException, FileNotFoundException, NoSuchMethodException {
        ENGINE.eval(new FileReader("www/catalog/nashjs.js"));
        Invocable invocable = (Invocable) ENGINE;

        Object result = invocable.invokeFunction("sayHello", "John Doe");
        System.out.println(result);
        System.out.println(result.getClass());
    }

    public static void loadKnockout() throws ScriptException, FileNotFoundException, NoSuchMethodException {
        ENGINE.eval(new FileReader("www/public/js/knockout-3.4.2.js"));
        Invocable invocable = (Invocable) ENGINE;
    }

    public static void loadVuejs() throws ScriptException, FileNotFoundException, NoSuchMethodException {
        ENGINE.eval(new FileReader("www/public/js/vue.js"));
        Invocable invocable = (Invocable) ENGINE;

        Object result = invocable.invokeFunction("printVueVersion");
        System.out.println(result);

    }

    public static void printType(Object object) {
        System.out.println(object.getClass());
    }

    public static void printObjectMirror(ScriptObjectMirror mirror) {
        System.out.println(mirror.getClassName() + ": " + Arrays.toString(mirror.getOwnKeys(true)));
    }

    public static String javaHello(String name) {
        return String.format("Hello %s from Java!", name);
    }

    public static void getFullName(ScriptObjectMirror person) {
        System.out.println("Full name is: " + person.callMember("getFullName"));
    }

    public int add(int a, int b) {
        return a + b;
    }
}
