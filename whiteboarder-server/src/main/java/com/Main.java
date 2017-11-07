package com;

import com.core.Context;
import com.core.Logger;
import com.core.WbException;
import com.rest.Rest;
import com.db.DatabaseConnector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.glassfish.grizzly.http.server.HttpServer;

public class Main {

	public static final int DB_CONNECTION_NUM_RETRIES = 5;
	public static final String LOCAL_CONFIG_PATH = "local-config.txt";

	public static void main(String[] args) throws Exception {
		String cfgPath = LOCAL_CONFIG_PATH;
		boolean isLocal = true;
		if (args.length == 1) {
			cfgPath = args[0];
			isLocal = false;
		}

		Map<String, String> cfg = getConfig(cfgPath);

		Logger.setupLogger(cfg.get("log-location"));
		DatabaseConnector dbc = startDatabaseConnection(cfg.get("db-host"), cfg.get("db-user"), cfg.get("db-pass"),
				isLocal);
		Context ctx = new Context(null, dbc, isLocal);
		final HttpServer server = Rest.startServer(ctx, cfg.get("rest-uri"), Integer.parseInt(cfg.get("port")));
		System.in.read();
		server.shutdownNow();
		endDatabaseConnection(dbc);
	}

	public static Map<String, String> getConfig(String path) throws FileNotFoundException {
		HashMap<String, String> cfg = new HashMap<String, String>();
		File f = new File(path);
		Scanner scanner = new Scanner(f);

		while (scanner.hasNext()) {
			String[] fields = scanner.nextLine().split("=");
			cfg.put(fields[0].trim(), fields[1].trim());
		}
		scanner.close();
		return cfg;
	}

	public static DatabaseConnector startDatabaseConnection(String host, String user, String password,
			boolean isLocal) {

		DatabaseConnector dbc = new DatabaseConnector(host, user, password, isLocal);
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

	public static void endDatabaseConnection(DatabaseConnector dbc) {
		int retry = 0;
		while (retry < DB_CONNECTION_NUM_RETRIES) {
			try {
				dbc.endConnection();
				return;
			} catch (WbException e) {
				retry++;
				continue;
			}
		}
	}
}