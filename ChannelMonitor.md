# Introduction #

This page describes the channel monitor interface and how it can be used to grab a snap-shot of the channels, ports and actors in a live system and format the output into some basic forms. The ChannelMonitor interface provides access to the set of channels in a system, and for each channel the set of read and write ports and for each port the owning actor.

## Sample Code ##

The following, a test case from the suite, creates two channels and three application actors as well as starting up the log and watchdog services in the background.

```
    new LogService().start();
    new WatchdogService().start();
		
    Channel<Integer> rawChannel = ChannelFactory.getInstance().createOneToOneChannel("RandomNumbers"); 
    Channel<Integer> filteredChannel = ChannelFactory.getInstance().createOneToOneChannel("FilteredNumbers");
		
    ActorFactory.getInstance().createActor(
        new NumberPrinter(filteredChannel.getReadPort(false)), 
        "NumberPrinter");
    ActorFactory.getInstance().createActor(
        new ChannelFilter<Integer, Integer>(
            rawChannel.getReadPort(false), 
            filteredChannel.getWritePort(false), 
            new NumberFilter(), 
            true), 
        "ChannelFilter");
    ActorFactory.getInstance().createActor(
        new RandomNumberGenerator(rawChannel.getWritePort(false), 500), 
        "RandomNumberGenerator");

    ChannelMonitorOutput.writeDOT(ChannelFactory.getInstance().getChannelMonitor(), System.err);
```

The final line grabs the ChannelMonitor and outputs the data as a [Graphviz](http://www.graphviz.org/) DOT file that can be rendered into various image formats.

## Graphviz Output ##

The `writeDOT` method will generate a complete `digraph` structure, and the the example above the generated file is shown below.

```
digraph channels {
  node [fontsize=10];
  node [shape="box" fillcolor="lightgray", style="filled" label="channel:/RandomNumbers/2"] channel_1;
  node [shape="ellipse" fillcolor="lightgray", style="filled" label="actor:/RandomNumberGenerator/4"] actor_4;
  actor_4 -> channel_1 [style="solid"];
  node [shape="ellipse" fillcolor="lightgray", style="filled" label="actor:/ChannelFilter/3"] actor_3;
  channel_1 -> actor_3 [style="solid"];
  node [shape="box" fillcolor="lightgray", style="filled" label="channel:/com.googlecode.acpj.services.LoggingChannel/0 {0/-1}"] channel_2;
  node [shape="ellipse" fillcolor="lightgray", style="filled" label="actor:/com.googlecode.acpj.services.WatchdogService/1"] actor_1;
  actor_1 -> channel_2 [style="solid"];
  node [shape="ellipse" fillcolor="lightgray", style="filled" label="actor:/com.googlecode.acpj.services.LogService/0"] actor_0;
  channel_2 -> actor_0 [style="solid"];
  node [shape="box" fillcolor="lightgray", style="filled" label="channel:/com.googlecode.acpj.services.WatchdogNotificationChannel/1 {0/-1}"] channel_3;
  node [shape="ellipse" fillcolor="lightgray", style="filled" label="actor:/NumberPrinter/2"] actor_2;
  actor_1 -> channel_3 [style="solid"];
  actor_3 -> channel_3 [style="solid"];
  actor_4 -> channel_3 [style="solid"];
  channel_3 -> actor_1 [style="solid"];
  node [shape="box" fillcolor="lightgray", style="filled" label="channel:/FilteredNumbers/3"] channel_4;
  actor_3 -> channel_4 [style="solid"];
  channel_4 -> actor_2 [style="solid"];
}
```

Once run through the `dot` command results in the following PNG image.

![http://acpj.googlecode.com/svn/trunk/com.googlecode.acpj/doc-resources/graph.png](http://acpj.googlecode.com/svn/trunk/com.googlecode.acpj/doc-resources/graph.png)

## RDF Output ##

The ChannelMonitorOutput class also provides methods to write out the monitor data as an RDF graph.