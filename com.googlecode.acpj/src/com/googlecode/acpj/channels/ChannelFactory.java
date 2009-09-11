/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.channels;

import com.googlecode.acpj.internal.config.Configuration;

/**
 * <p>
 * A singleton factory class that provides a mechanism to disconnect the
 * definition of an {@link Channel} from the particular implementation used
 * to implement the underlying buffering. This class is thus a facade and relies
 * on another implementation of itself that provides the actual implementation.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public abstract class ChannelFactory {

	private static ChannelFactory instance = null;

	/**
	 * Retrieve the static (singleton) instance of the ChannelFactory. Note that 
	 * the particular implementation of this class used at runtime can be
	 * changed using the system property described by 
	 * {@link com.googlecode.acpj.Arguments#CFG_DEFAULT_CHANNEL_FACTORY_CLASS}.
	 * 
	 * @return the singleton ChannelFactory instance.
	 */
	public static synchronized ChannelFactory getInstance() {
		if (instance == null) {
			Class<? extends ChannelFactory> factory = Configuration.getDefaultChannelFactoryImpl();
			try {
				instance = factory.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
	
	public static final int PORT_LIMIT_UNLIMITED = -1;

	public abstract <T> Channel<T> createOneToOneChannel();

	public abstract <T> Channel<T> createOneToAnyChannel();

	public abstract <T> Channel<T> createAnyToOneChannel();

	public abstract <T> Channel<T> createAnyToAnyChannel();

	public abstract <T> BufferedChannel<T> createOneToOneChannel(int capacity);

	public abstract <T> BufferedChannel<T> createOneToAnyChannel(int capacity);

	public abstract <T> BufferedChannel<T> createAnyToOneChannel(int capacity);

	public abstract <T> BufferedChannel<T> createAnyToAnyChannel(int capacity);

	public abstract <T> Channel<T> createOneToOneChannel(String name) throws IllegalArgumentException;

	public abstract <T> Channel<T> createOneToAnyChannel(String name) throws IllegalArgumentException;

	public abstract <T> Channel<T> createAnyToOneChannel(String name) throws IllegalArgumentException;

	public abstract <T> Channel<T> createAnyToAnyChannel(String name) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createOneToOneChannel(String name, int capacity) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createOneToAnyChannel(String name, int capacity) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createAnyToOneChannel(String name, int capacity) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createAnyToAnyChannel(String name, int capacity) throws IllegalArgumentException;

	public abstract <T> Channel<T> createOneToOneChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException;

	public abstract <T> Channel<T> createOneToAnyChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException;

	public abstract <T> Channel<T> createAnyToOneChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException;

	public abstract <T> Channel<T> createAnyToAnyChannel(String name, int readPortLimit, int writePortLimit) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createOneToOneChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createOneToAnyChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createAnyToOneChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createAnyToAnyChannel(String name, int readPortLimit, int writePortLimit, int capacity) throws IllegalArgumentException;
	

	public abstract <T> Channel<T> createChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit) throws IllegalArgumentException;

	public abstract <T> BufferedChannel<T> createChannel(String name, PortArity readPortArity, int readPortLimit, PortArity writePortArity, int writePortLimit, int capacity) throws IllegalArgumentException;
}
