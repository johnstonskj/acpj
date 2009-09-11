/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

/**
 * <p>
 * This interface represents the core concept of a channel, it is a generic class 
 * and so can be typed according to the messages it carries. By default a
 * channel is not buffered, this means that when one actor writes to the channel
 * they do not return until a reader has fully read the value being written; 
 * conversely a reader will be blocked indefinitely if no value has been written
 * to the channel.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface Channel<T> extends Poisonable {

	/**
	 * Retrieve the unique numeric identifier for this channel instance.
	 *
	 * @return a unique numeric identifier for this channel.
	 */
	public long getLocalId();
	
	/**
	 * Retrieve the unique name, formatted as a URI, for this channel instance. The
	 * specific format is: <code>channel:/{local-name}/{local-id}</code>
	 * where the local name is assigned when the channel is created by
	 * the {@link ChannelFactory}.
	 * 
	 * @return a URI formatted, but String representation, of the channels name.
	 */
	public String getName();
	
	/**
	 * Retrieve a new port with read-access rights to this channel. 
	 * 
	 * @param claimed denotes whether the new port is created already owned by the
	 *        calling actor.
	 *        
	 * @return a new Port instance.
	 * 
	 * @throws IllegalStateException
	 * @throws ChannelException
	 */
	public ReadPort<T> getReadPort(boolean claimed) throws IllegalStateException, ChannelException;
	
	/**
	 * Retrieve the port-arity associated with the read end of this channel.
	 * 
	 * @return the port-arity declared when this channel was created. 
	 */
	public PortArity getReadPortArity();

	/**
	 * Retrieve a new port with write-access rights to this channel. 
	 * 
	 * @param claimed denotes whether the new port is created already owned by the
	 *        calling actor.
	 *        
	 * @return a new Port instance.
	 * 
	 * @throws IllegalStateException
	 * @throws ChannelException
	 */
	public WritePort<T> getWritePort(boolean claimed) throws IllegalStateException, ChannelException;

	/**
	 * Retrieve the port-arity associated with the write end of this channel.
	 * 
	 * @return the port-arity declared when this channel was created. 
	 */
	public PortArity getWritePortArity();

}
