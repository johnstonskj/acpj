/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

import com.googlecode.acpj.actors.Actor;

/**
 * <p>
 * This interface provides a read-only view of the relevant properties of the
 * {@link com.googlecode.acpj.channels.Port} interface.
 * </p>
 * <p>
 * This interface is not related to the standard 
 * {@link com.googlecode.acpj.channels.Port} interface by type, this
 * stops the channel monitor being used to bypass channel/port security.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface MonitoredPort  {
	
	/**
	 * Safe proxy for the method {@link com.googlecode.acpj.channels.Port#getOwningActor()} 
	 * 
	 */
	public Actor getOwningActor();
	
	/**
	 * Safe proxy for the method {@link com.googlecode.acpj.channels.Port#isClosed()}
	 */
	public boolean isClosed();
}
