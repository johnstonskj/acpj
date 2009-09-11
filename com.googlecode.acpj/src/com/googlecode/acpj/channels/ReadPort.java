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
 * A specific port that allows reading from a channel.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface ReadPort<T> extends Port<T> {

	/**
	 * <p>
	 * Read and return a single value from the channel. The read operation exhibits the 
	 * following blocking behavior:
	 * </p>
	 * <ul>
	 *   <li>If the channel is unbuffered the caller is blocked until a value is 
	 *   available to read, if there is an outstanding write operation then the value
	 *   is taken from the writer and both parties are unblocked..</li>
	 *   <li>If the channel is buffered and has values buffered, the next available value
	 *       is read and returned immediately.
	 *   <li>If the channel is buffered and currently empty, the caller is blocked until there 
	 *       some value has been added to the buffer, at which time the next available value
	 *       is read and returned immediately.
	 * </ul>
	 * 
	 * @return the next available value from the channel.
	 * 
	 * @throws IllegalStateException if this port is closed.
	 * @throws ChannelException if the channel is unable to complete the read request.
	 */
	public T read() throws IllegalStateException, ChannelException;
}
