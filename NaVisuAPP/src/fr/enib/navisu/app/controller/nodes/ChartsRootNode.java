/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.controller.nodes;

import fr.enib.navisu.charts.model.kap.KAPChart;
import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 10/05/2012
 */
public class ChartsRootNode<T> extends AbstractNode {

    private Children kids;
    
    public ChartsRootNode(Children kids) {
        super(kids);
        this.kids = kids;
    }

    @Override
    public String getHtmlDisplayName() {
        return "<strong>Charts</strong>";
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("fr/enib/navisu/app/view/explorer/valid.gif");
    }
    
    @Override
    protected Sheet createSheet() {
        
        Sheet sheet = Sheet.createDefault();
        final KAPChart kapChart = getLookup().lookup(KAPChart.class);
        
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setDisplayName("Identification");
        sheet.put(set);
        
        Property<String> nameProp = new PropertySupport.ReadOnly<String>("getName", String.class, "Chart count", "Charts count in Catalog") {

            @Override
            public String getValue() {
                return  new Integer(kids.getNodesCount()).toString();
            }
        };
        set.put(nameProp);        
        
        return sheet;
    }
}
