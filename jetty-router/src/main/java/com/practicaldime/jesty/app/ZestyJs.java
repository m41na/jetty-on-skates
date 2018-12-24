package com.practicaldime.jesty.app;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import com.google.gson.Gson;
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
			//context.eval(Source.newBuilder("js", new File("www/zestyjs.js")).build());
			renderHandlebars();
			//ENGINE.eval(new FileReader("www/sample.js"));
			//ENGINE.eval(new FileReader("www/lib/test-load-ejs.js"));
			//$GRAALVM_HOME/bin/node --jvm --polyglot script.js
		}
	}
	
	public static void renderHandlebars() throws ScriptException, NoSuchMethodException, IOException {
		Map<String, Object> model = new HashMap<>();
		List<Todo> tasks = Arrays.asList(new Todo("fishing", false), new Todo("baking", true), new Todo("skiing", false));
		model.put("tasks", tasks);
		
		Value bindings = context.getBindings("js");		
		String strModel = new Gson().toJson(model);
		bindings.putMember("model", strModel);
		bindings.putMember("dist", "./");
		context.eval(Source.newBuilder("js", new File("www/lib/test-render-handlebars.js")).build());
	}

	public static void ping() throws ScriptException {
		context.eval("js", "print('very much alive here');");
	}

	public static void main(String... args) throws ScriptException, NoSuchMethodException, IOException {
		new ZestyJs().start(args);
	}
	
	static class Todo{
		
		private String name;
		private boolean completed;
		
		public Todo(String name, boolean completed) {
			super();
			this.name = name;
			this.completed = completed;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isCompleted() {
			return completed;
		}

		public void setCompleted(boolean completed) {
			this.completed = completed;
		}
	}
}
