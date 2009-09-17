/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.services;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.channels.BufferedChannel;
import com.googlecode.acpj.channels.ChannelException;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.ChannelRegistry;
import com.googlecode.acpj.channels.ReadPort;

/**
 * <p>
 * Captures the notion of a service which is a particular Actor pattern:
 * </p>
 * <ul>
 *   <li>Starts early on in application initialization and effectively runs for
 *       ever.</li>
 *   <li>Has a buffered, any-to-one <em>request queue</em> which clients use to
 *       send requests.</li>
 *   <li>This queue is registered for clients to discover.</li>
 *   <li>The service is stopped when the request queue is poisoned.</li>
 * </ul>
 * <p>
 * As an example of if it's use consider the included simple LogService:
 * </p>
 * <pre>
 *    public class LogService extends BasicService<LogRecord> {
 *        
 *        public static final String CHANNEL_NAME = "com.googlecode.acpj.services.RequestChannel";
 *        public static final String ACTOR_NAME = "com.googlecode.acpj.services.LoggerActor";
 *    
 *        public LogService() {
 *            setChannelName(CHANNEL_NAME);
 *            setActorName(ACTOR_NAME);
 *        }
 *    
 *        public void run() {
 *            Logger logger = Logger.getLogger("com.googlecode.acpj.services.logger");
 *            try {
 *                getReadPort().claim();
 *            } catch (Throwable t) {
 *                logger.log(Level.SEVERE, "Failed to claim logger port.", t);
 *            }
 *            while (true) {
 *                LogRecord request =  null;
 *                try {
 *                    request = getNextRequest();
 *                } catch (ChannelPoisonedException e) {
 *                    break;
 *                } catch (Throwable t) {
 *                    logger.log(Level.SEVERE, "Failed to read logger queue.", t);
 *                }        
 *                logger.log(request);
 *            }
 *        }
 *    }
 * </pre>
 * <p>
 * Starting and stopping the service and managing registration of the request queue and so
 * forth are handled by this class, in fact the <code>setChannelName</code> and
 * <code>setActorName</code> are not necessary in the constructor as default names will
 * be chosen if not supplied. The basic structure of a <code>run()</code> method is now: 
 * </p>
 * <ul>
 *   <li>claim the read port - <code>getReadPort().claim()</code>.</li>
 *   <li>some form of loop, usually <code>while(true)</code>.</li>
 *   <li>fetch the next request from the queue - <code>request = getNextRequest()</code>.</li>
 *   <li>process the request, sending out other messages, or even replying to
 *       this request (see {@link com.googlecode.acpj.channels.util.RequestWithCallbackPattern}).<li>
 *   <li>on the read detect the {@link com.googlecode.acpj.channels.ChannelPoisonedException}
 *       and exit the loop.</li>
 * </ul>
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public abstract class BasicService<RT> implements Runnable {
	
	private String channelName = null;
	private String actorName = null;
	
	private BufferedChannel<RT> requestChannel = null;
	private Actor serviceActor = null;
	private Object serviceLock = new Object();
	
	private ReadPort<RT> requestPort = null;
	
	/**
	 * Default constructor, allows framework creation of services.
	 */
	public BasicService() {
	}

	/**
	 * Return the request channel registered name.
	 *  
	 * @return the channel name as used in the channel registry.
	 */
	protected String getChannelName() {
		return channelName;
	}

	/**
	 * Set the channel registered name. Note that channels are only 
	 * registered during {@link #start()}, if you call this method
	 * when the service is running nothing will happen.
	 * 
	 * @param channelName the new channel name.
	 */
	protected void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * Return the logical name of the service actor.
	 * 
	 * @return the name of the service actor.
	 */
	protected String getActorName() {
		return actorName;
	}

	/**
	 * Set the logical name of the service actor. As actors are only
	 * created during {@link #start()}, if you call this method
	 * when the service is running nothing will happen.
	 * 
	 * @param actorName the new name
	 */
	protected void setActorName(String actorName) {
		this.actorName = actorName;
	}

	/**
	 * Retrieve the read port for the request channel.
	 * 
	 * @return the read port connected to the service request channel.
	 */
	protected ReadPort<RT> getReadPort() {
		return this.requestPort;
	}
	
	/**
	 * Retrieve the next request message from the service request channel.
	 * 
	 * @return the next request message to process.
	 * 
	 * @throws ChannelException
	 */
	protected RT getNextRequest() throws ChannelException {
		return this.requestPort.read();
	}
	
	/**
	 * Start the service, note that this will do nothing if the service
	 * is already running.
	 * 
	 * @return the instance of the actor actually performing this service.
	 */
	public Actor start() {
		synchronized (this.serviceLock) {
			if (this.serviceActor == null || !this.serviceActor.isRunning()) {
				if (channelName == null) {
					channelName = this.getClass().getCanonicalName();
				}
				if (actorName == null) {
					actorName = this.getClass().getCanonicalName();
				}
				this.requestChannel = ChannelFactory.getInstance().createAnyToOneChannel(channelName, BufferedChannel.BUFFER_CAPACITY_UNLIMITED);
				ChannelRegistry.getInstance().register(requestChannel, channelName, true);
				this.requestPort = (ReadPort<RT>)requestChannel.getReadPort(false); 
				serviceActor = ActorFactory.getInstance().createActor(this, actorName);
			}
		}
		return serviceActor;
	}
	
	/**
	 * Determines whether this service is actually running.
	 * 
	 * @return <code>true</code> if the service actor is running.
	 */
	public boolean isRunning() {
		return (serviceActor != null || serviceActor.isRunning());
	}
	
	/**
	 * Stop the service, note that this will do nothing if the service is not
	 * currently running.
	 */
	public void stop() {
		synchronized (serviceLock) {
			if (serviceActor != null && serviceActor.isRunning()) {
				ChannelRegistry.getInstance().deregister(channelName);
				requestChannel.poison();
				requestChannel = null;
				serviceActor = null;
			}
		}
	}
	
	/**
	 * This is the actual service method that will process request messages.
	 */
	public abstract void run(); 
	
}
