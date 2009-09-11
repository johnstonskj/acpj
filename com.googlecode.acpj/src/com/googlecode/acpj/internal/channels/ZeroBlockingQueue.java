/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE included in the
 * distribution of this code.
 * 
 */
package com.googlecode.acpj.internal.channels;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ZeroBlockingQueue<E> implements BlockingQueue<E> {

	private final Lock lock = new ReentrantLock();
	private final Condition notFull  = lock.newCondition(); 
	private final Condition notEmpty = lock.newCondition(); 
	
	private E value;

	public void put(E o) throws InterruptedException {
		lock.lock();
		try {
			while (value != null) {
//				System.out.println("... waiting for client to take item (1)");
				notFull.await();
			}
//			System.out.println("... putting item");
			this.value = o;
			notEmpty.signal();
			while (value != null) {
//				System.out.println("... waiting for client to take item (2)");
				notFull.await();
			}
		} finally {
			lock.unlock();
		}
	}

	public E take() throws InterruptedException {
		E o = null;
		lock.lock();
		try {
			while (value == null) {
//				System.out.println("... waiting for server to put item");
				notEmpty.await();
			}
//			System.out.println("... taking item");
			o = this.value;
			this.value = null;
			notFull.signal();
		} finally {
			lock.unlock();
		}
		return o;
	}

	public int drainTo(Collection<? super E> c) {
		throw new UnsupportedOperationException();
	}

	public boolean add(E o) {
		throw new UnsupportedOperationException();
	}

	public int drainTo(Collection<? super E> c, int maxElements) {
		throw new UnsupportedOperationException();
	}

	public boolean offer(E o) {
		throw new UnsupportedOperationException();
	}

	public boolean offer(E o, long timeout, TimeUnit unit)
			throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	public int remainingCapacity() {
		return 0;
	}

	public E element() {
		throw new UnsupportedOperationException();
	}

	public E peek() {
		throw new UnsupportedOperationException();
	}

	public E poll() {
		throw new UnsupportedOperationException();
	}

	public E remove() {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
	}

	public boolean contains(Object o) {
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		return false;
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return 0;
	}

	public Object[] toArray() {
		return null;
	}

	public <T> T[] toArray(T[] a) {
		return null;
	}

}
