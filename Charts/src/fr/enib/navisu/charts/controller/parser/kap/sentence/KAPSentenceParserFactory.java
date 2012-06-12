package fr.enib.navisu.charts.controller.parser.kap.sentence;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Jordan Mens and Thibault Pensec
 * @date 8 mars 2012
 */
public final class KAPSentenceParserFactory {

    private KAPSentenceParserFactory() {
    }

    /**
     * 
     * @param token
     * @return
     */
    public static KAPSentenceParser getSentenceParser(String token) {

        KAPSentenceParser kapSentenceParser = null;
        final int classNameLength = 3;
        
        try {
            String packageName = KAPSentenceParserFactory.class.getPackage().getName();
            String className = token.substring(0, classNameLength).toUpperCase();
            Class<?> cl = Class.forName(packageName + "." + className);
            kapSentenceParser = (KAPSentenceParser) cl.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(KAPSentenceParserFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return kapSentenceParser;
    }
}
