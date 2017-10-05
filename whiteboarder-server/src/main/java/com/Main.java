package com;

import com.rest.Rest;
import com.db.DatabaseConnector;
import java.io.IOException;
import org.glassfish.grizzly.http.server.HttpServer;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length > 0 && args[0] == "prod") {
			DatabaseConnector dbc = new DatabaseConnector(false);
			final HttpServer server = Rest.startServer(false);
			// TODO: Close everything at the appropriate time
		} else {
			DatabaseConnector dbc = new DatabaseConnector(true);
			final HttpServer server = Rest.startServer(true);
			System.in.read();
			server.stop();
			dbc.endConnection();
		}
	}
}