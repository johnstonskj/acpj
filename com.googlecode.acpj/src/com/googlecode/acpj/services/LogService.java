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

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.ChannelRegistry;

/**
 * <p>
 * A pre-built, simple, logging service that accepts standard Java util
 * {@link java.util.logging.LogRecord log records} and logs them.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class LogService {

	public static final String CHANNEL_NAME = "com.googlecode.acpj.services.MessageChannel";
	public static final String ACTOR_NAME = "com.googlecode.acpj.services.LoggerActor";

	private static Channel<LogRecord> loggerChannel = null;
	private static Actor loggerActor = null;
	private static Object serviceLock = new Object();

	public static void start() {
		synchronized (serviceLock) {
			if (loggerActor == null || !loggerActor.isRunning()) {
				loggerChannel = ChannelFactory.getInstance().createAnyToOneChannel(CHANNEL_NAME, -1);
				ChannelRegistry.getInstance().register(loggerChannel, CHANNEL_NAME, false);
				loggerActor = ActorFactory.getInstance().createActor(new LogServiceActor(loggerChannel.getReadPort(false)), ACTOR_NAME);
			}
		}
	}

	public static void stop() {
		synchronized (serviceLock) {
			if (loggerActor != null && loggerActor.isRunning()) {
				loggerChannel.poison();
				loggerChannel = null;
				loggerActor = null;
			}
		}
	}
}
