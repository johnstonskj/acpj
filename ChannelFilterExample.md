# Introduction #

The code in the following example creates three actors, one which generates a series of random numbers between 0 and 100 and writes them out to a channel. Another process reads numbers from a channel and prints them. However, while we could have these two share the same channel and print out all the numbers generated we only want to print numbers greater than 50 so we use the **ChannelFilter** pattern to inject another actor that reads the channel of all numbers, filters out the unwanted ones and passes the rest on to the printing actor.

```
    public class RandomNumberGenerator implements Runnable {
        private WritePort writePort = null;
        private int count = 0;
        public RandomNumberGenerator(WritePort writePort, int count) {
            this.writePort = writePort;
            this.count = count;
        }
        public void run() {
            writePort.claim();
            Random random = new Random();
            for (int i = 0; i < this.count; i++) {
                writePort.write(new Integer(random.nextInt(100)));
            }
            writePort.poison();
        }
    }
    
    public class NumberFilter implements ChannelOperation {
        public Integer process(Integer message) {
            if (message.intValue() < 50) {
                return null;
            } else {
                return message;
            }
        }        
    }
    
    public class NumberPrinter implements Runnable {
        private ReadPort readPort = null;
        public NumberPrinter(ReadPort readPort) {
            this.readPort = readPort;
        }
        public void run() {
            try {
                readPort.claim();
                while (true) {
                    try {
                        Integer number = readPort.read();
                        System.out.println("Received " + number);
                    } catch (ChannelPoisonedException e) {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
    
    WatchdogService.start();
        
    Channel rawChannel = ChannelFactory.getInstance().createOneToOneChannel(); 
    Channel filteredChannel = ChannelFactory.getInstance().createOneToOneChannel();
        
    ActorJoinPool pool = new BasicActorJoinPool();

    pool.addActor(
        new NumberPrinter(filteredChannel.getReadPort(false)), 
        "NumberPrinter");
    pool.addActor(
        new ChannelFilter(rawChannel.getReadPort(false), filteredChannel.getWritePort(false), new NumberFilter(), true), 
        "ChannelFilter");
    pool.addActor(
        new RandomNumberGenerator(rawChannel.getWritePort(false), 50), 
        "RandomNumberGenerator");
        
    pool.joinAll();
```

The following provides some more details.

  * Note that actors are coded simply as Java classes that implement the Runnable interface, in the same way you create thread classes today. RandomNumberGenerator, NumberFilter and NumberPrinter are all implementations of Runnable with constructors that take one or more Ports.
  * Note that the readers simply read forever, but that the writer has a particular way to communicate that all work is done, the RandomNumberGenerator actor poisons the channel it is writing to which renders the channel unusable from that point on, and this poison propogates through the filter and to the printing actor.
  * In the code that starts up the network note that we first start the WatchdogService which is a monitor that reports the birth and death of all actors and allows us to tell if an actor died due to an exception.
  * The network consists of two one-to-one Channels, each of these by default allows multiple read or write operations per Port and the channel itself is unbuffered.
  * Finally the three actors are created using the ActorFactory so that the actual details of the actor concurrency implementation is left as an implementation choice.