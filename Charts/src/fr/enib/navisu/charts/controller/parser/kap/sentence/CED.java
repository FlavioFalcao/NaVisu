package fr.enib.navisu.charts.controller.parser.kap.sentence;

import fr.enib.navisu.common.utils.Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public class CED extends KAPSentenceParser {

    private static final Logger LOGGER = Logger.getLogger(CED.class.getName());
    
    public CED() {
        super();
    }

    public CED(String line) {
        super(line);
    }
    
    @Override
    public void parse() {
        
        try {
        StringTokenizer st = new StringTokenizer(this.getSentence(), ",");
        String[] tmp = st.nextToken().split("=");
        
        if(tmp.length > 1)
            this.getChart().setSourceEdition(Integer.parseInt(tmp[1]));
        
        tmp = st.nextToken().split("=");
        if(tmp.length > 1)
            this.getChart().setRasterEdition(tmp[1]);
        
        
        tmp = st.nextToken().split("=");
        String editionDate = "";
        if(tmp.length > 1)
            editionDate = tmp[1];
        
        if(!editionDate.equals("")) {
            DateFormat df = new SimpleDateFormat(Utils.DD_MM_YYYY_DATE_PATTERN);
            try {
                this.getChart().setEditionDate(df.parse(editionDate));
            } catch (ParseException ex) {
                LOGGER.log(Level.SEVERE, "Unable to parse " + editionDate, ex);
            }
        }
        } catch(NumberFormatException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
