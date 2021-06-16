package com.rs.lib.thread;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import com.rs.lib.util.Logger;

public class CatchExceptionTimerTask implements TimerTask {
	
	private TimerTask task;
	
	public CatchExceptionTimerTask(TimerTask task) {
		this.task = task;
	}

	@Override
	public void run(Timeout timeout) {
		try {
			task.run(timeout);
		} catch(Throwable e) {
			Logger.handle(e);
		}
	}

}
