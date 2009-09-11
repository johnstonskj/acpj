/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.services;

/**
 * <p>
 * Represents a message sent from the actor runtime to the {@link WatchdogService}.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ActorStateMessage {
	
	private String actorName;
	private ActorState actorState;
	
	public ActorStateMessage(String actorName, ActorState actorState) {
		this.actorName = actorName;
		this.actorState = actorState;
	}
	
	public void setActorName(String actorName) {
		this.actorName = actorName;
	}
	
	public String getActorName() {
		return actorName;
	}
	
	public void setActorState(ActorState actorState) {
		this.actorState = actorState;
	}
	
	public ActorState getActorState() {
		return actorState;
	}

}
