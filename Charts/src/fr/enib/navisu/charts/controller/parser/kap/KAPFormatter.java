package fr.enib.navisu.charts.controller.parser.kap;

import fr.enib.navisu.common.parser.FileParser;
import fr.enib.navisu.common.reader.FileReader;
import java.nio.file.Path;
import java.util.*;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 * 
 * @author Jordan Mens and Thibault Pensec 
 * @date 7 mars 2012
 */
public class KAPFormatter extends FileParser {

    private Set<String> tokens;
    private Map<String, List<String>> sentences;
    private final String commentChar = "!";
    private final String fourSpaces = "    ";

    /**
     *
     * @param chartPath
     * @param tokens
     */
    public KAPFormatter(Path chartPath, Set<String> tokens) {
        super(chartPath);
        sentences = new HashMap<>();
        this.tokens = tokens;
    }

    /**
     * Parse method
     */
    @Override
    public void parse() {

        List<String> lines = new FileReader(getPath(), "DTM/").read();
        String currentToken = null, tmp;
        StringBuilder fullLine = null;
        List<String> l;
        
        sentences.clear();

        for (String line : lines) {
            if (!line.startsWith(commentChar)) {
                tmp = startsWithToken(line);
                if (tmp != null) {
                    currentToken = tmp;
                    fullLine = new StringBuilder(removeCRLF(line).replace(currentToken, ""));
                } else if (currentToken != null && line.startsWith(fourSpaces)) {
                    fullLine.append(removeCRLF(line).replaceFirst(fourSpaces, ","));
                } else {
                    currentToken = null;
                }
                if (fullLine != null && currentToken != null) {
                    l = (sentences.get(currentToken) == null)
                            ? new ArrayList<String>()
                            : sentences.get(currentToken);
                    for (String s : l) {
                        if (fullLine.toString().contains(s)) {
                            tmp = s;
                        }
                    }
                    l.remove(tmp);
                    l.add(fullLine.toString());
                    sentences.put(currentToken, l);
                }
            }
        }
    }

    /**
     * @param line
     * @return The token, null otherwise
     */
    private String startsWithToken(String line) {
        if (line != null && line.length() > 0) {
            for (String token : tokens) {
                if (line.startsWith(token)) {
                    return token;
                }
            }
        }
        return null;
    }

    /**
     * @param s
     * @return The input string without "\r" and/or "\n" character(s)
     */
    private String removeCRLF(String s) {
        return s.replace("\r", "").replace("\n", "");
    }

    /**
     * @return sentences
     */
    public Map<String, List<String>> getSentences() {
        return sentences;
    }

    /**
     * @param tokens
     */
    public void setTokens(Collection<String> tokens) {
        this.tokens.removeAll(this.tokens);
        this.tokens.addAll(tokens);
    }

    /**
     * @return tokens
     */
    public Set<String> getTokens() {
        return tokens;
    }
}
