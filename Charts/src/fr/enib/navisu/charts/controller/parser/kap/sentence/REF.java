package fr.enib.navisu.charts.controller.parser.kap.sentence;

import java.util.StringTokenizer;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Thibault Pensec & Jordan Mens
 * @date 22 mars 2012
 */
public class REF extends KAPSentenceParser {
    
    public REF() {}
    
    public REF(String line) {
        super(line);
    }

    @Override
    public void parse() {
        int id;
        double lat, lon;
        StringTokenizer st = new StringTokenizer(getSentence(), ",");
        id = Integer.parseInt(st.nextToken());
        st.nextToken(); // On passe les 2 entiers inutiles
        st.nextToken(); 
        lat = Double.parseDouble(st.nextToken());
        lon = Double.parseDouble(st.nextToken());
        
        getChart().addReferencePoint(lat, lon);
    }
}
