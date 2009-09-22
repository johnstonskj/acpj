/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.patterns;

import com.googlecode.acpj.actors.ActorException;
import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.channels.ChannelException;

/**
 * <p>
 * A Join Pool is a collection of Actors that can be collected together and
 * another Actor can wait for one or all of the set to complete. This is 
 * commonly used where one Actor creates a set of workers and doesn't need 
 * to worry about data coming back from them but does care to know when 
 * the workers are completed. 
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface ActorJoinPool {
	
	/**
	 * Create and add the provided {@link java.lang.Runnable} as an 
	 * {@link com.googlecode.acpj.actors.Actor} in the join pool.
	 * 
	 * @param runnable the {@link java.lang.Runnable} that will be used to create 
	 *        an {@link com.googlecode.acpj.actors.Actor} in pool.
	 * 
	 * @throws IllegalStateException you cannot add 
	 *         {@link com.googlecode.acpj.actors.Actor}s after a pool has joined.
	 * @throws ActorException an error was caused by the 
	 *          {@link com.googlecode.acpj.actors.Actor}.
	 */
	public void createActor(Runnable runnable) throws IllegalStateException, ActorException;

	/**
	 * Create and add the provided {@link java.lang.Runnable} as an 
	 * {@link com.googlecode.acpj.actors.Actor} in the join pool.
	 * 
	 * @param runnable the {@link java.lang.Runnable} that will be used to create an 
	 *        {@link com.googlecode.acpj.actors.Actor} in pool.
	 * @param factory the {@link com.googlecode.acpj.actors.ActorFactory} that will be 
	 *        used to create the new {@link com.googlecode.acpj.actors.Actor}.
	 * 
	 * @throws IllegalStateException you cannot add 
	 *         {@link com.googlecode.acpj.actors.Actor}s after a pool has joined.
	 * @throws ActorException an error was caused by the {@link com.googlecode.acpj.actors.Actor}.
	 */
	public void createActor(Runnable runnable, ActorFactory factory) throws IllegalStateException, ActorException;

	/**
	 * Wait for <em>at least</em> one of the {@link com.googlecode.acpj.actors.Actor}s 
	 * in the join pool to complete. Note that we guarantee at least one, but this could 
	 * imply any number between 1..n where n is the number of 
	 * {@link com.googlecode.acpj.actors.Actor}s in the pool.
	 * 
	 * @throws IllegalStateException you cannot join a pool previously joined
	 * @throws ChannelException an error occured communicating with the 
	 *         {@link com.googlecode.acpj.actors.Actor}s in the pool.
	 */
	public void joinAny() throws IllegalStateException, ChannelException;

	/**
	 * Wait for <em>all</em> of the Actors in the join pool to complete.
	 * 
	 * @throws IllegalStateException you cannot join a pool previously joined
	 * @throws ChannelException an error occured communicating with the Actors in the pool.
	 */
	public void joinAll() throws IllegalStateException, ChannelException;
}
