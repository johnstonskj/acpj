# Introduction #

The `com.googlecode.acpj.patterns` package contains a set of helper classes that provide the implementation of common patterns such as the request-response, message filter and join-pool. These patterns are general enough to be used as-is but simple enough that they can be replaced if necessary by more app-specific versions if you need them.

# Provided Patterns #

The following sections describe the patterns and their implementations as they exist currently.

## Request Response ##

May actors will perform work on behalf of another and will be able to return data to the requester, but because Channels are uni-directional the requester has to tell the serving Actor how to return the result. In this pattern the class takes two generic parameters, `DT` is the type of the request sent from the requester to the server, and `CT` is the callback type of the response. The pattern class uses two additional generic classes, `Request` and `RequestWithData` that are used as the actual payload to and fro the serving Actor.

```
public class RequestWithCallbackPattern<DT, CT> {
    public CT requestWithCallback(WritePort<Request<CT>> requestPort, Channel<CT> callbackChannel) ...
    public CT requestWithCallback(WritePort<Request<CT>> requestPort) ...
    public CT requestWithCallback(WritePort<RequestWithData<DT,CT>> requestPort, DT data, Channel<CT> callbackChannel) ...
    public CT requestWithCallback(WritePort<RequestWithData<DT,CT>> requestPort, DT data) ...
}
```

The example below demonstrates how this class is used, the serving actor takes a request and will return a new `UUID` instance back to the requester. The request itself has no associated data, and so it only carries the callback port over to the serving Actor.

```
    ActorFactory.getInstance().createActor(new UuidServer());
		
    Channel<Request<UUID>> requestChannel =
            ChannelRegistry.getInstance().lookupOrNull("test.uuid.server");
    WritePort<Request<UUID>> requestPort = 
            requestChannel.getWritePort(true);

    RequestWithCallbackPattern<Integer, UUID> guidRequestPattern = 
            new RequestWithCallbackPattern<Integer, UUID>(); 
		
    for (int i = 0; i < 5; i++) {
        UUID uuid = guidRequestPattern.requestWithCallback(requestPort);
        System.out.println("Got new UUID: " + uuid);
    }
```

Note that if you already have a data model for the channels used in your application and you're not able to use the `Request` classes used in this pattern then you'll find that the implementation of the pattern is pretty obvious and simple, you can re-implement for your own types relatively easily.

## Channel Filters ##

The notion of a channel filter is to create an actor that sits between two other actors, reading messages from one queue (originating with the _initial actor) and writing on the message to second queue (being read by the_terminal actor_). This basic pattern can implement a number of specific use-cases:_

  * True filtering, the message type in both channels are the same (which means that the initial and terminal could be directly connected together) and the purpose of the filter is to pass on some messages and drop others.
  * Monitoring, basically the same as above but rather than dropping messages the purpose is to monitor the messages, perhaps gathering statistics or logging particular messages. This allows the monitoring to be parameterized separately from the originating and terminal actors.
  * Transformation, the message type in both channels are not the same (which means  that the initial and terminal **cannot** be directly connected together) and the purpose of the filter is to read a message, transform it and pass it on.

The pattern revolves around two types, a class `ChannelFilter` which is a pre-built Actor that will do the reading and writing and takes an instance of the interface `ChannelOperation` which you provide to perform the actual filter operation. Again, the `ChannelFilter` is simple enough that if you need to do more you can, but in many cases all you need to do is to provide a class that implements `ChannelOperation`.

For a more complete example, see the ChannelFilterExample wiki page.

## Join Pool ##

The Join Pool notion is to be able to perform another operation that is commonly used in traditional threaded applications, waiting for one or more started Actors to finish. This is most common where a main Actor spawns a number of children and then needs to wait for them to complete. In this case the pattern is provided as both an interface, `ActorJoinPool `, that allows you to create new Actors in the pool, and then using `joinAny` you can wait for one or more Actors to complete or use `joinAll` to wait for all the Actors to complete.

```
public interface ActorJoinPool {
    public void createActor(Runnable runnable) throws IllegalStateException, ActorException;
    public void createActor(Runnable runnable, ActorFactory factory) throws IllegalStateException, ActorException;
    public void joinAny() throws IllegalStateException, ChannelException;
    public void joinAll() throws IllegalStateException, ChannelException;
}
```

The implementation creates an any-to-one _completion_ channel and wraps the provided `Runnable` in code that, when the Actor completes will write a message to this completion channel.

```
    ActorJoinPool pool = new BasicActorJoinPool();
    pool.createActor(new SimpleActorWithSleep());
    pool.createActor(new SimpleActorWithSleep());
    pool.createActor(new SimpleActorWithSleep());
    pool.createActor(new SimpleActorWithSleep());
    pool.createActor(new SimpleActorWithSleep());
		
    pool.joinAll();
```