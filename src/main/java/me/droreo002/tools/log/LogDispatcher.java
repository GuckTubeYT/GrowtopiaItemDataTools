package me.droreo002.tools.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.LogRecord;

/**
 * Taken from BungeeCord: https://github.com/SpigotMC/BungeeCord/blob/730715e68b7a6fe4b64e3b7a9b3b166d35f30abe/log/src/main/java/net/md_5/bungee/log/LogDispatcher.java
 * @author md_5
 */
public class LogDispatcher extends Thread {

    private final MainLogger logger;
    private final BlockingQueue<LogRecord> queue = new LinkedBlockingQueue<>();

    public LogDispatcher(MainLogger logger)
    {
        super( "BungeeCord Logger Thread" );
        this.logger = logger;
    }

    @Override
    public void run()
    {
        while ( !isInterrupted() )
        {
            LogRecord record;
            try
            {
                record = queue.take();
            } catch ( InterruptedException ex )
            {
                continue;
            }

            logger.doLog( record );
        }
        for ( LogRecord record : queue )
        {
            logger.doLog( record );
        }
    }

    public void queue(LogRecord record)
    {
        if ( !isInterrupted() )
        {
            queue.add( record );
        }
    }
}
