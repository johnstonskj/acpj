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
 * This interface denotes that the Channel component supports the notion of
 * being poisoned - that is the overall channel and all ports are immediately
 * unusable, any buffered messages are non-retrievable and any access to the
 * channel will result in a {@link ChannelPoisonedException} being thrown. 
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface Poisonable {

	/**
	 * Poison the channel, render it unusable.
	 * 
	 * @throws IllegalStateException the channel component is not in a state
	 *         where it can accept the request to poison the overall channel.
	 * @throws ChannelException
	 */
	public void poison() throws IllegalStateException, ChannelException;
	
	/**
	 * Determine whether the channel is in a poisoned state.
	 * 
	 * @return <code>true</code> if the channel has been poisoned.
	 * 
	 * @throws IllegalStateException
	 */
	public boolean isPoisoned() throws IllegalStateException;
}
