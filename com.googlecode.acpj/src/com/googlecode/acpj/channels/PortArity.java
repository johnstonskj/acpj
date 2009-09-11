/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

/**
 * <p>
 * Denotes the number of Ports allowed to be connected to either
 * the read or write end of a channel.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public enum PortArity {
	
	/**
	 * Only one individual port is allowed on this end of the channel at any 
	 * one time.
	 */
	ONE,
	
	/**
	 * Many ports are allowed on this end of the channel at any one time.
	 */
	ANY
}
