package fr.enib.navisu.charts.controller.parser.kap.sentence;

import fr.enib.navisu.charts.model.kap.KAPChart;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public class BSB extends KAPSentenceParser {

    private static final Logger LOGGER = Logger.getLogger(KAPSentenceParser.class.getName());
    
    /**
     * 
     */
    public BSB() {
        super();
    }

    /**
     * 
     * @param line
     */
    public BSB(String line) {
        super(line);
    }

    /**
     * 
     */
    @Override
    public void parse() {

        KAPChart chart = getChart();
        String sentence = getSentence();
        try {
            StringTokenizer st = new StringTokenizer(sentence, ",");
            String[] infos = st.nextToken().split("=");

            chart.setName(infos[1]);
            infos = st.nextToken().split("=");

            if (infos.length > 1) {
                chart.setNumber(Integer.parseInt(infos[1]));
            }

            infos = st.nextToken().split("=");
            Integer[] ra = {0, 0};
            ra[0] = Integer.parseInt(infos[1]);
            ra[1] = Integer.parseInt(st.nextToken());

            chart.setImageWidthHeight(ra);

            infos = st.nextToken().split("=");
            chart.setDrawingUnits(Integer.parseInt(infos[1]));
            
        } catch (NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "BSB{" + '}';
    }
}
