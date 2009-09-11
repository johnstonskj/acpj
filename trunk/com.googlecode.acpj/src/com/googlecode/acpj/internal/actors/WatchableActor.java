/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.actors;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelRegistry;
import com.googlecode.acpj.channels.WritePort;
import com.googlecode.acpj.services.ActorState;
import com.googlecode.acpj.services.ActorStateMessage;
import com.googlecode.acpj.services.WatchdogService;

/**
 * <p>
 * Internal - a class that wraps the Runnable the client gave us so that we can
 * report lifecycle events to the Watchdog service.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class WatchableActor implements Runnable {

	private Actor actor;
	private Runnable actual;
	
	public WatchableActor(Actor actor, Runnable actual) {
		this.actor = actor;
		this.actual = actual;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Channel<ActorStateMessage> watchdogChannel = ChannelRegistry.getInstance().lookupOrNull(WatchdogService.CHANNEL_NAME);
		WritePort<ActorStateMessage> watchdogPort = null;
		if (watchdogChannel != null) {
			watchdogPort = watchdogChannel.getWritePort(true);
		}
		try {
			if (watchdogPort != null) {
				watchdogPort.write(new ActorStateMessage(actor.getName(), ActorState.STARTED));
			}
			actual.run();
			if (watchdogPort != null) {
				watchdogPort.write(new ActorStateMessage(actor.getName(), ActorState.FINISHED));
			}
		} catch (Throwable t) {
			if (watchdogPort != null) {
				final String msg = String.format("%s (%s)", actor.getName(), t.toString());
				watchdogPort.write(new ActorStateMessage(msg, ActorState.DIED));
			}
		}
	}
}
