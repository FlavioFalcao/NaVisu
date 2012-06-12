/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.app.controller.nodes.ChartChildFactory.ChartNode;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.view.WWChart;
import fr.enib.navisu.common.utils.WWUtils;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Sector;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;


/**
 * @author Jordan MENS & Thibault PENSEC
 * @date 15/05/2012
 */
public class DisplayAndGotoChartNodeAction extends AbstractAction {

    private static final Logger LOGGER = Logger.getLogger(DisplayAndGotoChartNodeAction.class.getName());
    private Chart chart;
    private WWChart wwChart;
    private final ChartNode node;
    private static WorldWindowGLCanvas wwd = (WorldWindowGLCanvas) AppTopComponent.wwd();
    private static ChartsController appController = AppTopComponent.controller();

    public DisplayAndGotoChartNodeAction(final ChartNode node, Chart kapChart) {
        this.chart = kapChart;
        this.node = node;
        initialize();
    }

    private void initialize() {
        
        wwChart = appController.findWWChart(chart);
        
        if(wwChart.isVisible()) {
            putValue(NAME, "Hide");
        } else {
            putValue(NAME, "Display and Goto...");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        Sector sector = Sector.boundingSector(WWUtils.point2DToLatLon(chart.getPolygon().getCoordinates()));
        
        if (!wwChart.isVisible()) {
            wwChart.setVisible(true);
            WWUtils.goTo(wwd, sector);
        } else {
            wwChart.setVisible(false);
        }
        
        node.refresh();
    }
}
