package com.core;

import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Logger {
	public static java.util.logging.Logger log;

	public static void setupLogger(String filename) {
		try {
			log = java.util.logging.Logger.getLogger("WhiteboarderServer");
			FileHandler fh = new FileHandler(filename);
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
