/**
 *
 */
package org.wimi.osgi.logservice.ds.logback;

import java.util.LinkedList;

import org.osgi.service.log.LogReaderService;

/**
 * @author mwirth
 */
public class RegisterLogListener
{
	private LogbackLogListener logbackLogListener = new LogbackLogListener();
	private LinkedList<LogReaderService> reader = new LinkedList<LogReaderService>();

	public void bind(LogReaderService lrs)
	{
		reader.add(lrs);
		lrs.addLogListener(logbackLogListener);
	}

	public void unbind(LogReaderService lrs)
	{
		lrs.removeLogListener(logbackLogListener);
		reader.remove(lrs);
	}
}
