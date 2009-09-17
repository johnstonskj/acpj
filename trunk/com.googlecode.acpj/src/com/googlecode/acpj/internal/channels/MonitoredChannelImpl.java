/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.channels.Port;
import com.googlecode.acpj.channels.PortArity;
import com.googlecode.acpj.channels.monitor.MonitoredChannel;
import com.googlecode.acpj.channels.monitor.MonitoredPort;

/**
 * <p>
 * Internal = implementation of {@link com.googlecode.acpj.channels.monitor.MonitoredChannel}
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
class MonitoredChannelImpl implements MonitoredChannel {
	
	private SimpleChannel<?> actualChannel = null;
	
	private class MonitoredPortImpl implements MonitoredPort {
		private Port<?> actualPort = null;
		public MonitoredPortImpl(Port<?> actual) {
			this.actualPort = actual;
		}
		public Actor getOwningActor() {
			return actualPort.getOwningActor();
		}
		public boolean isClosed() {
			return actualPort.isClosed();
		}
	}
	
	private class MonitoredPortIterator implements Iterator<MonitoredPort> {
		private Iterator<MonitoredPort> actual = null;
		public MonitoredPortIterator(Iterator<MonitoredPort> actual) {
			this.actual = actual;
		}
		public boolean hasNext() {
			return actual.hasNext();
		}
		public MonitoredPort next() {
			return actual.next();
		}
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public MonitoredChannelImpl(SimpleChannel<?> actual) {
		this.actualChannel = actual;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#getBufferCapacity()
	 */
	public int getBufferCapacity() {
		if (isBuffered()) {
			return actualChannel.getBufferCapacity();
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#getName()
	 */
	public String getName() {
		return actualChannel.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#getReadPortArity()
	 */
	public PortArity getReadPortArity() {
		return actualChannel.getReadPortArity();
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#getReadPorts()
	 */
	public Iterator<MonitoredPort> getReadPorts() {
		Set<MonitoredPort> ports = new HashSet<MonitoredPort>();
		Iterator<?> readPorts = actualChannel.getReadPorts();
		while (readPorts.hasNext()) {
			ports.add(new MonitoredPortImpl((Port<?>)readPorts.next()));
		}
		return new MonitoredPortIterator(ports.iterator());
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#getWritePortArity()
	 */
	public PortArity getWritePortArity() {
		return actualChannel.getWritePortArity();
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#getWritePorts()
	 */
	public Iterator<MonitoredPort> getWritePorts() {
		Set<MonitoredPort> ports = new HashSet<MonitoredPort>();
		Iterator<?> writePorts = actualChannel.getWritePorts();
		while (writePorts.hasNext()) {
			ports.add(new MonitoredPortImpl((Port<?>)writePorts.next()));
		}
		return new MonitoredPortIterator(ports.iterator());
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#isBuffered()
	 */
	public boolean isBuffered() {
		return actualChannel.getBufferCapacity() != 0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#size()
	 */
	public int size() {
		return actualChannel.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.monitor.MonitoredChannel#isPoisoned()
	 */
	public boolean isPoisoned() {
		return actualChannel.isPoisoned();
	}

}
