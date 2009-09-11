/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.tests.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.googlecode.acpj.internal.actors.ActorPoolExecutor;

import junit.framework.TestCase;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ExecutorTests extends TestCase {
	
	class Printer implements Runnable {
		public void run() {
			Thread.yield();
			System.out.println(String.format("Thread %10d (%2d/%d) here.", this.hashCode(), Thread.currentThread().getId(), ActorPoolExecutor.getCurrentActor()));
			Thread.yield();
			System.out.println(String.format("Thread %10d (%2d/%d) still here.", this.hashCode(), Thread.currentThread().getId(), ActorPoolExecutor.getCurrentActor()));
			Thread.yield();
			System.out.println(String.format("Thread %10d (%2d/%d) done.", this.hashCode(), Thread.currentThread().getId(), ActorPoolExecutor.getCurrentActor()));
			Thread.yield();
		}
	}
	
	public void testExecutorThreadId() throws Exception {
		ExecutorService threadExecutor = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 10; i++) {
			threadExecutor.execute(new Printer());			
		}
		Thread.sleep(10000);
	}

	public void testExecutorActorId() throws Exception {
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService threadExecutor = new ActorPoolExecutor(processors * 8);
		for (int i = 0; i < 10; i++) {
			threadExecutor.execute(new Printer());			
		}
		Thread.sleep(10000);
	}
}
