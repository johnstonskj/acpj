/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.tests.internal;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Internal Tests for com.googlecode.acpj");
		//$JUnit-BEGIN$
		suite.addTestSuite(ZBQTests.class);
		suite.addTestSuite(ExecutorTests.class);
		//$JUnit-END$
		return suite;
	}

}
