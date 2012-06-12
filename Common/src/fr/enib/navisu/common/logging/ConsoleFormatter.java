/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */

package fr.enib.navisu.common.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 03/04/2012
 */
public class ConsoleFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return "[" + record.getLevel().getName() + "]"
                + "[" + record.getSourceClassName() + "][" + record.getSourceMethodName() + "]: "
                + record.getMessage() + "\n";
    }
}
