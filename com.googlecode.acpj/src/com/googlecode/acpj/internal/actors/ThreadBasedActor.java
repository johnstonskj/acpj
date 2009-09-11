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

/**
 * <p>
 * Internal - implementation of {@link com.googlecode.acpj.actors.Actor} for
 * use with standard Java Threads.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ThreadBasedActor implements Actor {

	Thread thread = null;
	
	public ThreadBasedActor(Runnable actual) {
		if (actual == null) {
			throw new IllegalArgumentException("Runnable may not be null");
		}
		this.thread = new Thread(new WatchableActor(this, actual));
		this.thread.setName(String.format("actor:/%d", this.thread.getId()));
		this.thread.start();
	}
	
	public ThreadBasedActor(Runnable actual, String name) {
		if (actual == null) {
			throw new IllegalArgumentException("Runnable may not be null");
		}
		if (name == null) {
			throw new IllegalArgumentException("Runnable name may not be null");
		}
		this.thread = new Thread(new WatchableActor(this, actual));
		this.thread.setName(String.format("actor:/%s/%d", name, this.thread.getId()));
		this.thread.start();
		/*
		 * Gives the new thread a chance to get going.
		 */
		Thread.yield();
	}
	
	public ThreadBasedActor() {
		this.thread = Thread.currentThread();
	}
	
	public long getLocalId() {
		return this.thread.getId();
	}
	
	public String getName() {
		return this.thread.getName();
	}

	public boolean isRunning() {
		return this.thread.isAlive();
	}
	
	@Override
	public int hashCode() {
		return this.thread.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ThreadBasedActor)) {
			return false;
		}
		return this.thread.equals(((ThreadBasedActor)obj).thread);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
