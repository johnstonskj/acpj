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
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ExecutorBasedActorFactory extends ActorFactory {

	public Actor createActor(Runnable runnable) {
		return new ExecutorBasedActor(runnable);
	}

	public Actor createActor(Runnable runnable, String name) {
		return new ExecutorBasedActor(runnable, name);
	}

	public Actor getCurrentActor() {
		return ActorPoolExecutor.getCurrentActor();
	}
}
