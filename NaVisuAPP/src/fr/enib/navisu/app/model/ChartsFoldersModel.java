/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.enib.navisu.app.model;

import fr.enib.navisu.common.utils.Utils;
import java.util.TreeSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Thibault PENSEC & Jordan MENS
 */
@XmlRootElement
public class ChartsFoldersModel {
    
    public static final String XML_FILE_PATH = Utils.getDataCacheFolder() + "chartsfolders.xml";
    
    @XmlElement(name="chartsfolder")
    private TreeSet<String> chartsFolders;

    public ChartsFoldersModel() {
        chartsFolders = new TreeSet<>();
    }

    public ChartsFoldersModel(TreeSet<String> chartsFolders) {
        this.chartsFolders = chartsFolders;
    }
    
    public void addChartsFolder(String folder) {
        chartsFolders.add(folder);
    }
    
    public boolean removeChartsFolder(String folder) {
        return chartsFolders.remove(folder);
    }
    
    public boolean contains(String folder) {
        for(String s : chartsFolders)
            if(s.equals(folder))
                return true;
        return false;
    }
    
    public void clear() {
        chartsFolders.clear();
    }
    
    public int size() {
        return chartsFolders.size();
    }
    
    @XmlTransient
    public TreeSet<String> getChartsFolders() {
        return chartsFolders;
    }
}
