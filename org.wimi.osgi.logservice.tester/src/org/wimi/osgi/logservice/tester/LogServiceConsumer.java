/**
 *
 */
package org.wimi.osgi.logservice.tester;

import org.osgi.service.log.LogService;
import org.wimi.osgi.logservice.tester.internal.LoggingThread;

/**
 * @author mwirth
 */
public class LogServiceConsumer
{

	private LoggingThread thread;

	public void bind(LogService logService)
	{
		logService.log(LogService.LOG_INFO, "bind logService");

		thread = new LoggingThread(logService, "lggingthread started from DS", 2);
		thread.start();
	}

	public void unbind(LogService ls)
	{
		thread.cancle();
	}
}
