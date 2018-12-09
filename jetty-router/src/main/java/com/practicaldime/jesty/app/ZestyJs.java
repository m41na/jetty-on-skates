package com.practicaldime.jesty.app;

import java.io.File;
import java.io.IOException;

import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import com.practicaldime.zesty.app.AppProvider;


public class ZestyJs {

	private static final Context context = Context.newBuilder("js")
			.allowIO(true)
			.allowCreateThread(true)
			.allowHostAccess(true).build();

	static {
		String baseDir = System.getProperty("user.dir");

		Value bindings = context.getBindings("js");
		bindings.putMember("zesty", AppProvider.class);
		bindings.putMember("__dirname", baseDir);
	}

	public void start(String... args) throws ScriptException, NoSuchMethodException, IOException {
		if (args != null && args.length > 0) {
			context.eval(Source.newBuilder("js", new File(args[0])).build());
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
			//context.eval(Source.newBuilder("js", new File("www/lib/jvm-npm.js")).build());
			//context.eval(Source.newBuilder("js", new File("www/lib/test-load-handlebars.js")).build());
			context.eval(Source.newBuilder("js", new File("www/zestyjs.js")).build());
			//ENGINE.eval(new FileReader("www/sample.js"));
			//ENGINE.eval(new FileReader("www/lib/test-load-ejs.js"));
			//$GRAALVM_HOME/bin/node --jvm --polyglot script.js
		}
	}

	public static void ping() throws ScriptException {
		context.eval("js", "print('very much alive here');");
	}

	public static void main(String... args) throws ScriptException, NoSuchMethodException, IOException {
		new ZestyJs().start(args);
	}
}
