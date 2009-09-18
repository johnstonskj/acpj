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

import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelPoisonedException;
import com.googlecode.acpj.channels.ChannelRegistry;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * A pre-built, simple, service that monitors all the events generated by the 
 * actor runtime that denote the birth and death of actors.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class WatchdogService extends BasicService<ActorStateMessage> {
	
	/**
	 * The publicly registered channel name, clients use this to lookup the channel
	 * prior to sending requests.
	 */
	public static final String CHANNEL_NAME = "com.googlecode.acpj.services.WatchdogNotificationChannel";
	
	public WatchdogService() {
		setChannelName(CHANNEL_NAME);
	}

	/**
	 * The watchdog service logic, it receives new messages, of the form
	 * {@link ActorStateMessage} and either sends them to the logger service,
	 * or writes them to the console.
	 */
	@Override
	public void run() {
		Channel<LogRecord> loggerChannel = null;
		WritePort<LogRecord> loggerPort = null;
		while (true) {
			if (loggerChannel == null) {
				loggerChannel = ChannelRegistry.getInstance().lookupOrNull(LogService.CHANNEL_NAME);
				if (loggerChannel != null) {
					loggerPort = loggerChannel.getWritePort(true);
				}
				try {
					getReadPort().claim();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			ActorStateMessage message =  null;
			try {
				message = getNextRequest();
			} catch (ChannelPoisonedException e) {
				break;
			} catch (Throwable t) {
				t.printStackTrace();
			}
			final String msg = String.format("%10s - %s", message.getActorState(), message.getActorName());
			if (loggerPort != null) {
				loggerPort.write(new LogRecord(Level.INFO, msg));
			} else {
				System.out.println(String.format("%10s - %s", message.getActorState(), message.getActorName()));
			}
		}
	}
}