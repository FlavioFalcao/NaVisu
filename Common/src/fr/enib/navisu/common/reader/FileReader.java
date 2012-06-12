package fr.enib.navisu.common.reader;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public class FileReader implements Reader {

    private Path filePath;
    private List<String> entries = null;
    private String stop;

    /**
     *
     */
    public FileReader() {
        this(FileSystems.getDefault().getPath(""), "");
    }

    /**
     *
     * @param fileName
     * @param stop
     */
    public FileReader(Path fileName, String stop) {
        this.filePath = fileName;
        this.stop = stop;
        entries = new ArrayList<>();
    }

    /**
     * Get the value of fileName
     *
     * @return the value of fileName
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * Set the value of fileName
     *
     * @param filePath new value of fileName
     */
    public void setFileName(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Read the file fileName, and return a list of sentences
     *
     * @return
     */
    @Override
    public List<String> read() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(filePath.toString()), "ISO-8859-1"));
            /*
             * String line; while ((line = bufferedReader.readLine()) != null) {
             * if (line.contains(stop)) { break; } entries.add(line + "\n"); }
             *
             */
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.contains(stop)) {
                    break;
                }
                entries.add(line + "\n");
                line = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return entries;
    }
}
