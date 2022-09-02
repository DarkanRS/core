package com.rs.lib.util;

import java.util.logging.Level;

import com.rs.lib.db.DBConnection;

public final class Logger {
	
	public static final void setupFormat() {
		System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s %2$s %5$s%6$s%n");
	}

	public static final void handle(Object caller, Throwable e) {
		if (DBConnection.getErrors() != null && DBConnection.getErrors().getDocs() != null)
			DBConnection.getErrors().logError(e);
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.SEVERE, "SEVERE [" + caller.getClass().getEnclosingMethod().getName() + "]", e);
	}
	
	public static final void handle(Object caller, String message, Throwable e) {
		if (DBConnection.getErrors() != null && DBConnection.getErrors().getDocs() != null) {
			if (e == null)
				DBConnection.getErrors().logError(message);
			else
				DBConnection.getErrors().logError(e);
		}
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.SEVERE, "SEVERE [" + caller.getClass().getEnclosingMethod().getName() + "]: " + message, e);
	}
	
	public static final void handleNoRecord(Object caller, String message, Throwable e) {
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.SEVERE, "SEVERE [" + caller.getClass().getEnclosingMethod().getName() + "]: exception:", e);
	}
	
	public static final void error(Object caller, Object msg) {
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.SEVERE, "SEVERE [" + caller.getClass().getEnclosingMethod().getName() + "]: " + msg);
	}
	
	public static final void info(Object caller, Object msg) {
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.CONFIG, "DEBUG [" + caller.getClass().getEnclosingMethod().getName() + "]: " + msg);
	}
	
	public static final void debug(Object caller, Object msg) {
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.FINE, "LOG [" + caller.getClass().getEnclosingMethod().getName() + "]: " + msg);
	}
	
	public static final void trace(Object caller, Object msg) {
		java.util.logging.Logger.getLogger(caller.getClass().getEnclosingClass().getSimpleName()).log(Level.FINER, "TRACE [" + caller.getClass().getEnclosingMethod().getName() + "]: " + msg);
	}
}
