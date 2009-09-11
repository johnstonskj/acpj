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
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class DefaultChannelRegistry extends ChannelRegistry {
	
	private static ConcurrentHashMap<String, Channel<?>> registry = new ConcurrentHashMap<String, Channel<?>>();

	public boolean deregister(String publicName) {
		return (registry.remove(publicName) != null);
	}

	public boolean register(Channel<?> channel, String publicName, boolean remotable) {
		return (registry.put(publicName, channel) == null);
	}

	public Channel<?> lookup(String publicName) throws IllegalArgumentException, UnknownError {
		Channel<?> channel = lookupOrNull(publicName);
		if (channel == null) {
			throw new UnknownError(String.format("No registry with key '%s'", publicName));
		}
		return channel;
	}
	
	public Channel<?> lookupOrNull(String publicName) throws IllegalArgumentException {
		if (publicName == null) {
			throw new IllegalArgumentException("Channel name must not be null");
		}
		return registry.get(publicName);
	}
}
