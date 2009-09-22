/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.tests;

import java.net.URI;

import com.googlecode.acpj.actors.Actor;
import com.googlecode.acpj.actors.ActorFactory;
import com.googlecode.acpj.patterns.ActorJoinPool;
import com.googlecode.acpj.patterns.BasicActorJoinPool;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class SimpleActorTests extends TestCase {

	public class SimpleActor implements Runnable {
		public void run() {
			System.out.println(String.format("Actor <%s> running.", ActorFactory.getInstance().getCurrentActor().getName()));			
		}
	}
	
	public class SimpleActorWithSleep implements Runnable {
		public void run() {
			System.out.println(String.format("Actor <%s> sleeping.", ActorFactory.getInstance().getCurrentActor().getName()));
			try {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(String.format("Actor <%s> ending.", ActorFactory.getInstance().getCurrentActor().getName()));			
		}
	}
	
	public void test001_CreateBadActor() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));
		
		try {
			ActorFactory.getInstance().createActor(null);
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
		
		try {
			ActorFactory.getInstance().createActor(null, "name");
			fail();
		} catch (IllegalArgumentException e) {
			// success
		}
	}

	public void test002_CreateSimpleActor() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));

		System.out.println("Starting simple actor.");
		Actor actor = ActorFactory.getInstance().createActor(new SimpleActor());
		System.out.println("Started simple actor.");
		/*
		 * Validate the constructed actor name
		 */
		URI actorName = new URI(actor.getName());
		// check URI scheme
		Assert.assertEquals(actorName.getScheme(), "actor");
		// check URI path, should be the integer actor ID
		String id = actorName.getPath().substring(1);
		new Long(id);
		Thread.sleep(1000);
	}

	public void test003_CreateSimpleNamedActor() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));

		System.out.println("Starting simple actor.");
		Actor actor = ActorFactory.getInstance().createActor(new SimpleActor(), "my simple actor");
		System.out.println("Started simple actor.");
		/*
		 * Validate the constructed actor name
		 */
		Assert.assertTrue(actor.getName().contains("/my simple actor/"));
	}

	public void test004_CreateActorJoinPoolAll() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));
		
		ActorJoinPool pool = new BasicActorJoinPool();
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		
		System.out.println("About to join");
		pool.joinAll();
		System.out.println("Completed join");
	}

	public void test005_CreateActorJoinPoolAny() throws Exception {
		System.out.println(String.format("===== %s =====", getName()));
		
		ActorJoinPool pool = new BasicActorJoinPool();
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		pool.createActor(new SimpleActorWithSleep());
		
		System.out.println("About to join");
		pool.joinAny();
		System.out.println("Completed join");
	}
}
