package fr.enib.navisu.common.parser;

import java.nio.file.Path;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec 
 * @date 7 mars 2012
 */
public abstract class FileParser implements Parser {

    private Path filePath;

    /**
     * @param filePath 
     */
    public FileParser(Path filePath) {

        this.filePath = filePath;
    }

    /**
     * @return
     */
    public Path getPath() {
        return filePath;
    }

    /**
     * @param chartPath
     */
    public void setPath(Path chartPath) {
        this.filePath = chartPath;
    }
}
