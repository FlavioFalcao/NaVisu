package fr.enib.navisu.charts.controller.parser.kap.sentence;

import fr.enib.navisu.common.parser.SentenceParser;
import fr.enib.navisu.charts.model.kap.KAPChart;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public abstract class KAPSentenceParser extends SentenceParser {

    private KAPChart kapChart;
    
    /**
     * 
     */
    public KAPSentenceParser() {
        super();
    }
    
    /**
     * 
     * @param line
     */
    public KAPSentenceParser(String line) {
        super(line);
    }
    

    /**
     * 
     * @return
     */
    public KAPChart getChart() {
        return kapChart;
    }

    /**
     * 
     * @param kapChart
     */
    public void setKapChart(KAPChart kapChart) {
        this.kapChart = kapChart;
    }
}
