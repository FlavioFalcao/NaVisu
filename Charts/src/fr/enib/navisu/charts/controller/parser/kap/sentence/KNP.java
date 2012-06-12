package fr.enib.navisu.charts.controller.parser.kap.sentence;

import fr.enib.navisu.charts.model.kap.KAPChart;
import java.util.StringTokenizer;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public class KNP extends KAPSentenceParser {
    
    private static final String UNKNOWN = "UNKNOWN";
    private static final int DEFAULT_TEXT_ANGLE = 90;
    private static final String SC = "SC";
    private static final String GD = "GD";
    private static final String PR = "PR";
    private static final String PP = "PP";
    private static final String PI = "PI";
    private static final String SK = "SK";
    private static final String UN = "UN";
    private static final String SD = "SD";
    private static final String DX = "DX";
    private static final String DY = "DY";
    private static final String TA = "TA";
    
    
    /**
     * 
     */
    public KNP() {
        super();
    }

    /**
     * 
     * @param line
     */
    public KNP(String line) {
        super(line);
    }
    
    /**
     * 
     */
    @Override
    public void parse() {
        
        KAPChart kapChart = getChart();
        StringTokenizer st = new StringTokenizer(this.getSentence(), ",");
        String sentence, token = null, valueToken = null;
        int nbTokens = st.countTokens();
        
        for(int i = 0; i < nbTokens; i++) {
            sentence = st.nextToken();
            String[] tokens = sentence.split("=");
            token = tokens[0];
            if(!token.equals("SP") && tokens.length == 2) {
                valueToken = tokens[1]; 
            }
            
            switch(token) {
                
                case SC :
                    kapChart.setScale(Integer.parseInt(valueToken));
                    break;
                    
                case GD : 
                    kapChart.setGeodeticDatumName(valueToken);
                    break;
                    
                case PR :
                    kapChart.setProjectionName(valueToken);
                    break;
                    
                case PP : 
                    kapChart.setProjectionParameter(Double.parseDouble(valueToken));
                    break;
                    
                case PI :
                    if(valueToken.equalsIgnoreCase(UNKNOWN)) {
                        kapChart.setProjectionInterval(null);
                    } else {
                        kapChart.setProjectionInterval(Double.parseDouble(valueToken));
                    }
                    break;
                    
                case SK :
                    kapChart.setSkewAngel(Double.parseDouble(valueToken));
                    break;
                    
                case UN :
                    kapChart.setDepthUnits(valueToken);
                    break;
                    
                case SD :
                    kapChart.setSoundingDatum(valueToken);
                    break;
                    
                case DX : 
                    kapChart.setXResolution(Double.parseDouble(valueToken));
                    break;
                    
                case DY :
                    kapChart.setYResolution(Double.parseDouble(valueToken));
                    break;
                    
                case TA :
                    kapChart.setTextAngle(Double.parseDouble(valueToken));
                    break;
                    
                default :
                    kapChart.setTextAngle(DEFAULT_TEXT_ANGLE);
            }
        }
    }
}
