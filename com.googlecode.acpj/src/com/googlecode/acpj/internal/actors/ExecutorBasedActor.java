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
 * use with Executors.
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
		setLocalId(-Thread.currentThread().getId());
		this.name = Thread.currentThread().getName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.actors.Actor#getLocalId()
	 */
	public long getLocalId() {
		return this.id;
	}

	public void setLocalId(long id) {
		this.id = id;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.actors.Actor#getName()
	 */
	public String getName() {
		if (this.name == null) {
			return String.format("actor:/%d", getLocalId());
		} else {
			return String.format("actor:/%s/%d", this.name, getLocalId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.actors.Actor#isRunning()
	 */
	public boolean isRunning() {
		return this.running;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		try {
			this.running = true;
			new WatchableActor(this, this.actual).run();
			this.running = false;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (int)getLocalId();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ExecutorBasedActor)) {
			return false;
		}
		return getLocalId() == ((ExecutorBasedActor)obj).getLocalId();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}
}
