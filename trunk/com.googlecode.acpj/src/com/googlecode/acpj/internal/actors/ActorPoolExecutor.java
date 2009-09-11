/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.actors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.acpj.actors.Actor;

/**
 * <p>
 * Internal - an extended form of the standard Java 
 * {@link java.util.concurrent.ThreadPoolExecutor} which sets up the Thread-
 * Local Storage correctly to support a <code>getCurrentActor</code> call 
 * in the same way for Executor-started threads.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ActorPoolExecutor extends ThreadPoolExecutor {

	private static AtomicLong serialNumberGenerator = new AtomicLong(0);
    private static ThreadLocal<Actor> currentActor = new ThreadLocal<Actor>();
    
    public ActorPoolExecutor(int poolSize) {
		this(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	}

	protected ActorPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#beforeExecute(java.lang.Thread, java.lang.Runnable)
	 */
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		try {
			ExecutorBasedActor actor = (ExecutorBasedActor)r;
			actor.setLocalId(serialNumberGenerator.getAndIncrement());
	    	currentActor.set(actor);
		} catch (Throwable tx) {
			tx.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable, java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		try {
			currentActor.remove();
		} catch (Throwable tx) {
			tx.printStackTrace();
		}
	}

    public static Actor getCurrentActor() {
    	Actor current = currentActor.get();
    	return current == null ? new ExecutorBasedActor() : current;
    }
}
