package org.wimi.osgi.logservice.tester;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.wimi.osgi.logservice.tester.internal.LoggingThread;

public class Activator implements BundleActivator
{
	private static BundleContext context;

	private LoggingThread thread;

	static BundleContext getContext()
	{
		return context;
	}

	public void start(BundleContext context) throws Exception
	{
		ServiceTracker logServiceTracker = new ServiceTracker(context, org.osgi.service.log.LogService.class.getName(), null);
		logServiceTracker.open();
		final LogService logservice = (LogService) logServiceTracker.getService();

		if (logservice != null)
			logservice.log(LogService.LOG_INFO, "logmessage from Activator: Activator started");

		thread = new LoggingThread(logservice, "logmessage from running thread in Activator", 2);
		thread.start();
	}

	public void stop(BundleContext context) throws Exception
	{
		thread.cancle();
	}
}
