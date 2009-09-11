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
 * An extension of the standard {@link Channel} interface to support the buffering
 * of multiple messages to be read.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface BufferedChannel<T> extends Channel<T> {
	
	/**
	 * Denotes an unlimited (implementations may place a physical upper bound)
	 * buffer capacity.
	 */
	public static final int BUFFER_CAPACITY_UNLIMITED = -1;
	
	/**
	 * Retrieve the overall capacity of this channel, this was set when the channel
	 * was created.
	 * 
	 * @return the overall capacity of this channel.
	 */
	public int getBufferCapacity();
	
	/**
	 * The size of the channel buffer at present, this is the number of messages
	 * waiting to be read.
	 * 
	 * @return the number of buffered messages.
	 */
	public int size();

}
