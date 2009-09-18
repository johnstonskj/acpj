/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.services;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * <p>
 * A pre-built, simple, service that wraps a standard Log4J output.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class LogService extends BasicService<LogRecord> {

	/**
	 * The publicly registered channel name, clients use this to lookup the channel
	 * prior to sending requests.
	 */
	public static final String CHANNEL_NAME = "com.googlecode.acpj.services.LoggingChannel";

	private Logger logger = null;
	
	/**
	 * Construct a new log service.
	 */
	public LogService() {
		setChannelName(CHANNEL_NAME);
	}
	
	/**
	 * Cache a Log4J Logger instance.
	 */
	@Override
	public void startup() {
		this.logger = Logger.getLogger("com.googlecode.acpj.services.logger");
		super.startup();
	}

	/**
	 * log the request using the default Logger instance.
	 *
	 * @param request the request read from the channel.
	 * 
	 * @return <code>true</code> and the service will continue to read requests.
	 */
	@Override
	public boolean handleRequest(LogRecord request) {
		this.logger.log(request);
		return true;
	}

}
