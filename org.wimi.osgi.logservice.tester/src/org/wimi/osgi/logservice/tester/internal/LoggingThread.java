/**
 *
 */
package org.wimi.osgi.logservice.tester.internal;

import org.osgi.service.log.LogService;

/**
 * @author mwirth
 */
public class LoggingThread extends Thread
{
	private int waittime;
	private boolean run;

	private LogService logService;
	private String message;

	/**
	 * @param message
	 * @param seconds
	 */
	public LoggingThread(LogService logService, String message, int seconds)
	{
		super();
		this.logService = logService;
		this.waittime = seconds * 1000;
		this.message = message;
	}

	@Override
	public void run()
	{
		run = true;
		int i = 0;
		while (run)
		{
			if (logService != null)
				logService.log(LogService.LOG_INFO, message + ": " + i);
			i++;
			try
			{
				System.out.println("sleeping ..........");
				Thread.sleep(waittime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				run = false;
			}
		}
	}

	/**
	 *
	 */
	public void cancle()
	{
		run = false;
	}

}
