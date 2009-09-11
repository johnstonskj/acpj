/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelRegistry;

/**
 * <p>
 * Internal - default implementation of the {@link com.googlecode.acpj.channels.ChannelRegistry} 
 * interface.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class DefaultChannelRegistry extends ChannelRegistry {
	
	private static ConcurrentHashMap<String, Channel<?>> registry = new ConcurrentHashMap<String, Channel<?>>();

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.ChannelRegistry#deregister(java.lang.String)
	 */
	public boolean deregister(String publicName) {
		return (registry.remove(publicName) != null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.ChannelRegistry#register(com.googlecode.acpj.channels.Channel, java.lang.String, boolean)
	 */
	public boolean register(Channel<?> channel, String publicName, boolean remotable) {
		return (registry.put(publicName, channel) == null);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.ChannelRegistry#lookup(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> Channel<T> lookup(String publicName) throws IllegalArgumentException, UnknownError {
		Channel<T> channel = (Channel<T>) lookupOrNull(publicName);
		if (channel == null) {
			throw new UnknownError(String.format("No registry with key '%s'", publicName));
		}
		return channel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.ChannelRegistry#lookupOrNull(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> Channel<T> lookupOrNull(String publicName) throws IllegalArgumentException {
		if (publicName == null) {
			throw new IllegalArgumentException("Channel name must not be null");
		}
		return (Channel<T>) registry.get(publicName);
	}
}
