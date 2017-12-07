package com;

import com.core.Context;
import com.core.Logger;
import com.core.WbException;
import com.rest.Rest;
import com.db.DatabaseConnector;
import com.websocket.WebSocketServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Arrays;
import java.util.Scanner;

import org.glassfish.grizzly.http.server.HttpServer;

/**
 * Main is responsible for server startup and configuration.
 * @author Stephen Davidson
 */
public class Main {

	public static final int DB_CONNECTION_NUM_RETRIES = 5;

	// Local config
	public static final String LOCAL_DB_HOST = "jdbc:mysql://localhost:3306/mysql";
	public static final String LOCAL_DB_USER = "root";
	public static final String LOCAL_URI = "http://localhost/whiteboarder/";
	public static final int LOCAL_PORT = 8080;

	// Prod config
	public static final String PROD_DB_HOST = "jdbc:mysql://mysql.cs.iastate.edu:3306/db309ytc1";
	public static final String PROD_DB_USER = "dbu309ytc1";
	public static final String PROD_DB_PASS = "sffwC#x#";
	public static final String PROD_URI = "http://proj-309-yt-c-1.cs.iastate.edu/whiteboarder/";
	public static final int PROD_PORT = 80;

	/**
	 * Startup point of the Whiteboarder server.
	 * @param args specifies whether or not the server is running on the prod server.
	 */
	public static void main(String[] args) throws Exception {
		boolean isLocal = true;
		if (args.length == 1 && args[0].equals("prod"))
			isLocal = false;

		String dbHost = isLocal ? LOCAL_DB_HOST : PROD_DB_HOST;
		String dbUser = isLocal ? LOCAL_DB_USER : PROD_DB_USER;
		String dbPass = isLocal ? null : PROD_DB_PASS;
		String uri = isLocal ? LOCAL_URI : PROD_URI;
		int port = isLocal ? LOCAL_PORT : PROD_PORT;
		writeRunType(isLocal);

		Logger.setupLogger();
		DatabaseConnector dbc = startDatabaseConnection(dbHost, dbUser, dbPass, isLocal);
		Context ctx = new Context(null, dbc, isLocal);
		HttpServer server = Rest.setupServer(ctx, uri, port);
		WebSocketServer.startServer(ctx, server);
		System.in.read();
		server.shutdownNow();
		endDatabaseConnection(dbc);
	}

	/**
	 * Write run type writes a file to the statically served web directory with
	 * run configuration information for the client.
	 * @param isLocal whether or not the server is running locally.
	 */
	public static void writeRunType(boolean isLocal) throws IOException {
		File f = new File("../whiteboarder-web/runtype.txt");
		f.delete();
		f.createNewFile();

		List<String> line = Arrays.asList(isLocal ? "local" : "prod");
		Path path = Paths.get("../whiteboarder-web/runtype.txt");
		Files.write(path, line, Charset.forName("UTF-8"));
	}

	/**
	 * startDatabaseConnection establishes a connection the the MySQL database.
	 * @param host the host name for the database connection.
	 * @param user the user name for the database connection.
	 * @param password the password for the database connection.
	 * @param isLocal whether or not the server is running locally.
	 * @return the connection to the MySQL database.
	 */
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

	/**
	 * endDatabase connection closes the connection to the MySQl database
	 * @param dbc the database connection.
	 */
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