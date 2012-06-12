package fr.enib.navisu.charts.controller.parser.kap;

import fr.enib.navisu.common.event.EventProvider;
import fr.enib.navisu.common.parser.Parser;
import fr.enib.navisu.common.utils.Utils;
import fr.enib.navisu.charts.controller.parser.kap.sentence.KAPSentenceParser;
import fr.enib.navisu.charts.controller.parser.kap.sentence.KAPSentenceParserFactory;
import fr.enib.navisu.charts.model.kap.KAPChart;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Ecole Nationale d'Ingénieurs de Brest (ENIB) - France
 *
 * @author Thibault Pensec and Jordan Mens 
 * @date 8 mars 2012
 */
public class KAPParser implements Parser {

    private KAPChart chart;
    private KAPFormatter kapFormatter;

    /**
     * Default constructor
     */
    public KAPParser() {
        this("");
    }
    
    /**
     * @param path Path to the file to parse
     */
    public KAPParser(String path) {
        this(path, new String[]{"VER/", "BSB/", "KNP/", "CED/", "PLY/", "REF/"});
    }
    
    /**
     * @param tokens Array of tokens
     */
    public KAPParser(String... tokens) {
        this("", tokens);
    }
    
    /**
     * @param path Path to the file to parse
     * @param tokens Array of tokens which will be parse 
     */
    public KAPParser(String path, String... tokens) {
        kapFormatter = new KAPFormatter(Paths.get(path), Utils.arrayToSet(tokens));
    }

    /**
     * Overriden parse method
     * @see Parser#parse() 
     */
    @Override
    public void parse() {

        Map<String, List<String>> sentences;
        KAPSentenceParser parser;
        
        chart = new KAPChart();
        setID(kapFormatter.getPath().toString());
        kapFormatter.parse();
        sentences = kapFormatter.getSentences();
        
        for (String token : sentences.keySet()) {
            for (String sentence : sentences.get(token)) {
                parser = KAPSentenceParserFactory.getSentenceParser(token);
                parser.setKapChart(chart);
                parser.setSentence(sentence);
                parser.parse();
                chart = parser.getChart();
            }
        }
    }
    
    /**
     * Just call the parse method and return the chart
     * @param path
     * @return The parse chart
     * @see KAPParser#parse() 
     */
    public KAPChart parse(String path) {
        setPath(path);
        parse();
        return chart;
    }

    /**
     * @return The KAPChart
     */
    public KAPChart getChart() {
        return chart;
    }

    /**
     * @param path New path to the kap file to parse
     * @return Current instance of KAPParser
     */
    public KAPParser setPath(String path) {
        kapFormatter.setPath(Paths.get(path));
        return this;
    }
    
    /**
     * @return the path to the chart to format
     */
    public String getPath() {
        return kapFormatter.getPath().toString();
    }
    
    /**
     * Initialize the ID of the chart from its path
     * @param path The chart path
     */
    private void setID(String path) {
        int p1 = Utils.getFormattedPath(path).lastIndexOf(File.separator);
        int p2 = Utils.getFormattedPath(path).lastIndexOf(".");
        String id = Utils.getFormattedPath(path).substring(p1 + 1, p2);
        chart.setId(id);
    }
}
