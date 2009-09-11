/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.actors;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.actors.ActorFactory;

/**
 * <p>
 * Internal - implementation of {@link com.googlecode.acpj.actors.ActorFactory} for
 * use with standard Java Threads.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ThreadBasedActorFactory extends ActorFactory {

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.actors.ActorFactory#createActor(java.lang.Runnable)
	 */
	public Actor createActor(Runnable runnable) {
		return new ThreadBasedActor(runnable);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.actors.ActorFactory#createActor(java.lang.Runnable, java.lang.String)
	 */
	public Actor createActor(Runnable runnable, String name) {
		return new ThreadBasedActor(runnable, name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.actors.ActorFactory#getCurrentActor()
	 */
	public Actor getCurrentActor() {
		return new ThreadBasedActor();
	}
}
