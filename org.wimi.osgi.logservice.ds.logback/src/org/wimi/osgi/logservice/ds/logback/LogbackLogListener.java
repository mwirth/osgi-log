package org.wimi.osgi.logservice.ds.logback;

import java.util.HashMap;

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * converts LogEntry-Objects into calls to slf4j
 */
public class LogbackLogListener implements LogListener
{
	HashMap<Long, Logger> loggers = new HashMap<Long, org.slf4j.Logger>();

	/**
	 * called from LogReaderService
	 */
	public void logged(LogEntry log)
	{
		if (( log.getBundle() == null ) || ( log.getBundle().getSymbolicName() == null ))
		{
			// if there is no name, it's probably the framework emitting a log
			// This should not happen and we don't want to log something anonymous
			return;
		}

		// Retrieve a Logger object, or create it if none exists.
		Logger logger = loggers.get(log.getBundle().getBundleId());
		if (logger == null)
		{
			logger = LoggerFactory.getLogger(log.getBundle().getSymbolicName());
			loggers.put(log.getBundle().getBundleId(), logger);
		}

		// If there is an exception available, use it, otherwise just log
		// the message
		if (log.getException() != null)
		{
			switch (log.getLevel())
			{
			case LogService.LOG_DEBUG:
				logger.debug(log.getMessage(), log.getException());
				break;
			case LogService.LOG_INFO:
				logger.info(log.getMessage(), log.getException());
				break;
			case LogService.LOG_WARNING:
				logger.warn(log.getMessage(), log.getException());
				break;
			case LogService.LOG_ERROR:
				logger.error(log.getMessage(), log.getException());
				break;
			}
		}
		else
		{
			switch (log.getLevel())
			{
			case LogService.LOG_DEBUG:
				logger.debug(log.getMessage());
				break;
			case LogService.LOG_INFO:
				logger.info(log.getMessage());
				break;
			case LogService.LOG_WARNING:
				logger.warn(log.getMessage());
				break;
			case LogService.LOG_ERROR:
				logger.error(log.getMessage());
				break;
			}
		}
	}
}