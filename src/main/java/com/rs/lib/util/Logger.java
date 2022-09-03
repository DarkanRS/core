package com.rs.lib.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import com.rs.lib.Globals;
import com.rs.lib.db.DBConnection;

public final class Logger {
	private static final String ROOT_KEY = "DarkanRS";
	private static final java.util.logging.Logger DARKAN_ROOT_LOGGER = java.util.logging.Logger.getLogger(ROOT_KEY);

	public static final void setupFormat() {
		//System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT %1$tL] [%4$-7s] %5$s %n");
		DARKAN_ROOT_LOGGER.addHandler(new ConsoleHandler());
		DARKAN_ROOT_LOGGER.setUseParentHandlers(false);
	}

	public static final void setLevel(Level level) {
		DARKAN_ROOT_LOGGER.setLevel(level);
		for (Handler handler : DARKAN_ROOT_LOGGER.getHandlers())
			handler.setLevel(level);
	}

	public static final void handle(Class<?> source, String method, Throwable e) {
		if (DBConnection.getErrors() != null && DBConnection.getErrors().getDocs() != null)
			DBConnection.getErrors().logError(method, e);
		DARKAN_ROOT_LOGGER.log(Level.SEVERE, "[" + source.getSimpleName() + "." + method + "] " + (e.getClass().getEnclosingMethod() != null ? "["+e.getClass().getEnclosingMethod().getName() + "]: " : ""), e);
		if (Globals.DEBUG && e != null)
			e.printStackTrace();
	}

	public static final void handle(Class<?> source, String method, String message, Throwable e) {
		if (DBConnection.getErrors() != null && DBConnection.getErrors().getDocs() != null) {
			if (e == null)
				DBConnection.getErrors().logError(method, message);
			else
				DBConnection.getErrors().logError(method, message, e);
		}
		handleNoRecord(source, method, message, e);
	}

	public static final void handleNoRecord(Class<?> source, String method, String message, Throwable e) {
		DARKAN_ROOT_LOGGER.log(Level.SEVERE, "[" + source.getSimpleName() + "." + method + "] " + (e.getClass().getEnclosingMethod() != null ? "["+e.getClass().getEnclosingMethod().getName() + "]: " : "") + "" + message, e);
		if (Globals.DEBUG && e != null)
			e.printStackTrace();
	}

	public static final void error(Class<?> source, String method, Object msg) {
		DARKAN_ROOT_LOGGER.log(Level.SEVERE, "[" + source.getSimpleName() + "." + method + "] " + msg);
	}
	
	public static final void warn(Class<?> source, String method, Object msg) {
		DARKAN_ROOT_LOGGER.log(Level.WARNING, "[" + source.getSimpleName() + "." + method + "] " + msg);
	}

	public static final void info(Class<?> source, String method, Object msg) {
		DARKAN_ROOT_LOGGER.log(Level.CONFIG, "[" + source.getSimpleName() + "." + method + "] " + msg);
	}

	public static final void debug(Class<?> source, String method, Object msg) {
		DARKAN_ROOT_LOGGER.log(Level.FINE, "[" + source.getSimpleName() + "." + method + "] " + msg);
	}

	public static final void trace(Class<?> source, String method, Object msg) {
		DARKAN_ROOT_LOGGER.log(Level.FINER, "[" + source.getSimpleName() + "." + method + "] " + msg);
	}
}
