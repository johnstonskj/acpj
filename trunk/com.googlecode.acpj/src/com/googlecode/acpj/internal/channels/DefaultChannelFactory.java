/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import com.googlecode.acpj.channels.BufferedChannel;
import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.PortArity;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class DefaultChannelFactory extends ChannelFactory {
	
	public <T> Channel<T> createAnyToAnyChannel() {
		return createChannel(null, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createAnyToAnyChannel(int capacity) {
		return createChannel(null, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createAnyToAnyChannel(String name) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createAnyToAnyChannel(String name, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createAnyToAnyChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ANY, writePortLimit);
	}

	public <T> BufferedChannel<T> createAnyToAnyChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ANY, writePortLimit, capacity);
	}

	
	
	public <T> Channel<T> createAnyToOneChannel() {
		return createChannel(null, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createAnyToOneChannel(int capacity) {
		return createChannel(null, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createAnyToOneChannel(String name) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createAnyToOneChannel(String name, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ANY, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createAnyToOneChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ANY, writePortLimit);
	}

	public <T> BufferedChannel<T> createAnyToOneChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ANY, writePortLimit, capacity);
	}

	
	
	public <T> Channel<T> createOneToAnyChannel() {
		return createChannel(null, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createOneToAnyChannel(int capacity) {
		return createChannel(null, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createOneToAnyChannel(String name) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createOneToAnyChannel(String name, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createOneToAnyChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ONE, writePortLimit);
	}

	public <T> BufferedChannel<T> createOneToAnyChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ANY, readPortLimit, PortArity.ONE, writePortLimit, capacity);
	}

	
	
	public <T> Channel<T> createOneToOneChannel() {
		return createChannel(null, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createOneToOneChannel(int capacity) {
		return createChannel(null, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createOneToOneChannel(String name) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED);
	}

	public <T> BufferedChannel<T> createOneToOneChannel(String name, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, PORT_LIMIT_UNLIMITED, PortArity.ONE, PORT_LIMIT_UNLIMITED, capacity);
	}

	public <T> Channel<T> createOneToOneChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ONE, writePortLimit);
	}

	public <T> BufferedChannel<T> createOneToOneChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException {
		return createChannel(name, PortArity.ONE, readPortLimit, PortArity.ONE, writePortLimit, capacity);
	}

	
	
	public <T> Channel<T> createChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit) throws IllegalArgumentException {
		return new SimpleChannel<T>(name, readPortArity, readPortLimit, writePortArity, writePortLimit, 0);
	}

	public <T> BufferedChannel<T> createChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit, int capacity) throws IllegalArgumentException {
		return new SimpleChannel<T>(name, readPortArity, readPortLimit, writePortArity, writePortLimit, capacity);
	}

}
