/**
 *
 */
package org.wimi.osgi.logservice.console;

import java.util.LinkedList;

import org.osgi.service.log.LogReaderService;

/**
 * @author mwirth
 */
public class RegisterLogListener
{
	private ConsoleLogImpl consoleLog = new ConsoleLogImpl();
	private LinkedList<LogReaderService> logReaderServices = new LinkedList<LogReaderService>();

	public void bind(LogReaderService lrs)
	{
		logReaderServices.add(lrs);
		lrs.addLogListener(consoleLog);
	}

	public void unbind(LogReaderService lrs)
	{
		lrs.removeLogListener(consoleLog);
		logReaderServices.remove(lrs);
	}

}
