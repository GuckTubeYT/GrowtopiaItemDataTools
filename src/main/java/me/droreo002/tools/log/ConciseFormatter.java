package me.droreo002.tools.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Taken from BungeeCord: https://github.com/SpigotMC/BungeeCord/blob/730715e68b7a6fe4b64e3b7a9b3b166d35f30abe/log/src/main/java/net/md_5/bungee/log/ConciseFormatter.java
 * @author md_5
 */
public class ConciseFormatter extends Formatter {

    private final DateFormat date = new SimpleDateFormat( System.getProperty( "net.md_5.bungee.log-date-format", "HH:mm:ss" ) );

    @Override
    public String format(LogRecord record)
    {
        StringBuilder formatted = new StringBuilder();

        formatted.append( date.format( record.getMillis() ) );
        formatted.append( " [" );
        formatted.append( record.getLevel().getLocalizedName() );
        formatted.append( "] " );
        formatted.append( formatMessage( record ) );
        formatted.append( '\n' );

        if ( record.getThrown() != null )
        {
            StringWriter writer = new StringWriter();
            record.getThrown().printStackTrace( new PrintWriter( writer ) );
            formatted.append( writer );
        }

        return formatted.toString();
    }
}
