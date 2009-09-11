/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

import com.googlecode.acpj.actors.Actor;

/**
 * <p>
 * A Port is an abstraction representing one end of a channel, either the read
 * or the write end. Channels are uni-directional so a port may only read from
 * or write to a channel. This interface defines the common methods for both 
 * {@link ReadPort} and {@link WritePort}.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface Port<T> extends Poisonable {
	
	/**
	 * Return the actor that currently owns this port, a port cannot be shared
	 * by more than one actor without an explicit transfer of ownership. When a
	 * port is released the owning actor is set to <code>null</code>.
	 *  
	 * @return an Actor instance representing the port's current owner, or 
	 *         <code>null</code> if the port is not owned.
	 */
	public Actor getOwningActor();
	
	/**
	 * Claim this port on behalf of the calling Actor, this is the explicit
	 * claim of ownership.
	 * 
	 * @throws ChannelException if the port is already claimed.
	 */
	public void claim() throws ChannelException;
	
	/**
	 * Release ownership of this port on behalf of the calling Actor, this is
	 * the explicit relinquishing of ownership.
	 * 
	 * @throws ChannelException if the calling actor does not currently own the port.
	 */
	public void release() throws ChannelException;
	
	/**
	 * Close this port and disconnect from this channel. This is important for
	 * ports with arity ONE, this allows another actor to now get and claim a 
	 * port for themselves.
	 */
	public void close();
	
	/**
	 * Determines whether this port is closed.
	 * 
	 * @return <code>true</code> if this port is closed.
	 */
	public boolean isClosed();
}
