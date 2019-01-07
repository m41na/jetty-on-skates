package com.practicaldime.jesty.app;

import com.google.common.collect.Maps;
import com.practicaldime.zesty.app.AppServer;

public class ZestyWp {

	public static void main(String... args) {
		// start server
		int port = 8080;
		String host = "localhost";
		
		AppServer app = new AppServer(Maps.newHashMap());
		app.router().wordpress("/var/www/wordpress", "http://127.0.0.1:9000")
			.listen(port, host, (msg)-> System.out.println(msg));
	}
}
