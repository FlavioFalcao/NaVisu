/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */

package fr.enib.navisu.common.logging;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 03/04/2012
 */
public class ConsoleHandler extends java.util.logging.Handler {
    
    public static final PrintStream OUT = System.out;
    public static final PrintStream ERR = System.err;
    
    private static final Level[] OUT_LEVELS = new Level[]{Level.INFO};
    private static final Level[] ERR_LEVELS = new Level[]{Level.WARNING, Level.SEVERE};
    
    public ConsoleHandler() {
        super();
        setFormatter(new ConsoleFormatter());
    }
    
    @Override
    public void publish(LogRecord record) {
        if(Arrays.asList(OUT_LEVELS).contains(record.getLevel())) {
            OUT.print(getFormatter().format(record));
        } 
        else if(Arrays.asList(ERR_LEVELS).contains(record.getLevel())) {
            ERR.print(getFormatter().format(record));
        }
        flush();
    }

    @Override
    public void flush() {
        OUT.flush();
        ERR.flush();
    }

    @Override
    public void close() throws SecurityException {
        OUT.close();
        ERR.close();
    }
}
