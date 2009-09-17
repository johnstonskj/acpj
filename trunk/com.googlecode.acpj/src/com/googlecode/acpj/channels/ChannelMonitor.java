/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

import java.util.Iterator;

/**
 * <p>
 * Interface to retrieve monitor all the channels current live in the system.
 * Currently the monitor is retrieved from the 
 * {@link com.googlecode.acpj.channels.ChannelFactory} as the channel factory then
 * provides the hooks for monitoring the creation of all channels.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public interface ChannelMonitor {
	
	/**
	 * Returned views of all the system channels. Note that the interface 
	 * {@link MonitoredChannel} is not related to the standard 
	 * {@link com.googlecode.acpj.channels.Channel} interface by type, this
	 * stops the channel monitor being used to bypass channel/port security.
	 * 
	 * @return an iterator over all the currently live channels.
	 */
	public Iterator<MonitoredChannel> getChannels(); 
}
