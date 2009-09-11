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
 * A specific port that allows writing to a channel.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface WritePort<T> extends Port<T> {

	/**
	 * <p>
	 * Write a single value to the channel. The write operation exhibits the 
	 * following blocking behavior:
	 * </p>
	 * <ul>
	 *   <li>If the channel is unbuffered the caller is blocked until the reader has read
	 *       this value successfully from the channel.</li>
	 *   <li>If the channel is buffered and not at capacity, the value is added to the buffer
	 *       and the caller returns immediately.
	 *   <li>If the channel is buffered and at capacity, the caller is blocked until there 
	 *       is free capacity in the buffer, the value is added to the buffer and the caller
	 *       then returns.
	 * </ul>
	 * 
	 * @param value the value to write to the channel.
	 * 
	 * @throws IllegalStateException if this port is closed.
	 * @throws ChannelException if the channel is unable to complete the read request.
	 */
	public void write(T value) throws IllegalStateException, ChannelException;
}
