/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.channels.BufferedChannel;
import com.googlecode.acpj.channels.ChannelException;
import com.googlecode.acpj.channels.ChannelPoisonedException;
import com.googlecode.acpj.channels.Port;
import com.googlecode.acpj.channels.PortArity;
import com.googlecode.acpj.channels.ReadPort;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * Internal - Common implementation for both the 
 * {@link com.googlecode.acpj.channels.BufferedChannel} and
 * {@link com.googlecode.acpj.channels.Channel} interfaces.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class SimpleChannel<T> implements BufferedChannel<T> {
	
	class Item<V> {
		/*
		 * Allows the data value to be null, otherwise the BlockingQueue objects.
		 */
		V data = null;
	}

	private static AtomicLong serialNumberGenerator = new AtomicLong(0);

	private long id = serialNumberGenerator.getAndIncrement();
	private String name = null;
	
	private int capacity = 1;
	private BlockingQueue<Item<T>> values = null;
	
	private PortArity readArity = null;
	private int readPortLimit = 0;
	private List<ReadPort<T>> readPorts = null; 
	
	private PortArity writeArity = null;
	private int writePortLimit = 0;
	private List<WritePort<T>> writePorts = null;
	
	private boolean poisoned = false;
	
	public SimpleChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit, int capacity) {
		if (name == null) {
			this.name = String.format("channel:/%d", this.id);
		} else {
			this.name = String.format("channel:/%s/%d", name, this.id);
		}
	
		if (readPortArity == null) {
			throw new IllegalArgumentException("Read port arity may not be null.");
		}
		this.readArity = readPortArity;
		if (readPortLimit == 0) {
			throw new IllegalArgumentException("Read port limit may not be zero.");
		}
		this.readPortLimit = readPortLimit;
		this.readPorts = new ArrayList<ReadPort<T>>(this.readArity == PortArity.ONE ? 1 : 10);

		if (writePortArity == null) {
			throw new IllegalArgumentException("Write port arity may not be null.");
		}
		this.writeArity = writePortArity;
		if (writePortLimit == 0) {
			throw new IllegalArgumentException("Write port limit may not be zero.");
		}
		this.writePortLimit = writePortLimit;
		writePorts = new ArrayList<WritePort<T>>(this.writeArity == PortArity.ONE ? 1 : 10);

		if (capacity == 0) {
			values = new ZeroBlockingQueue<Item<T>>();
		} else {
			values = new LinkedBlockingQueue<Item<T>>(capacity == BUFFER_CAPACITY_UNLIMITED ? Integer.MAX_VALUE : capacity);
		}
	}

	public T readValue() throws IllegalStateException, ChannelException {
		if (isPoisoned()) {
			throw new ChannelPoisonedException();
		}
		T value = null;
		try {
			Item<T> temp = values.take();
			value = temp.data;
		} catch (InterruptedException e) {
			throw new ChannelException(e);
		}
		if (isPoisoned()) {
			throw new ChannelPoisonedException();
		}
		return value;
	}
	
	public void writeValue(T value) throws IllegalStateException, ChannelException {
		if (isPoisoned()) {
			throw new ChannelPoisonedException();
		}
		if (value == null) {
			throw new IllegalArgumentException("Value may not be null.");
		}
		try {
			Item<T> temp = new Item<T>();
			temp.data = value;
			values.put(temp);
		} catch (InterruptedException e) {
			throw new ChannelException(e);
		}
		if (isPoisoned()) {
			throw new ChannelPoisonedException();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Channel#getReadPort()
	 */
	public ReadPort<T> getReadPort(boolean claimed) throws IllegalStateException,
			ChannelException {
		if (isPoisoned()) {
			throw new ChannelPoisonedException();
		}
		if (readArity == PortArity.ONE) {
			if (readPorts.size() == 0) {
				ReadPort<T> newPort = new SimpleReadPort<T>(this, readPortLimit, claimed ? ActorFactory.getInstance().getCurrentActor() : null);
				if (readPorts.add(newPort) == true) {
					return newPort;
				}
			} else {
				throw new ChannelException("Port arity invalid for write port.");
			}
		} else {
			ReadPort<T> newPort = new SimpleReadPort<T>(this, readPortLimit, claimed ? ActorFactory.getInstance().getCurrentActor() : null);
			if (readPorts.add(newPort) == true) {
				return newPort;
			}
		}
		throw new ChannelException("Could not create ReadPort");
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Channel#getReadPortArity()
	 */
	public PortArity getReadPortArity() {
		return this.readArity;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Channel#getLocalId()
	 */
	public long getLocalId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Channel#getName()
	 */
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.BufferedChannel#getBufferCapacity()
	 */
	public int getBufferCapacity() {
		return this.capacity;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.BufferedChannel#size()
	 */
	public int size() {
		return this.values.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Channel#getWritePort()
	 */
	public WritePort<T> getWritePort(boolean claimed) throws IllegalStateException, ChannelException {
		if (isPoisoned()) {
			throw new ChannelPoisonedException();
		}
		if (writeArity == PortArity.ONE) {
			if (writePorts.size() == 0) {
				WritePort<T> newPort = new SimpleWritePort<T>(this, writePortLimit, claimed ? ActorFactory.getInstance().getCurrentActor() : null);
				if (writePorts.add(newPort) == true) {
					return newPort;
				}
			} else {
				throw new ChannelException("Port arity invalid for write port.");
			}
		} else {
			WritePort<T> newPort = new SimpleWritePort<T>(this, writePortLimit, claimed ? ActorFactory.getInstance().getCurrentActor() : null);
			if (writePorts.add(newPort) == true) {
				return newPort;
			}
		}
		throw new ChannelException("Could not create WritePort");
	}
	
	public void closePort(Port<T> port) {
		if (port instanceof ReadPort) {
			readPorts.remove(port);
		} else {
			writePorts.remove(port);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Channel#getWritePortArity()
	 */
	public PortArity getWritePortArity() {
		return writeArity;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Poisonable#poison()
	 */
	public void poison() throws IllegalStateException, ChannelException {
		this.poisoned = true;
		shutdown();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.channels.Poisonable#isPoisoned()
	 */
	public boolean isPoisoned() throws IllegalStateException {
		return this.poisoned;
	}
	
	private void shutdown() {
		/*
		 * Clear the buffer of any outstanding value(s).
		 */
		this.values.clear();
		try {
			/*
			 * Now post a sentinel to wake up any remaining threads.
			 */
			this.values.put(new Item<T>());
		} catch (InterruptedException e) {
		}
		/*
		 * Orphan all the outstanding ports, we are not allowed to create any more.
		 */
		this.readPorts.clear();
		this.writePorts.clear();
	}

}
