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
 * Internal - implementation of the standard Java interface
 * {@link java.util.concurrent.BlockingQueue} so as to be usable by the
 * {@link com.googlecode.acpj.internal.channels.SimpleChannel} instead 
 * of the usual {@link java.util.concurrent.LinkedBlockingQueue} used
 * when buffering for real.
 * </p>
 * <p>
 * WARNING: this is not a true implementation of the BlockingQueue API
 * as it only implements those operations used by our Channel implementation
 * and many of the others will raise {@link java.lang.UnsupportedOperationException}.
 * </p>
 * 
 * @author Simon Johnston (simon@johnstonshome.org)
 * @since 0.1.0
 * 
 */
public class ZeroBlockingQueue<E> implements BlockingQueue<E> {

	private final Lock lock = new ReentrantLock();
	private final Condition notFull  = this.lock.newCondition(); 
	private final Condition notEmpty = this.lock.newCondition(); 
	
	private E value;

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
	 */
	public void put(E o) throws InterruptedException {
		this.lock.lock();
		try {
			while (this.value != null) {
				this.notFull.await();
			}
			this.value = o;
			this.notEmpty.signal();
			while (this.value != null) {
				this.notFull.await();
			}
		} finally {
			this.lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#take()
	 */
	public E take() throws InterruptedException {
		E o = null;
		this.lock.lock();
		try {
			while (this.value == null) {
				this.notEmpty.await();
			}
			o = this.value;
			this.value = null;
			this.notFull.signal();
		} finally {
			this.lock.unlock();
		}
		return o;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
	 */
	public int drainTo(Collection<? super E> c) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#add(java.lang.Object)
	 */
	public boolean add(E o) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
	 */
	public int drainTo(Collection<? super E> c, int maxElements) {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
	 */
	public boolean offer(E o) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	public boolean offer(E o, long timeout, TimeUnit unit)
			throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
	 */
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.BlockingQueue#remainingCapacity()
	 */
	public int remainingCapacity() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Queue#element()
	 */
	public E element() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Queue#peek()
	 */
	public E peek() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Queue#poll()
	 */
	public E poll() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Queue#remove()
	 */
	public E remove() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	public int size() {
		return 0;
	}

	public Object[] toArray() {
		return new Object[0];
	}

	public <T> T[] toArray(T[] a) {
		if (a.length > 0) {
			a[0] = null;
		}
		return a;
	}

}
