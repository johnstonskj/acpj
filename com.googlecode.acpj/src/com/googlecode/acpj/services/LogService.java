/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.services;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.googlecode.acpj.channels.ChannelPoisonedException;

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

	/**
	 * Construct a new log service.
	 */
	public LogService() {
		setChannelName(CHANNEL_NAME);
	}

	/**
	 * Run the logger service, this basically takes standard Java 
	 * {@link java.util.logging.LogRecord} messages and outputs them
	 * using a default logger configuration.
	 */
	@Override
	public void run() {
		Logger logger = Logger.getLogger("com.googlecode.acpj.services.logger");
		try {
			getReadPort().claim();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Failed to claim logger port.", t);
		}
		while (true) {
			LogRecord request =  null;
			try {
				request = getNextRequest();
			} catch (ChannelPoisonedException e) {
				break;
			} catch (Throwable t) {
				logger.log(Level.SEVERE, "Failed to read logger queue.", t);
			}		
			logger.log(request);
		}
	}

}
