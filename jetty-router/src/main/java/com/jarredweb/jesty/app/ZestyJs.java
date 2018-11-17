package com.jarredweb.jesty.app;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.coveo.nashorn_modules.FilesystemFolder;
import com.coveo.nashorn_modules.Require;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

public class ZestyJs {

	private static final NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();
	public static final ScriptEngine ENGINE = FACTORY.getScriptEngine("-scripting", "-doe", "-ot", "--language=es6");

	static {
		String baseDir = System.getProperty("user.dir");

		ScriptContext context = ENGINE.getContext();
		context.setAttribute("zesty", new AppProvider(), ScriptContext.ENGINE_SCOPE);
		context.setAttribute("__dirname", baseDir, ScriptContext.ENGINE_SCOPE);

		try {
			Path nodeLibs = Paths.get(baseDir);
			FilesystemFolder rootFolder = FilesystemFolder.create(nodeLibs.toFile(), "UTF-8");
			Require.enable((NashornScriptEngine) ENGINE, rootFolder);
		} catch (Exception e) {
			System.err.print("Could not bind to Nodejs modules folder");
			try {
				// load jvm-npm.js
				ENGINE.eval("www/jvm-npm.js");
			} catch (ScriptException ex) {
				System.err.print("Could not load Nodejs modules folder");
			}
		}
	}

	public void start(String... args) throws ScriptException, NoSuchMethodException, IOException {
		if (args != null && args.length > 0) {
			ENGINE.eval(new FileReader(args[0]));
		} else {
			// ENGINE.eval(new FileReader("www/zjdbc-test.js"));
			// ENGINE.eval(new FileReader("www/zjdbc.js"));
			// ENGINE.eval(new FileReader("www/zestyjs.js"));
			// ENGINE.eval(new FileReader("www/tasks2/app.js"));
			// ENGINE.eval(new FileReader("www/tasks2/test-ejs.js"));
			// ENGINE.eval(new FileReader("www/tasks2/test-tmpl.js"));
//			Bindings bindings = ZestyJs.ENGINE.createBindings();
//			bindings.put("model", "hello there");
//			String script = new String(
//					Files.readAllBytes(Paths.get(System.getProperty("user.dir"), "www/lib/test-apply-args.js")));
//			System.out.println(script);
//			ENGINE.eval(script, bindings);
			// ENGINE.eval(new FileReader("www/todoapp/index.js"));
			ENGINE.eval(new FileReader("www/lib/test-load-handlebars.js"));
			//ENGINE.eval(new FileReader("www/lib/test-load-ejs.js"));
		}
	}

	public static void ping() throws ScriptException {
		ENGINE.eval("print('very much alive here');");
	}

	public static void main(String... args) throws ScriptException, NoSuchMethodException, IOException {
		new ZestyJs().start(args);
	}
}
