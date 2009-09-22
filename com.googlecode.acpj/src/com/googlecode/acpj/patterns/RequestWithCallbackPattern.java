/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.patterns;

import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.WritePort;

/**
 * <p>
 * Codifies the common pattern where an actor has to request another actor to 
 * perform some function and return some value; the requestor has to send to 
 * the service a callback where the value can be sent, in this case represented 
 * as a WritePort. 
 * </p>
 * <dl>
 *   <dt>DT</dt>
 *   <dd>Data type for the request.</dd>
 *   <dt>CT</dt>
 *   <dd>Data type for the callback response.</dd>
 * </dl>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class RequestWithCallbackPattern<DT, CT> {

	public CT requestWithCallback(WritePort<Request<CT>> requestPort, Channel<CT> callbackChannel) {
		/*
		 * Create a new request, passing in the original data as well as
		 * the write port for the callback channel.
		 */
		Request<CT> request = new Request<CT>(callbackChannel.getWritePort(false));
		/*
		 * Write the request on the service channel. 
		 */
		requestPort.write(request);
		/*
		 * Return the response from the service by reading the value
		 * sent down the channel from the service.
		 */
		return callbackChannel.getReadPort(true).read();
	}

	public CT requestWithCallback(WritePort<Request<CT>> requestPort) {
		/*
		 * Create a channel for the service to callback on.
		 * This is a one-shot channel, can only write to it once, can only
		 * read from it once and it has a buffer size of 1.
		 */
		Channel<CT> callbackChannel = ChannelFactory.getInstance().createOneToOneChannel("callback", 1, 1, 1);
		return requestWithCallback(requestPort, callbackChannel);
	}

	public CT requestWithCallback(WritePort<RequestWithData<DT,CT>> requestPort, DT data, Channel<CT> callbackChannel) {
		/*
		 * Create a new request, passing in the original data as well as
		 * the write port for the callback channel.
		 */
		RequestWithData<DT,CT> request = new RequestWithData<DT,CT>(data, callbackChannel.getWritePort(false));
		/*
		 * Write the request on the service channel. 
		 */
		requestPort.write(request);
		/*
		 * Return the response from the service by reading the value
		 * sent down the channel from the service.
		 */
		return callbackChannel.getReadPort(true).read();
	}

	public CT requestWithCallback(WritePort<RequestWithData<DT,CT>> requestPort, DT data) {
		/*
		 * Create a channel for the service to callback on.
		 * This is a one-shot channel, can only write to it once, can only
		 * read from it once and it has a buffer size of 1.
		 */
		Channel<CT> callbackChannel = ChannelFactory.getInstance().createOneToOneChannel("callback", 1, 1, 1);
		return requestWithCallback(requestPort, data, callbackChannel);
	}
}
