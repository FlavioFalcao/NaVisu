package fr.enib.navisu.charts.controller.parser.kap.sentence;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public class VER extends KAPSentenceParser {

    /**
     * 
     */
    public VER() {
        super();
    }

    /**
     * 
     * @param line
     */
    public VER(String line) {
        super(line);
    }
    
    /**
     * 
     */
    @Override
    public void parse() {
        getChart().setVersion(getSentence());
    }
}
