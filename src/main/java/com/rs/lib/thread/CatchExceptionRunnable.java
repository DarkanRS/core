package com.rs.lib.thread;

import com.rs.lib.util.Logger;

public class CatchExceptionRunnable implements Runnable {
	
	private Runnable runnable;
	
	public CatchExceptionRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		try {
			runnable.run();
		} catch(Throwable e) {
			Logger.handle(e);
		}
	}

}
