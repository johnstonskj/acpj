/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.patterns;

import com.googlecode.acpj.actors.ActorException;
import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.channels.BufferedChannel;
import com.googlecode.acpj.channels.ChannelException;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.ReadPort;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * A basic implementation of the {@link ActorJoinPool} interface using a 
 * notification channel to 
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class BasicActorJoinPool implements ActorJoinPool {
	
	private class ActorWrapper implements Runnable {
		private Runnable actual = null;
		private WritePort<Boolean> completionWritePort = null;
		public ActorWrapper(Runnable actual, WritePort<Boolean> completionWritePort) {
			this.actual = actual;
			this.completionWritePort = completionWritePort;
		}
		public void run() {
			try {
				this.completionWritePort.claim();
				actual.run();
			} finally {
				this.completionWritePort.write(true);
				this.completionWritePort.close();
			}
		}
	}

	private BufferedChannel<Boolean> completetionChannel = null;
	private ReadPort<Boolean> completionReadPort = null;
	private int actorCount = 0;
	
	public BasicActorJoinPool() {
		this.completetionChannel = ChannelFactory.getInstance().createAnyToOneChannel(
				BasicActorJoinPool.class.getCanonicalName(), 
				ChannelFactory.PORT_LIMIT_UNLIMITED, // read port limit 
				1, // write port limit
				BufferedChannel.BUFFER_CAPACITY_UNLIMITED); 
		this.completionReadPort = completetionChannel.getReadPort(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.patterns.ActorJoinPool#addActor(java.lang.Runnable)
	 */
	public synchronized void createActor(Runnable runnable) throws IllegalStateException, ActorException {
		createActor(runnable, ActorFactory.getInstance());	
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.patterns.ActorJoinPool#addActor(java.lang.Runnable, com.googlecode.acpj.actors.ActorFactory)
	 */
	public synchronized void createActor(Runnable runnable, ActorFactory factory) throws IllegalStateException, ActorException {
		if (this.actorCount == -1) {
			throw new IllegalStateException("This pool has already been joined.");
		}
		factory.createActor(new ActorWrapper(runnable, completetionChannel.getWritePort(false)));
		actorCount++;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.patterns.ActorJoinPool#joinAll()
	 */
	public synchronized void joinAll() throws IllegalStateException, ChannelException {
		if (this.actorCount == -1) {
			throw new IllegalStateException("This pool has already been joined.");
		} else if (this.actorCount == 0) {
			throw new IllegalStateException("No actors in pool to join.");
		}
		int expectedCount = this.actorCount;
		this.actorCount = -1;

		for (int count = 0; count < expectedCount; count++) {
			this.completionReadPort.read();
		}
		this.completionReadPort.close();
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.acpj.patterns.ActorJoinPool#joinAny()
	 */
	public synchronized void joinAny() throws IllegalStateException, ChannelException {
		if (this.actorCount == -1) {
			throw new IllegalStateException("This pool has already been joined.");
		} else if (this.actorCount == 0) {
			throw new IllegalStateException("No actors in pool to join.");
		}
		this.actorCount = -1;
		this.completionReadPort.read();
		this.completionReadPort.close();
	}

}
