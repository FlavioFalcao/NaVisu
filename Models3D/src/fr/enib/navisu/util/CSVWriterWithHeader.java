/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.util;

import java.io.Writer;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Serge
 */
public class CSVWriterWithHeader extends CSVWriter {

    public CSVWriterWithHeader(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        super(writer, separator, quotechar, escapechar, lineEnd);
    }

    public CSVWriterWithHeader(Writer writer, char separator, char quotechar, String lineEnd) {
        super(writer, separator, quotechar, lineEnd);
    }

    public CSVWriterWithHeader(Writer writer, char separator, char quotechar, char escapechar) {
        super(writer, separator, quotechar, escapechar);
    }

    public CSVWriterWithHeader(Writer writer, char separator, char quotechar, String[] header) {
        super(writer, separator, quotechar);
        this.header = header;
    }

    public CSVWriterWithHeader(Writer writer, char separator, char quotechar) {
        super(writer, separator, quotechar);
    }

    public CSVWriterWithHeader(Writer writer, char separator) {
        super(writer, separator);
    }

    public CSVWriterWithHeader(Writer writer) {
        super(writer);
    }

    /**
     * Writes the entire list to a CSV file. The list is assumed to be a
     * String[]
     *
     * @param allLines a List of String[], with each String[] representing a
     * line of the file.
     */
    @Override
    public void writeAll(List allLines) {
        for (String s : header) {
            getPw().write(s);
            getPw().write("\n");
        }
        for (Iterator iter = allLines.iterator(); iter.hasNext();) {
            String[] nextLine = (String[]) iter.next();
            writeNext(nextLine);
        }
    }
    private String[] header;
}
