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
public class NTM extends KAPSentenceParser {

    public NTM() {
        super();
    }

    public NTM(String line) {
        super(line);
    }
    
    @Override
    public void parse() {
        
        StringTokenizer st = new StringTokenizer(this.getSentence(), ",");
        this.getChart().setNtmEditionNumber(Double.parseDouble(st.nextToken().split("=")[1]));
        
        String ntmDate = st.nextToken().split("=")[1];
        DateFormat df = new SimpleDateFormat(Utils.DD_MM_YYYY_DATE_PATTERN);
        try {
            this.getChart().setNtmDate(df.parse(ntmDate));
        } catch (ParseException ex) {
            Logger.getLogger(NTM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
