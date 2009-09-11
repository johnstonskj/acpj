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
 * Denotes an attempt to use a channel that has been poisoned.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ChannelPoisonedException extends ChannelException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new exception with a default "channel has been poisoned" message.
	 */
	public ChannelPoisonedException() {
		super("Channel has been poisoned.");
	}

	/**
	 * Construct a new exception with the provided message.
	 * 
	 * @param message a text message to associate with this exception occurrence.
	 */
	public ChannelPoisonedException(String message) {
		super(message);
	}
}
