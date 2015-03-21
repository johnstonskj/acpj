

# Introduction #

This page provides an introduction to the ACP/J API and it's intended usage, background on the ACP/J design and philosophy can be found on the [home page](http://acpj.googlecode.com/).

## Getting the library ##

Latest stable releases are available on the [downloads page](http://code.google.com/p/acpj/downloads/list), or alternatively you can [checkout](http://code.google.com/p/acpj/source/checkout) the latest source from Subversion. The library is developed in Eclipse so the root structure in Subversion is one or more Eclipse projects, ant scripts are included though for performing builds outside of Eclipse itself.

Each stable release should deliver _at least_ the following files:

  * `acpj-{version}.jar` - the built library itself (required).
  * `acpj-test-{version}.jar` - the built test suite for the library (optional).
  * `acpj-doc-{version}.jar` - the JavaDoc describing the library (optional).
  * `acpj-src-{version}.jar` - the library source code (optional).

Currently the ACP/J library has itself no external dependencies, the junit.jar is required (it is included in the lib directory of the Subversion repository) for the test suite to run.

# Getting Started #

We'll use a simple Producer-Consumer example here to show the basic features of the library and discuss some of the provided capabilities as well as the patterns expected to be used by developers to make the most of these capabilities. The producer in this case will generate a series of strings, the constructor for the producer taking a number indicating how many such strings to produce.

```
    public class ChannelProducer implements Runnable {
        private Channel<String> channel = null;
        private int limit;
        public ChannelProducer(Channel<String> channel, int limit) {
            this.channel = channel;
            this.limit = limit;
        }
        public void run() {
            try {
                WritePort<String> writePort = channel.getWritePort(true);
                final String name = writePort.getOwningActor().getName();
                for (int i = 0; i < limit; i++) {
                    try {
                        writePort.write("This is actor " + name + " calling.");
                    } catch (ChannelPoisonedException e) {
                        break;
                    }
                }
                channel.poison();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
```

This class looks pretty simple, the constructor just stores away the runtime parameters and the `run()` method from the `Runnable` interface does all the interesting work. Basically there's a loop, and for each iteration we write a message to the channel we've been provided.

The Consumer is, hopefully, as straightforward. It is similarly structured, with a `run()` method that in this case loops forever, until we detect that the producer has poisoned the channel between us.

```
    public class ChannelConsumer implements Runnable {
        private Channel<String> channel = null;
        public ChannelConsumer(Channel<String> channel) {
            this.channel = channel;
        }
        public void run() {
            try {
                ReadPort<String> readPort = channel.getReadPort(true);
                final String name = readPort.getOwningActor().getName();
                while (true) {
                    try {
                        final String value = readPort.read();
                        System.out.println("ChannelConsumer " + name + ", read '" + value.toString() + "'");
                    } catch (ChannelPoisonedException e) {
                        break;
                    }
                }
                System.out.println("ChannelConsumer " + name + " done.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
```

The code to start these two communicating is also relatively simple. First we create an anonymous one-to-one channel (the defaults here are that the port limit on both read and write is unlimited and that we create an unbuffered channel), then start the two actors passing the channel to each.

```
    Channel<String> channel = ChannelFactory.getInstance().createOneToOneChannel();

    ActorFactory.getInstance().createActor(new ChannelConsumer(channel));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel, 10));
```

## Passing Ports To Actors ##

In the example above we saw that the producer and consumer were each given a copy of the channel, and we trusted them to take the right port from the channel, that is the producer will take the write port and the consumer the read port. However, in some cases we have actors we may not necessarily trust, so we don't want to provide them with the whole channel, just with the port so that all they can do is the action (read or write) we've assigned them. The following snippet shows how we would change the initialization code, so we pass just a read port to the consumer and just a write port to the producer.

```
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
```

Note the `boolean` value we passed into the methods `getReadPort()` and `getWritePort()`, this denotes whether or not the port returned should be claimed and owned by the calling Actor, and in this case as we need the port to be used by another Actor we pass in `false`. In the original example above when the producer and consumer retrieved their own ports they passed in `true`, so if they are now being passed an unclaimed port how do they take ownership before using it? Well, the `run()` method now looks like this:

```
        public void run() {
            try {
                this.readPort.claim(); // readPort was passed to the constructor.
                final String name = readPort.getOwningActor().getName();
                while (true) {
                    try {
                        final String value = readPort.read();
                        System.out.println("ChannelConsumer " + name + ", read '" + value.toString() + "'");
                    } catch (ChannelPoisonedException e) {
                        break;
                    }
                }
                System.out.println("ChannelConsumer " + name + " done.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
```

So, in general it is better to pass only Port instances to Actors rather than Channels as it ensures a level of safety, but does mean you have to beware of `claim()` and `release()`.

## Channel Arity Independence ##

In general Actors who are passed ports in the manner described above have no way to know, and in fact should not care, about the arity of the channel they are connected to. This means that the same producer/consumer pair **should** be able to operate on a one-to-one, or any other form of channel. This is in fact the case, consider the following snippets.

The same classes above, but using a One-to-Any Channel.

```
    Channel<String> channel = ChannelFactory.getInstance().createOneToAnyChannel();
        
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
```

The same classes above, but using an Any-to-One Channel.

```
    Channel<String> channel = ChannelFactory.getInstance().createAnyToOneChannel();
        
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
```

The same classes above, but using an Any-to-Any Channel.

```
    Channel<String> channel = ChannelFactory.getInstance().createAnyToAnyChannel();
        
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelConsumer(channel.getReadPort(false)));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
    ActorFactory.getInstance().createActor(new ChannelProducer(channel.getWritePort(false), 10));
```

## Poisoning Channels ##

In the example above, when the producer was complete it poisoned the channel it was writing to, this is generally good behavior as it not only signals to consumers that the work is done, but does so in a **very** final way.

## More Information ##

For a more complete example, see the ChannelFilterExample wiki page.

For details on the patterns provided by the `util` package, see the ChannelPatterns wiki page.

For details on the service framework, see the ServiceActors wiki page.

For details on the channel monitor framework, see the ChannelMonitor wiki page.