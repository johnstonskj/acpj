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
 * Generally denotes an exception or error occurred during the creation
 * of a new {@link com.googlecode.acpj.actors.Actor} instance.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ActorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a new exception with the provided message.
	 * 
	 * @param message a text message to associate with this exception occurrence.
	 */
	public ActorException(String message) {
		super(message);
	}

	/**
	 * Construct a new exception denoting the provided exception as the cause.
	 * 
	 * @param cause a exception deemed to be the cause of this exception occurrence.
	 */
	public ActorException(Throwable cause) {
		super(cause);
	}

	/**
	 * Construct a new exception with both message and cause.
	 * 
	 * @param message a text message to associate with this exception occurrence.
	 * @param cause a exception deemed to be the cause of this exception occurrence.
	 */
	public ActorException(String message, Throwable cause) {
		super(message, cause);
	}
}
