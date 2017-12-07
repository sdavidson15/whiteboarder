package com.core;

import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * Logger is the class responsible for handling the server logs.
 * @author Stephen Davidson
 */
public class Logger {
	public static java.util.logging.Logger log;

	/**
	 * setupLogger creates a file that the server will write logs too, along with terminal logs.
	 */
	public static void setupLogger() {
		try {
			log = java.util.logging.Logger.getLogger("WhiteboarderServer");
			FileHandler fh = new FileHandler("whiteboarder-server.log");
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
