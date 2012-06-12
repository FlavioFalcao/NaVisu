package fr.enib.navisu.charts.controller.parser.kap.sentence;

import java.awt.Point;
import java.util.StringTokenizer;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public class PLY extends KAPSentenceParser {

    public PLY() {
        super();
    }

    public PLY(String line) {
        super(line);
    }

    @Override
    public void parse() {
        
        int id; // Unused
        double lat, lon;
        
        StringTokenizer st = new StringTokenizer(getSentence(), ",");
        id = Integer.parseInt(st.nextToken());
        lat = Double.parseDouble(st.nextToken());
        lon = Double.parseDouble(st.nextToken());
        
        getChart().getPolygon().addCoordinate(new Point.Double(lat, lon));
    }
}
