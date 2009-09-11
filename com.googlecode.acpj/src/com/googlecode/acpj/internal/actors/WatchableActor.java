package com.googlecode.acpj.internal.actors;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelRegistry;
import com.googlecode.acpj.channels.WritePort;
import com.googlecode.acpj.services.ActorState;
import com.googlecode.acpj.services.ActorStateMessage;
import com.googlecode.acpj.services.WatchdogService;

public class WatchableActor implements Runnable {

	private Actor actor;
	private Runnable actual;
	
	public WatchableActor(Actor actor, Runnable actual) {
		this.actor = actor;
		this.actual = actual;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		Channel<ActorStateMessage> watchdogChannel = (Channel<ActorStateMessage>) ChannelRegistry.getInstance().lookupOrNull(WatchdogService.CHANNEL_NAME);
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
