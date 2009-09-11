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
import com.googlecode.acpj.services.LogService;
import com.googlecode.acpj.services.WatchdogService;

import junit.framework.TestCase;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ServiceTests extends TestCase {

	class Works implements Runnable {

		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
	
	class Crashes implements Runnable {
		@SuppressWarnings("null")
		public void run() {
			String thing = null;
			thing.length();
		}
	}
	
	public void test001_WatchdogTests() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));
		WatchdogService.start();
		
		ActorFactory.getInstance().createActor(new Works(), "works");
		ActorFactory.getInstance().createActor(new Crashes(), "crashes");
		ActorFactory.getInstance().createActor(new Works(), "works");		
		Thread.sleep(500);
	}

	public void test002_WatchdogWithLoggerTests() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));
		LogService.start();
		WatchdogService.start();
		
		ActorFactory.getInstance().createActor(new Works(), "works");
		ActorFactory.getInstance().createActor(new Crashes(), "crashes");
		ActorFactory.getInstance().createActor(new Works(), "works");		
		Thread.sleep(500);
	}
}
