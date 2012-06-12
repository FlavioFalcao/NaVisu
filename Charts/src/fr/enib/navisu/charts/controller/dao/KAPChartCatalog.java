/*
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * (2012)
 */

package fr.enib.navisu.charts.controller.dao;

import fr.enib.navisu.charts.controller.parser.kap.KAPParser;
import fr.enib.navisu.charts.model.kap.KAPChart;
import fr.enib.navisu.common.catalog.Catalog;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author Thibault PENSEC & Jordan MENS
 * @date 02/04/2012
 */
public class KAPChartCatalog extends Catalog<KAPChart> {

    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(KAPChartCatalog.class.getName());
    /** Parser */
    private KAPParser parser;

    public KAPChartCatalog() {
        parser = new KAPParser();
    }
    
    /**
     * Call <code>void parseRecursively(Path root)</code> method
     * @param chartsPath 
     * @see KAPChartCatalog#parseRecursively(java.nio.file.Path) 
     */
    public KAPChartCatalog(String chartsPath) {
        this();
        parseRecursively(Paths.get(chartsPath));
    }
    
    /**
     * Parse recursively kap files from fiven root folder
     * @param root 
     */
    public final void parseRecursively(Path root) {
        //LOGGER.log(Level.INFO, root.toAbsolutePath().toString());
        for(File f : root.toFile().listFiles()) {
            if(f.isDirectory() && !f.getName().startsWith(".")) {
                parseRecursively(f.toPath());
            }
            if(f.getName().toLowerCase().endsWith(".kap")) {
                parser = new KAPParser();
                add(parser.parse(f.getAbsolutePath()));
            }
        }
    }
}
