/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.patterns;

import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * Simple class representing a request for service and providing a 
 * WritePort to which the result should be sent.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class Request<CT> {

	private WritePort<CT> callbackPort = null;
	
	public Request(WritePort<CT> callbackPort) {
		this.setCallbackPort(callbackPort);
	}
	
	public void setCallbackPort(WritePort<CT> callbackPort) {
		this.callbackPort = callbackPort;
	}
	
	public WritePort<CT> getCallbackPort() {
		return this.callbackPort;
	}
}

