/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.enib.navisu.app.controller.actions;

import fr.enib.navisu.app.AppTopComponent;
import fr.enib.navisu.charts.controller.ChartsController;
import fr.enib.navisu.charts.model.Chart;
import fr.enib.navisu.charts.model.kap.KAPChart;
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
public class GoToChartNodeAction extends AbstractAction {

    private static final Logger LOGGER = Logger.getLogger(GoToChartNodeAction.class.getName());
    private Chart kapChart;
    private static WorldWindowGLCanvas wwd = (WorldWindowGLCanvas) AppTopComponent.wwd();
    //private static KAPController appController = AppTopComponent.controller();
    private static ChartsController appController = AppTopComponent.controller();
    private WWChart chart;

    public GoToChartNodeAction(Chart kapChart) {
        this.kapChart = kapChart;
        initialize();
    }

    private void initialize() {
        
        chart = appController.findWWChart(kapChart);
        
        if(chart.isVisible()) {
            putValue(NAME, "Hide");
        } else {
            putValue(NAME, "Goto and display");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (!chart.isVisible()) {
            Sector sector = Sector.boundingSector(WWUtils.point2DToLatLon(kapChart.getPolygon().getCoordinates()));
            try {
                chart.setVisible(true);
            } catch (Exception ex) {
                AppTopComponent.io().getErr().println(ex.getMessage());
            }
            WWUtils.goTo(wwd, sector);
        } else {
            chart.setVisible(false);
        }
    }
}
