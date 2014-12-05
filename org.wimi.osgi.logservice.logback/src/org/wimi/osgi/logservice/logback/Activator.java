package org.wimi.osgi.logservice.logback;

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

	private LogbackLogListener logbackLogListener = new LogbackLogListener();
	private LinkedList<LogReaderService> reader = new LinkedList<LogReaderService>();
	private BundleContext bundleContext;

	public void start(BundleContext context) throws Exception
	{
		bundleContext = context;

		// register class as listener
		String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
		try
		{
			context.addServiceListener(this, filter);
		}
		catch (InvalidSyntaxException e)
		{
			e.printStackTrace();
		}

		// register LogbackAdaptor to all LogReaderServices
		ServiceReference<?>[] refs = context.getServiceReferences(org.osgi.service.log.LogReaderService.class.getName(), null);
		if (refs != null)
		{
			for (int i = 0; i < refs.length; i++)
			{
				LogReaderService service = (LogReaderService) context.getService(refs[i]);
				if (service != null)
				{
					reader.add(service);
					service.addLogListener(logbackLogListener);
				}
			}
		}
	}

	public void stop(BundleContext context) throws Exception
	{
		for (Iterator<LogReaderService> i = reader.iterator(); i.hasNext();)
		{
			LogReaderService lrs = i.next();
			lrs.removeLogListener(logbackLogListener);
			i.remove();
		}
	}

	// ServiceListener to keep track of all LogReaderServices for register and unregister
	public void serviceChanged(ServiceEvent event)
	{
		LogReaderService lrs = (LogReaderService) bundleContext.getService(event.getServiceReference());
		if (lrs != null)
		{
			if (event.getType() == ServiceEvent.REGISTERED)
			{
				reader.add(lrs);
				lrs.addLogListener(logbackLogListener);
			}
			else if (event.getType() == ServiceEvent.UNREGISTERING)
			{
				lrs.removeLogListener(logbackLogListener);
				reader.remove(lrs);
			}
		}
	}
}
