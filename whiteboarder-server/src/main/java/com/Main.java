package com;

import com.core.Logger;
import com.rest.Rest;
import com.db.DatabaseConnector;
import java.io.IOException;
import org.glassfish.grizzly.http.server.HttpServer;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length > 0 && args[0] == "prod") {
			Logger.setupLogger();
			DatabaseConnector dbc = new DatabaseConnector(false);
			final HttpServer server = Rest.startServer(dbc, false);
			// TODO: Close everything at the appropriate time
		} else {
			Logger.setupLogger();
			DatabaseConnector dbc = new DatabaseConnector(true);
			final HttpServer server = Rest.startServer(dbc, true);
			System.in.read();
			server.stop();
			dbc.endConnection();
		}
	}
}