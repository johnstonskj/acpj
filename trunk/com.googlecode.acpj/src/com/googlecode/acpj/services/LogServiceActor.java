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
import com.googlecode.acpj.channels.ReadPort;

/**
 * <p>
 * Internal - actor that implements the Logger service itself.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
class LogServiceActor implements Runnable {

	private Logger logger = Logger.getLogger("com.googlecode.acpj.services.logger");
	
	private ReadPort<LogRecord> loggerPort = null;
	
	public LogServiceActor(ReadPort<LogRecord> loggerPort) {
		this.loggerPort = loggerPort;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			loggerPort.claim();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Failed to claim logger port.", t);
		}
		while (true) {
			LogRecord record =  null;
			try {
				record = loggerPort.read();
			} catch (ChannelPoisonedException e) {
				break;
			} catch (Throwable t) {
				logger.log(Level.SEVERE, "Failed to read logger queue.", t);
			}		
			logger.log(record);
		}
	}
}
