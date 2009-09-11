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
import com.googlecode.acpj.channels.ReadPort;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
class WatchdogServiceActor implements Runnable {

	private ReadPort<ActorStateMessage> watchdogPort = null;
	
	public WatchdogServiceActor(ReadPort<ActorStateMessage> watchdogPort) {
		this.watchdogPort = watchdogPort;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		Channel<LogRecord> loggerChannel = null;
		WritePort<LogRecord> loggerPort = null;
		while (true) {
			if (loggerChannel == null) {
				loggerChannel = (Channel<LogRecord>) ChannelRegistry.getInstance().lookupOrNull(LogService.CHANNEL_NAME);
				if (loggerChannel != null) {
					loggerPort = loggerChannel.getWritePort(true);
				}
				try {
					watchdogPort.claim();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			ActorStateMessage message =  null;
			try {
				message = watchdogPort.read();
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
