package fr.enib.navisu.common.parser;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Thibault Pensec and Jordan Mens
 * @date 8 mars 2012
 */
public abstract class SentenceParser implements Parser {

    private String sentence;

    
    /**
     * 
     */
    public SentenceParser() {
        
        this("");
    }
    
    /**
     * @param sentence 
     */
    public SentenceParser(String sentence) {
        this.sentence = sentence;
    }
    
    /**
     * @return the sentence
     */
    public String getSentence() {
        return sentence;
    }

    /**
     * @param sentence the line to set
     */
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
