# Introduction #

This Page brings together discussions on future enhancements or possible improvements to ACP, some of these require some significant design work and here we ruminate on these problems.

## Mutable Messages ##

One issue today is that while CSP gives us a good abstraction for inter-actor communication it doesn't stop us doing something silly if we use mutable data as messages sent over channels. For example, I have a mutable object A, I send it over a channel to another process, that process can update the object and so can I - causing all the usual thread havoc.

So, is it possible to restrict the channel to only accepting immutable messages? Well, we could do a few tricks, such as:

  * Rely on documenting a rule _please don't pass immutable objects_ and wait and see how long before someone does and it all goes horribly wrong.
  * Rely on using the `Object.clone()` method to return copies of an object, that way either on write or read we'd clone the original. The issue here is that clone is usually implemented as a shallow copy and so unlikely to be completely safe.
  * Require all messages be serializable and on write we serialize to a byte array then de-serialize on read. Well, this would be a useful implementation as it will make network channels easier, but will be darned slow for in-process cases.

## Network Channels ##

For scaling out it would be nice to be able to send messages over channels to other processes, on other machines. To this end we'll need at least one network protocol and a set of interfaces for doing this. Some initial thoughts are:

  * Create a simple, fast, low-level TCP API first (like memcached) and build up the lib in Java code; the protocol might only support some channel types initially but should allow good performance.
  * Need to look not just at transferring messages but all of the signals to support blocking.
  * Look at how we support cases such as an N:1 channel with both local and remote writers - do we need a proxy actor locally to write to the same standard channel?
  * We would require all messages being sent over a network to be serializable.

## Port Sets ##

Another issue is the ability to multiplex ports, specifically on read we'd like to read on a set of ports simultaneously, something like this:

```
    PortSet set = new PortSet(3);
    set.add(channelA.getReadPort());
    set.add(channelB.getReadPort());
    set.add(channelC.getReadPort());

    int portIndex = set.select();
    if (portIndex == 0) {
        data = set.get(0).read();
    }
```

However this means somehow a single actor would have to be waiting on n-channels, noting that other actors may be waiting on those channels, potentially with their own overlapping port sets.

This may mean we'll have to implement our own channel data structures rather than relying on the `BlockingQueue` API in the Jav concurrency package.

## A Better Executor? ##

Currently we have a fixed thread pool and issues when the number of actors gets large, they can be queued but can't be in any way active. Do we need our own Thread scheduler so that we can actually support a larger number of active threads than there are pooled threads?