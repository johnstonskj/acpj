/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.channels.ChannelException;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * Internal - implmementation of the {@link com.googlecode.acpj.channels.WritePort} interface.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class SimpleWritePort<T> extends SimplePort<T> implements WritePort<T> {

	public SimpleWritePort(SimpleChannel<T> channel, int limit, Actor owner) {
		super(channel, limit, owner);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.WritePort#write(java.lang.Object)
	 */
	public synchronized void write(T value) throws IllegalStateException, ChannelException {
		if (isClosed()) {
			throw new IllegalStateException("Port is closed.");
		}
		if (!isMine()) {
			throw new ChannelException("Current actor does not own this port.");
		}
		if (getLimit() != ChannelFactory.PORT_LIMIT_UNLIMITED) {
			if (getLimit() == PORT_LIMIT_EXCEEDED) {
				throw new IllegalStateException("Read port limit exceeded.");
			}
			decrementLimit();
		}
		if (value instanceof SimplePort<?>) {
			((SimplePort<?>)value).release();
		}
		this.channel.writeValue(value);
	}
}
