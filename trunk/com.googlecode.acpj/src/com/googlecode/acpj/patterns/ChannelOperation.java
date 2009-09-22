/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.patterns;

/**
 * <p>
 * This denotes an operation that processes one message and returns another
 * message, or <code>null</code>. This can be seen as the abstraction of a 
 * number of operations we may wish to do on channels:
 * </p>
 * <ul>
 *   <li>Filtering - the operation takes a message and returns a boolean value
 *       as to whether the message should be passed on or not.</li>
 *   <li>Filtering - alternatively the operation can either return the original
 *       message (denotes acceptance) or <code>null</code> to reject a message.</li>
 *   <li>Transformation - the operation can transform the message from its inbound
 *       form into an entirely different type and return this to be passed on down
 *       the outbound channel.</li>
 * </ul>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface ChannelOperation<Tin, Tout> {

	public Tout process(Tin message);
}
