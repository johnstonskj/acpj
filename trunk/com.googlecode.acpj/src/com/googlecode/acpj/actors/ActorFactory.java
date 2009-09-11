/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.actors;

import com.googlecode.acpj.internal.config.Configuration;

/**
 * <p>
 * A singleton factory class that provides a mechanism to disconnect the
 * definition of an {@link Actor} from the particular implementation used
 * to allow it to run concurrently. This class is thus a facade and relies
 * on another implementation of itself that provides the actual implementation.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public abstract class ActorFactory {
	
	private static ActorFactory instance = null;

	/**
	 * Retrieve the static (singleton) instance of the ActorFactory. Note that 
	 * the particular implementation of this class used at runtime can be
	 * changed using the system property described by 
	 * {@link com.googlecode.acpj.Arguments#CFG_DEFAULT_ACTOR_FACTORY_CLASS}.
	 * 
	 * @return the singleton ActorFactory instance.
	 */
	public static synchronized ActorFactory getInstance() {
		if (instance == null) {
			Class<? extends ActorFactory> factory = Configuration.getDefaultActorFactoryImpl();
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
	 * Create a new actor instance (note that actors are created in an already
	 * running state).
	 * 
	 * @param runnable an object implementing the {@link java.lang.Runnable} interface
	 *        to be the new actor function.
	 * @return a new actor instance, or <code>null</code> if the actor could not be
	 *        created.
	 */
	public abstract Actor createActor(Runnable runnable);

	/**
	 * Create a new actor instance (note that actors are created in an already
	 * running state).
	 * 
	 * @param runnable an object implementing the {@link java.lang.Runnable} interface
	 *        to be the new actor function.
	 * @param name a name to assign to the new actor instance, note that this name need
	 *        not be unique, it is in general only used for debugging.
	 * @return a new actor instance, or <code>null</code> if the actor could not be
	 *        created.
	 */
	public abstract Actor createActor(Runnable runnable, String name);

	/**
	 * Retrieve the instance of the actor that represents the caller, note that in the
	 * case of threads that are running but not created as actors a single, shared
	 * actor instance is used. 
	 * 
	 * @return an actor instance representing the caller.
	 */
	public abstract Actor getCurrentActor();
	
}
