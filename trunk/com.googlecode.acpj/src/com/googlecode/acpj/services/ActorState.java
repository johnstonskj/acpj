/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.services;

/**
 * <p>
 * The state of an actor, included in the {@link ActorStateMessage}, sent to 
 * the {@link WatchdogService}. 
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public enum ActorState {
	STARTED,
	FINISHED,
	DIED
}
