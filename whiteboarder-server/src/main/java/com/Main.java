package com;

import com.core.Context;
import com.core.Logger;
import com.core.WbException;
import com.rest.Rest;
import com.db.DatabaseConnector;
import java.io.IOException;
import org.glassfish.grizzly.http.server.HttpServer;

public class Main {

	public static final int DB_CONNECTION_NUM_RETRIES = 5;

	public static void main(String[] args) throws IOException {
		if (args.length > 0 && args[0] == "prod") {
			Logger.setupLogger();
			DatabaseConnector dbc = startDatabaseConnection(false);
			if (dbc != null) {
				Context ctx = new Context(null, dbc, false);
				final HttpServer server = Rest.startServer(ctx);
				// TODO: Close everything at the appropriate time
			}
		} else {
			Logger.setupLogger();
			DatabaseConnector dbc = startDatabaseConnection(true);
			if (true) {
				Context ctx = new Context(null, dbc, true);
				final HttpServer server = Rest.startServer(ctx);
				System.in.read();
				server.stop();
				endDatabaseConnection(dbc);
			}
		}
	}

	public static DatabaseConnector startDatabaseConnection(boolean isLocal) {
		DatabaseConnector dbc = new DatabaseConnector(isLocal);

		int retry = 0;
		while (retry < DB_CONNECTION_NUM_RETRIES) {
			try {
				dbc.startConnection();
				return dbc;
			} catch (WbException e) {
				retry++;
			}
		}
		return null;
	}

	public static boolean endDatabaseConnection(DatabaseConnector dbc) {
		int retry = 0;
		while (retry < DB_CONNECTION_NUM_RETRIES) {
			try {
				dbc.endConnection();
				return true;
			} catch (WbException e) {
				retry++;
				continue;
			}
		}
		return false;
	}
}