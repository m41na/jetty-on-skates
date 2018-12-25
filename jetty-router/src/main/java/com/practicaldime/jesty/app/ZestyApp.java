package com.practicaldime.jesty.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.practicaldime.jesty.todos.Task;
import com.practicaldime.jesty.todos.spring.TodosDaoSpring;
import com.practicaldime.zesty.app.AppServer;
import com.practicaldime.zesty.extras.AppWsEvents;
import com.practicaldime.zesty.servlet.HandlerRequest;
import com.practicaldime.zesty.servlet.HandlerResponse;
import com.practicaldime.zesty.servlet.HandlerServlet;

public class ZestyApp {

	private static final Logger LOG = LoggerFactory.getLogger(ZestyApp.class);

	public static void main(String... args) {
		// start database
		TodosDaoSpring todos = TodosDaoSpring.instance();

		// start server
		int port = 8080;
		String host = "localhost";

		Map<String, String> props = Maps.newHashMap();
		props.put("appctx", "/app");
		props.put("assets", "www"); //"www/handlebars-ui/dist, www/react-ui/dist"
		props.put("engine", "freemarker"); //string, jtwig, freemarker

		AppServer app = new AppServer(props);

		app.router().get("/", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.send(request.getRequestURI());
			}
		}).get("/check", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.send("<h1>GET from Async AppHandler</h1>");
			}
		}).post("/check", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.send("<h1>POST from Async AppHandler</h1>");
			}
		}).put("/check", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.send("<h1>PUT from Async AppHandler</h1>");
			}
		}).delete("/check", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.send("<h1>DELETE from Async AppHandler</h1>");
			}
		}).get("/wowza", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				Map<String, Object> model = new HashMap<>();
				model.put("title", "Wowza!!");
				model.put("number", Math.floor(Math.random() * 10));
				response.render("wowza", model);
			}
		}).get("/pong", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.redirect(app.resolve("/wowza"));
			}
		}).get("/zesty", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.download("www", "zestyjs.js", null, () -> LOG.info("download completed"));
			}
		}).get("/upload", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				response.render("upload", Maps.newHashMap());
			}
		}).post("/www/upload", "", "multipart/form-data", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String dest = request.getParameter("destination");
				request.upload(dest);
				response.redirect(app.resolve("/upload"));
			}
		}).get("/todos", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				List<Task> tasks = todos.retrieveByRange(0, 100);
				Map<String, Object> model = new HashMap<>();
				model.put("title", "Todos List");
				model.put("tasks", tasks);
				response.render("todos", model);
			}
		}).get("/todos/done", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				Boolean completed = Boolean.valueOf(request.getParameter("complete"));
				List<Task> tasks = todos.retrieveByDone(completed);
				Map<String, Object> model = new HashMap<>();
				model.put("tasks", tasks);
				response.render("todos", model);
			}
		}).get("/todos/refresh", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				List<Task> tasks = todos.retrieveByRange(0, 100);
				response.json(tasks);
			}
		}).post("/todos", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String task = request.getParameter("task");
				todos.createTask(task);

				Task created = todos.retrieveTask(task);
				response.json(created);
			}
		}).put("/todos/done", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String task = request.getParameter("task");
				boolean done = Boolean.valueOf(request.getParameter("complete"));
				todos.updateDone(task, done);

				Task updated = todos.retrieveTask(task);
				response.json(updated);
			}
		}).put("/todos/rename", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String task = request.getParameter("task");
				String newName = request.getParameter("newName");
				todos.updateName(task, newName);

				Task updated = todos.retrieveTask(newName);
				response.json(updated);
			}
		}).delete("/todos", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String task = request.getParameter("name");
				todos.deleteTask(task);

				response.redirect(303, app.resolve("/todos/refresh"));
			}
		}).delete("/todos", "application/json", "", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String task = request.getParameter("name");
				todos.deleteTask(task);

				response.json(task);
			}
		}).delete("/todos/all", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				int status = todos.clearAllTasks();
				response.json(status);
			}
		}).get("/basex/{source}", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String source = request.param("source");
				try (InputStream is = this.getClass().getResourceAsStream("/xmldata/" + source + ".xml");
						Scanner s = new Scanner(is).useDelimiter("\\A")) {
					String result = s.hasNext() ? s.next() : "";
					response.send(result);
				} catch (IOException ex) {
					response.sendStatus(400);
				}
			}
		}).post("/basex/{source}", new HandlerServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public void handle(HandlerRequest request, HandlerResponse response) {
				String source = request.param("source");
				LOG.info("xml source is {}", source);

				long size = request.capture();
				LOG.debug("request capture size is {}", size);

				if (request.body() != null) {
					String xml = new String(request.body());
					LOG.debug(xml);

					// save to disl and respond
					String type = request.getHeader("type");
					try (FileWriter fout = new FileWriter(new File("www/capture/", source + type))) {
						fout.write(xml);
					} catch (IOException ex) {
						response.status(500);
						response.send(ex.getMessage());
						return;
					}
					response.sendStatus(201);
				} else {
					response.status(500);
					response.send("no content");
				}
			}
		}).websocket("/events/*", AppWsEvents::new)
			.wordpress("/var/www/wordpress", "http://localhost:9000")
			.listen(port, host, (msg)-> System.out.println(msg));
	}
}
