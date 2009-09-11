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
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ExecutorBasedActor implements Actor, Runnable {
	
	private static ActorPoolExecutor threadExecutor;
	
	static {
		int processors = Runtime.getRuntime().availableProcessors();
		threadExecutor = new ActorPoolExecutor(processors * 8);
	}
	
	private long id = -1;
	private String name = null;
	private boolean running = false;
	private Runnable actual = null;
	
	public ExecutorBasedActor(Runnable actual) {
		if (actual == null) {
			throw new IllegalArgumentException("Runnable may not be null");
		}
		this.actual = actual;
		threadExecutor.execute(this);
	}
	
	public ExecutorBasedActor(Runnable actual, String name) {
		if (actual == null) {
			throw new IllegalArgumentException("Runnable may not be null");
		}
		if (name == null) {
			throw new IllegalArgumentException("Runnable name may not be null");
		}
		this.name = name;
		this.actual = actual;
		threadExecutor.execute(this);
	}
	
	public ExecutorBasedActor() {
		id = -Thread.currentThread().getId();
		name = Thread.currentThread().getName();
	}
	
	public long getLocalId() {
		return this.id;
	}

	public void setLocalId(long id) {
		this.id = id;
	}
	
	public String getName() {
		if (this.name == null) {
			return String.format("actor:/%d", getLocalId());
		} else {
			return String.format("actor:/%s/%d", this.name, getLocalId());
		}
	}

	public boolean isRunning() {
		return this.running;
	}
	
	public void run() {
//		System.out.println("About to run " + getName());
		try {
			this.running = true;
			new WatchableActor(this, this.actual).run();
			this.running = false;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		return (int)getLocalId();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ExecutorBasedActor)) {
			return false;
		}
		return getLocalId() == ((ExecutorBasedActor)obj).getLocalId();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
