# Introduction #

The notion of a Service is really an Actor pattern (see ChannelPatterns), specifically it describes a kind of Actor which is typically a singleton (there's only one of them per system), runs for a long time and possibly for the lifetime of the system and finally it has a single request channel to perform work on behalf of other Actors.

# Implementing a Service #

TBD

## The BasicService Class ##

```
public abstract class BasicService<RT> implements Runnable {
    public BasicService() ...
    public String getChannelName() ...
    protected void setChannelName(String channelName) ...
    public String getActorName() ...
    protected void setActorName(String actorName) ...

    protected ReadPort<RT> getReadPort() ...
    protected RT getNextRequest() throws ChannelException ...

    public Actor start() ...
    public boolean isRunning() ...
    public void stop() ...

    public void startup() ...
    public abstract boolean handleRequest(RT request);
    public void shutdown(boolean poisoned) ...

    public void run() ...
}
```

## Example Service ##

```
public class LogService extends BasicService<LogRecord> {
        
    public static final String CHANNEL_NAME = "com.googlecode.acpj.services.RequestChannel";
        
    private Logger logger = null;
    
    public LogService() {
        setChannelName(CHANNEL_NAME);
    }
  
    @Override
    public void startup() {
       this.logger = Logger.getLogger("com.googlecode.acpj.services.logger");
        super.startup();
    }
    
    @Override
    public boolean handleRequest(LogRecord request) {
        this.logger.log(request);
        return true;
    }
}
```
# Provided Services #

  * LoggerService; basically a wrapper around the standard Java logging API, the request channel takes a standard `LogRecord` instance and allows Actors to offload the I/O required for logging to a single service.
  * WatchdogService;