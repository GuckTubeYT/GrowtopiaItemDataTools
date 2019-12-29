package me.droreo002.tools.log;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import jline.console.ConsoleReader;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Erase;

public class ColouredWriter extends Handler {

    private final Map<ConsoleColor, String> replacements = new EnumMap<>( ConsoleColor.class );
    private final ConsoleColor[] colors = ConsoleColor.values();
    private final ConsoleReader console;

    public ColouredWriter(ConsoleReader console) {
        this.console = console;

        replacements.put( ConsoleColor.BLACK, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.BLACK ).boldOff().toString() );
        replacements.put( ConsoleColor.DARK_BLUE, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.BLUE ).boldOff().toString() );
        replacements.put( ConsoleColor.DARK_GREEN, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.GREEN ).boldOff().toString() );
        replacements.put( ConsoleColor.DARK_AQUA, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.CYAN ).boldOff().toString() );
        replacements.put( ConsoleColor.DARK_RED, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.RED ).boldOff().toString() );
        replacements.put( ConsoleColor.DARK_PURPLE, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.MAGENTA ).boldOff().toString() );
        replacements.put( ConsoleColor.GOLD, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.YELLOW ).boldOff().toString() );
        replacements.put( ConsoleColor.GRAY, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.WHITE ).boldOff().toString() );
        replacements.put( ConsoleColor.DARK_GRAY, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.BLACK ).bold().toString() );
        replacements.put( ConsoleColor.BLUE, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.BLUE ).bold().toString() );
        replacements.put( ConsoleColor.GREEN, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.GREEN ).bold().toString() );
        replacements.put( ConsoleColor.AQUA, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.CYAN ).bold().toString() );
        replacements.put( ConsoleColor.RED, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.RED ).bold().toString() );
        replacements.put( ConsoleColor.LIGHT_PURPLE, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.MAGENTA ).bold().toString() );
        replacements.put( ConsoleColor.YELLOW, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.YELLOW ).bold().toString() );
        replacements.put( ConsoleColor.WHITE, Ansi.ansi().a( Ansi.Attribute.RESET ).fg( Ansi.Color.WHITE ).bold().toString() );
        replacements.put( ConsoleColor.MAGIC, Ansi.ansi().a( Ansi.Attribute.BLINK_SLOW ).toString() );
        replacements.put( ConsoleColor.BOLD, Ansi.ansi().a( Ansi.Attribute.UNDERLINE_DOUBLE ).toString() );
        replacements.put( ConsoleColor.STRIKETHROUGH, Ansi.ansi().a( Ansi.Attribute.STRIKETHROUGH_ON ).toString() );
        replacements.put( ConsoleColor.UNDERLINE, Ansi.ansi().a( Ansi.Attribute.UNDERLINE ).toString() );
        replacements.put( ConsoleColor.ITALIC, Ansi.ansi().a( Ansi.Attribute.ITALIC ).toString() );
        replacements.put( ConsoleColor.RESET, Ansi.ansi().a( Ansi.Attribute.RESET ).toString() );
    }

    public void print(String s) {
        for ( ConsoleColor color : colors ) {
            s = s.replaceAll( "(?i)" + color.toString(), replacements.get( color ) );
        }
        try {
            console.print( Ansi.ansi().eraseLine( Erase.ALL ).toString() + ConsoleReader.RESET_LINE + s + Ansi.ansi().reset().toString() );
            console.drawLine();
            console.flush();
        } catch ( IOException ignored) { }
    }

    @Override
    public void publish(LogRecord record) {
        if ( isLoggable( record ) ) {
            print( getFormatter().format( record ) );
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
