/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.actors;

/**
 * <p>
 * An actor is a very simple abstraction for a separate thread of execution,
 * it's implementation may be as a single thread, as a lighter-weight function
 * using a thread pool or even as co-operating system processes. The key is 
 * that none of these implementations are allowed to leak out into the API.
 * </p>
 * <p>
 * The basic characteristics of an Actor are:
 * </p>
 * <ul>
 *   <li>It starts on creation and runs to completion.</li>
 *   <li>It has a integer (<code>long</code>) unique identifier.</li>
 *   <li>Allows a name to be associated with it.</li>
 * </ul>
 * <p>
 * Specifically, unlike Java threads you cannot start, stop or interrupt a 
 * thread explicitly and you can only query whether the particular actor is
 * still running.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface Actor {

	/**
	 * Retrieve the unique numeric identifier for this actor instance.
	 *
	 * @return a unique numeric identifier for this actor.
	 */
	public long getLocalId();
	
	/**
	 * Retrieve the unique name, formatted as a URI, for this actor instance. The
	 * specific format is: <code>actor:/{local-name}/{local-id}</code>
	 * where the local name is assigned when the actor is created by
	 * the {@link ActorFactory}.
	 * 
	 * @return a URI formatted, but String representation, of the actors name.
	 */
	public String getName();
	
	/**
	 * Return whether or not this instance is still running.
	 * 
	 * @return <code>true</code> if this instance is still running, or <code>false</code>.
	 */
	public boolean isRunning();
}
