/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.tests;

import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.channels.BufferedChannel;
import com.googlecode.acpj.channels.Channel;
import com.googlecode.acpj.channels.ChannelFactory;
import com.googlecode.acpj.channels.ChannelRegistry;
import com.googlecode.acpj.channels.ReadPort;
import com.googlecode.acpj.channels.WritePort;
import com.googlecode.acpj.patterns.ActorJoinPool;
import com.googlecode.acpj.patterns.BasicActorJoinPool;

import junit.framework.TestCase;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class EndToEndTests extends TestCase {
	
	private class LogRecord {
		int level;
		String message;
		public LogRecord(int level, String message) {
			this.level = level;
			this.message = message;
		}
	}
	
	class LoggerActor implements Runnable {
		public void run() {
			BufferedChannel<LogRecord> logChannel = ChannelFactory.getInstance().createAnyToOneChannel("com.googlecode.acpj.logger", -1);
			ChannelRegistry.getInstance().register(logChannel, "com.googlecode.acpj.logger", false);
			try {
				ReadPort<LogRecord> readPort = logChannel.getReadPort(true);
				while (true) {
					LogRecord record = readPort.read();
					System.out.println(String.format("%02d %s", record.level, record.message));
					Thread.yield();
				}
			} catch (Exception e) {
				;
			}
		}		
	}
	
	class NumberGenerator implements Runnable {
		private int id = 0;
		public NumberGenerator(int id) {
			this.id = id;
		}
		public void run() {
			Channel<LogRecord> logChannel = ChannelRegistry.getInstance().lookup("com.googlecode.acpj.logger");
			WritePort<LogRecord> writePort = logChannel.getWritePort(true);
			for (int i = 0; i < 10; i++) {
				writePort.write(new LogRecord(1, String.format("A number, %d, from %d", i, this.id)));
			}
		}
	}
	
	public void testCreateLoggerExample() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));
				
		ActorFactory.getInstance().createActor(new LoggerActor());
		Thread.sleep(2000);
		
		ActorJoinPool pool = new BasicActorJoinPool();

		for (int i = 0; i < 5; i++) {
			pool.createActor(new NumberGenerator(i));
		}
		
		pool.joinAll();
	}

}
