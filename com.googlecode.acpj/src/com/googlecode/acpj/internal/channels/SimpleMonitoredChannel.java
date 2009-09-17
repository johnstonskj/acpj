/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import java.util.Iterator;

import com.googlecode.acpj.channels.ReadPort;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * Internal - allows {@link SimpleChannel} to be monitored.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
interface SimpleMonitoredChannel<T> {

	/**
	 * Retrieve all the read ports owned by this channel.
	 * @return
	 */
	public Iterator<ReadPort<T>> getReadPorts();
	
	/**
	 * Retrieve all the write ports owned by this channel.
	 * @return
	 */
	public Iterator<WritePort<T>> getWritePorts();
}
