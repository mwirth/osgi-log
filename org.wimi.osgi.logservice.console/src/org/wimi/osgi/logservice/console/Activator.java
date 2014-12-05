package org.wimi.osgi.logservice.console;

import java.util.Iterator;
import java.util.LinkedList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogReaderService;

public class Activator implements BundleActivator, ServiceListener
{
	private ConsoleLogImpl consoleLog = new ConsoleLogImpl();
	private LinkedList<LogReaderService> logReaderServices = new LinkedList<LogReaderService>();
	private BundleContext bundleContext;

	// ServiceListener to dynamically keep track of all the LogReaderService being registered or unregistered
	public void start(BundleContext context) throws Exception
	{
		bundleContext = context;

		// list of all the registered LogReaderService
		// add consoleLog listener
		// filter so that only events related to LogReaderService are received
		String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
		try
		{
			context.addServiceListener(this, filter);
		}
		catch (InvalidSyntaxException e)
		{
			e.printStackTrace();
		}

		ServiceReference<?>[] refs = context.getServiceReferences(org.osgi.service.log.LogReaderService.class.getName(), null);
		if (refs != null)
		{
			for (int i = 0; i < refs.length; i++)
			{
				LogReaderService service = (LogReaderService) context.getService(refs[i]);
				if (service != null)
				{
					logReaderServices.add(service);
					service.addLogListener(consoleLog);
				}
			}
		}
	}

	public void stop(BundleContext context) throws Exception
	{
		for (Iterator<LogReaderService> i = logReaderServices.iterator(); i.hasNext();)
		{
			LogReaderService lrs = i.next();
			lrs.removeLogListener(consoleLog);
			i.remove();
		}
	}

	@Override
	public void serviceChanged(ServiceEvent event)
	{
		LogReaderService lrs = (LogReaderService) bundleContext.getService(event.getServiceReference());
		if (lrs != null)
		{
			if (event.getType() == ServiceEvent.REGISTERED)
			{
				logReaderServices.add(lrs);
				lrs.addLogListener(consoleLog);
			}
			else if (event.getType() == ServiceEvent.UNREGISTERING)
			{
				lrs.removeLogListener(consoleLog);
				logReaderServices.remove(lrs);
			}
		}
	}
}
