/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj;

/**
 * <p>
 * Provides the identifiers for VM arguments that can be used to override the 
 * default configuration of the library.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class Arguments {

	/**
	 * <p>
	 *   This is the qualified name of a class that will act as the implementation 
	 *   of the {@link com.googlecode.acpj.channels.ChannelFactory} interface and
	 *   be used to create new {@link com.googlecode.acpj.channels.Channel} instances.
	 * </p>
	 */
	public static final String CFG_DEFAULT_CHANNEL_FACTORY_CLASS = "com.googlecode.acpj.config.DefaultChannelFactory";

	/**
	 * <p>
	 *   This is the qualified name of a class that will act as the implementation 
	 *   of the {@link com.googlecode.acpj.channels.ChannelRegistry} interface and
	 *   be used to manage a name to channel mapping for actors to discover well-
	 *   known channels.
	 * </p>
	 */
	public static final String CFG_DEFAULT_CHANNEL_REGISTRY_CLASS = "com.googlecode.acpj.config.DefaultRegistryFactory";

	/**
	 * <p>
	 *   This is the qualified name of a class that will act as the implementation 
	 *   of the {@link com.googlecode.acpj.actors.ActorFactory} interface and
	 *   be used to create new {@link com.googlecode.acpj.actors.Actor} instances.
	 * </p>
	 * <p>
	 *   Currently the following two values may be used:
	 * </p>
	 * <dl>
	 *   <dt><code>com.googlecode.acpj.internal.actors.ExecutorBasedActorFactory</code></dt>
	 *   <dd>Uses a {@link java.util.concurrent.ThreadPoolExecutor} to manage each actor
	 *       within a pool of threads for better performance (the default value).</dd>
	 *   <dt><code>com.googlecode.acpj.internal.actors.ThreadBasedActorFactory</code></dt>
	 *   <dd>Creates each actor on it's own {@link java.lang.Thread} instance.</dd>
	 * </dl>
	 */
	public static final String CFG_DEFAULT_ACTOR_FACTORY_CLASS = "com.googlecode.acpj.config.DefaultActorFactory";

	/**
	 * <p>
	 *   This is an integer value that denotes the number of the thread pool to use when 
	 *   using a {@link java.util.concurrent.ThreadPoolExecutor} based actor factory. The
	 *   default value is some multiple of the number of CPUs reported for the current
	 *   machine.
	 * </p>
	 */
	public static final String CFG_THREAD_POOL_SIZE = "com.googlecode.acpj.config.ThreadPoolSize";

	/**
	 * <p>
	 *   This is a Boolean value that determines whether the {@link com.googlecode.acpj.channels.ChannelFactory}
	 *   should attempt to monitor all channels.
	 * </p>
	 */
	public static final String CFG_MONITOR_CHANNELS = "com.googlecode.acpj.config.MonitorChannels";
}

