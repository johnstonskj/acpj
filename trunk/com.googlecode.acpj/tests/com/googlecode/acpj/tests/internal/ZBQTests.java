/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.tests.internal;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

import com.googlecode.acpj.internal.channels.ZeroBlockingQueue;

import junit.framework.TestCase;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ZBQTests extends TestCase {
	
	class Producer implements Runnable {
		private BlockingQueue<String> queue = null;
		private Random random = null;
		public Producer(BlockingQueue<String> queue, Random random) {
			this.queue = queue;
			this.random = random;
		}
		public void run() {
			try {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(this.random.nextInt(10000));
					System.out.println("<<< about to put to queue.");
					this.queue.put("here is message " + i);
					System.out.println("<<< put to queue.");
				}
				System.out.println("<<< done putting to queue.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class Consumer implements Runnable {
		private BlockingQueue<String> queue = null;
		private Random random = null;
		public Consumer(BlockingQueue<String> queue, Random random) {
			this.queue = queue;
			this.random = random;
		}
		public void run() {
			try {
				while (true) {
					System.out.println("<<< about to take from queue.");
					String value = this.queue.take();
					System.out.println("<<< taken '" + value + "' from queue.");
					if (value.equals("")) {
						System.out.println("<<< done taking from queue.");
						break;
					} 
					Thread.sleep(this.random.nextInt(10000));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testZeroBlockingQueue() throws Exception {
		Random random = new Random();
		BlockingQueue<String> queue = new ZeroBlockingQueue<String>();

		Thread consumer = new Thread(new Consumer(queue, random));
		Thread producer = new Thread(new Producer(queue, random));
		
		consumer.start();
		producer.start();
		
		producer.join();
		consumer.join();
	}
}
