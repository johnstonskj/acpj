/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

import com.googlecode.acpj.internal.config.Configuration;

/**
 * <p>
 * This is a singleton class that maintains a shared registry mapping
 * instances of {@link Channel channels} to well-known string names. This allows
 * a service to publish its channels and allow clients to be able to discover
 * the channel without having to negotiate in any way with the service. 
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public abstract class ChannelRegistry {

	private static ChannelRegistry instance = null;

	/**
	 * Retrieve the static (singleton) instance of the ChannelRegistry. Note that 
	 * the particular implementation of this class used at runtime can be
	 * changed using the system property described by 
	 * {@link com.googlecode.acpj.Arguments#CFG_DEFAULT_CHANNEL_REGISTRY_CLASS}.
	 * 
	 * @return the singleton ChannelRegistry instance.
	 */
	public static synchronized ChannelRegistry getInstance() {
		if (instance == null) {
			Class<? extends ChannelRegistry> factory = Configuration.getDefaultChannelRegistryImpl();
			try {
				instance = factory.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	/**
	 * Register the channel with the associated publicName. It is recommended that 
	 * either a Java package-like naming scheme be used or URIs be generated as
	 * names to ensure uniqueness.
	 * 
	 * @param channel the channel instance to register.
	 * @param publicName the public name clients will use to discover the channel.
	 * @param remotable whether this entry is visible to remote actors (in other process spaces).
	 * 
	 * @return <code>true</code> if the channel was successfully added.
	 */
	public abstract boolean register(Channel<?> channel, String publicName, boolean remotable);
	
	/**
	 * Deregister the channel, remove the entry associated with this publc name.
	 * 
	 * @param publicName the name to deregister.
	 * 
	 * @return <code>true</code> if the channel was successfully removed.
	 */
	public abstract boolean deregister(String publicName);

	/**
	 * Find the channel associated with the public name, this assumes the channel
	 * has been previously registered.
	 * 
	 * @param publicName the name of the channel to find.
	 * 
	 * @return the channel instance.
	 * 
	 * @throws IllegalArgumentException 
	 * @throws UnknownError the public name has not been registered.
	 */
	public abstract Channel<?> lookup(String publicName) throws IllegalArgumentException, UnknownError;
	
	/**
	 * Find the channel associated with the public name, this assumes the channel
	 * has been previously registered.
	 * 
	 * @param publicName the name of the channel to find.
	 * 
	 * @return the channel instance, or <code>null</code> if not found.
	 * 
	 * @throws IllegalArgumentException 
	 */
	public abstract Channel<?> lookupOrNull(String publicName) throws IllegalArgumentException;
}
